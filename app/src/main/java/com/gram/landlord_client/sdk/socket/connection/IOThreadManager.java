package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.entity.JsonReq;
import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IIOManager;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;
import com.gram.landlord_client.sdk.socket.interfaces.IReader;
import com.gram.landlord_client.sdk.socket.interfaces.IReaderProtocol;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;
import com.gram.landlord_client.sdk.socket.interfaces.IWriter;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;
import com.orhanobut.logger.Logger;
import java.io.InputStream;
import java.io.OutputStream;

public class IOThreadManager implements IIOManager<OkSocketOptions> {
    private InputStream mInputStream;
    private OutputStream mOutputStream;
    private volatile OkSocketOptions mOkOptions;
    private IStateSender mSender;
    private IReader mReader;
    private IWriter mWriter;
    private AbsLoopThread mSimplexThread;
    private DuplexReadThread mDuplexReadThread;
    private DuplexWriteThread mDuplexWriteThread;
    private OkSocketOptions.IOThreadMode mCurrentThreadMode;

    public IOThreadManager(InputStream inputStream, OutputStream outputStream, OkSocketOptions okOptions, IStateSender stateSender) {
        mInputStream = inputStream;
        mOutputStream = outputStream;
        mOkOptions = okOptions;
        mSender = stateSender;
        initIO();
    }

    private void initIO() {
        assertHeaderProtocolNotEmpty();
        mReader = new ReaderImpl();
        mReader.initialize(mInputStream, mSender);
        mWriter = new WriterImpl();
        mWriter.initialize(mOutputStream, mSender);
    }

    public void startEngine() {
        mCurrentThreadMode = mOkOptions.getIOThreadMode();
        mReader.setOption(mOkOptions);
        mWriter.setOption(mOkOptions);
        switch(mOkOptions.getIOThreadMode()) {
            case DUPLEX:
                Logger.d("DUPLEX is processing");
                duplex();
                break;
            case SIMPLEX:
                Logger.w("SIMPLEX is processing");
                simplex();
                break;
            default:
                throw new RuntimeException("未定义的线程模式");
        }

    }

    private void duplex() {
        shutdownAllThread((Exception)null);
        mDuplexWriteThread = new DuplexWriteThread(mWriter, mSender);
        mDuplexReadThread = new DuplexReadThread(mReader, mSender);
        mDuplexWriteThread.start();
        mDuplexReadThread.start();
    }

    private void simplex() {
        shutdownAllThread((Exception)null);
        mSimplexThread = new SimplexIOThread(mReader, mWriter, mSender);
        mSimplexThread.start();
    }

    private void shutdownAllThread(Exception e) {
        if (mSimplexThread != null) {
            mSimplexThread.shutdown(e);
            mSimplexThread = null;
        }

        if (mDuplexReadThread != null) {
            mDuplexReadThread.shutdown(e);
            mDuplexReadThread = null;
        }

        if (mDuplexWriteThread != null) {
            mDuplexWriteThread.shutdown(e);
            mDuplexWriteThread = null;
        }

    }

    public void setOkOptions(OkSocketOptions options) {
        mOkOptions = options;
        if (mCurrentThreadMode == null) {
            mCurrentThreadMode = mOkOptions.getIOThreadMode();
        }

        assertTheThreadModeNotChanged();
        assertHeaderProtocolNotEmpty();
        mWriter.setOption(mOkOptions);
        mReader.setOption(mOkOptions);
    }

    public void send(ISendable sendable) {
        mWriter.offer(sendable);
        if(!(sendable instanceof IPulseSendable)) Logger.i("writer offer %s successful\n",sendable.getClass().getSimpleName());
        if(sendable instanceof JsonReq) Logger.i("json type: %s, content: %s",((JsonReq) sendable).getJsonType(),
                new String(((JsonReq) sendable).getContent()));
    }

    public void close() {
        close(new ManuallyDisconnectException());
    }

    public void close(Exception e) {
        shutdownAllThread(e);
        mCurrentThreadMode = null;
    }

    private void assertHeaderProtocolNotEmpty() {
        IReaderProtocol protocol = mOkOptions.getReaderProtocol();
        if (protocol == null) {
            throw new IllegalArgumentException("The reader protocol can not be Null.");
        } else if (protocol.getHeaderLength() == 0) {
            throw new IllegalArgumentException("The header length can not be zero.");
        }
    }

    private void assertTheThreadModeNotChanged() {
        if (mOkOptions.getIOThreadMode() != mCurrentThreadMode) {
            throw new IllegalArgumentException("can't hot change iothread mode from " + mCurrentThreadMode + " to " + mOkOptions.getIOThreadMode() + " in blocking io manager");
        }
    }
}

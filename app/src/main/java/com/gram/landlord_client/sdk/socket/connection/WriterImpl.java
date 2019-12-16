package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.WriteException;
import com.gram.landlord_client.sdk.socket.interfaces.IIOCoreOptions;
import com.gram.landlord_client.sdk.socket.interfaces.IOAction;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;
import com.gram.landlord_client.sdk.socket.interfaces.IWriter;
import com.gram.landlord_client.sdk.socket.util.BytesUtils;
import com.orhanobut.logger.Logger;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.concurrent.LinkedBlockingQueue;

public class WriterImpl implements IWriter<IIOCoreOptions> {

    private volatile IIOCoreOptions mOkOptions;

    private IStateSender mStateSender;

    private OutputStream mOutputStream;

    private LinkedBlockingQueue<ISendable> mQueue = new LinkedBlockingQueue<>();

    @Override
    public void initialize(OutputStream outputStream, IStateSender stateSender) {
        mStateSender = stateSender;
        mOutputStream = outputStream;
    }

    @Override
    public boolean write() throws RuntimeException {
        ISendable sendable = null;
        try {
            sendable = mQueue.take();
        } catch (InterruptedException e) {
            //ignore;
        }

        if (sendable != null) {
            try {
                byte[] sendBytes = sendable.parse();
                int packageSize = mOkOptions.getWritePackageBytes();
                int remainingCount = sendBytes.length;
                ByteBuffer writeBuf = ByteBuffer.allocate(packageSize);
                writeBuf.order(mOkOptions.getWriteByteOrder());
                int index = 0;
                while (remainingCount > 0) {
                    int realWriteLength = Math.min(packageSize, remainingCount);
                    writeBuf.clear();
                    writeBuf.rewind();
                    writeBuf.put(sendBytes, index, realWriteLength);
                    writeBuf.flip();
                    byte[] writeArr = new byte[realWriteLength];
                    writeBuf.get(writeArr);
                    mOutputStream.write(writeArr);
                    mOutputStream.flush();
                    byte[] forLogBytes = Arrays.copyOfRange(sendBytes, index, index + realWriteLength);
                    Logger.d("write bytes: " + BytesUtils.toHexStringForLog(forLogBytes));
                    Logger.d("bytes write length:" + realWriteLength);
                    index += realWriteLength;
                    remainingCount -= realWriteLength;
                }
                if (sendable instanceof IPulseSendable) {
                    mStateSender.sendBroadcast(IOAction.ACTION_PULSE_REQUEST, sendable);
                } else {
                    mStateSender.sendBroadcast(IOAction.ACTION_WRITE_COMPLETE, sendable);
                }
            } catch (Exception e) {
                throw new WriteException(e);
            }
            return true;
        }
        return false;
    }

    @Override
    public void setOption(IIOCoreOptions option) {
        mOkOptions = option;
    }

    @Override
    public void offer(ISendable sendable) {
        mQueue.offer(sendable);
    }

    @Override
    public void close() {
        if (mOutputStream != null) {
            try {
                mOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

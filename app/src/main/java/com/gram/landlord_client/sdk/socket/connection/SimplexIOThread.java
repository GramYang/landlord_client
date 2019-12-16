package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IReader;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;
import com.gram.landlord_client.sdk.socket.interfaces.IWriter;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;
import com.orhanobut.logger.Logger;
import java.io.IOException;

public class SimplexIOThread extends AbsLoopThread {
    private IStateSender mStateSender;
    private IReader mReader;
    private IWriter mWriter;
    private boolean isWrite = false;

    public SimplexIOThread(IReader reader, IWriter writer, IStateSender stateSender) {
        super("client_simplex_io_thread");
        mStateSender = stateSender;
        mReader = reader;
        mWriter = writer;
    }

    protected void beforeLoop() throws IOException {
        mStateSender.sendBroadcast("action_write_thread_start");
        mStateSender.sendBroadcast("action_read_thread_start");
    }

    protected void runInLoopThread() throws IOException {
        isWrite = mWriter.write();
        if (isWrite) {
            isWrite = false;
            mReader.read();
        }

    }

    public synchronized void shutdown(Exception e) {
        mReader.close();
        mWriter.close();
        super.shutdown(e);
    }

    protected void loopFinish(Exception e) {
        e = e instanceof ManuallyDisconnectException ? null : e;
        if (e != null) {
            Logger.e("simplex error,thread is dead with exception:" + e.getMessage());
        }

        mStateSender.sendBroadcast("action_write_thread_shutdown", e);
        mStateSender.sendBroadcast("action_read_thread_shutdown", e);
    }
}

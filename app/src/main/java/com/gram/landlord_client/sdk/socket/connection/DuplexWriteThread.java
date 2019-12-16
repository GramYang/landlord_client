package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;
import com.gram.landlord_client.sdk.socket.interfaces.IWriter;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;
import com.orhanobut.logger.Logger;

import java.io.IOException;

public class DuplexWriteThread extends AbsLoopThread {
    private IStateSender mStateSender;
    private IWriter mWriter;

    public DuplexWriteThread(IWriter writer, IStateSender stateSender) {
        super("client_duplex_write_thread");
        mStateSender = stateSender;
        mWriter = writer;
    }

    protected void beforeLoop() {
        mStateSender.sendBroadcast("action_write_thread_start");
    }

    protected void runInLoopThread() throws IOException {
        mWriter.write();
    }

    public synchronized void shutdown(Exception e) {
        mWriter.close();
        super.shutdown(e);
    }

    protected void loopFinish(Exception e) {
        e = e instanceof ManuallyDisconnectException ? null : e;
        if (e != null) {
            Logger.e("duplex write error,thread is dead with exception:" + e.getMessage());
        }

        mStateSender.sendBroadcast("action_write_thread_shutdown", e);
    }
}

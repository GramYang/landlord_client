package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.exception.ManuallyDisconnectException;
import com.gram.landlord_client.sdk.socket.interfaces.IReader;
import com.gram.landlord_client.sdk.socket.interfaces.IStateSender;
import com.gram.landlord_client.sdk.socket.util.AbsLoopThread;
import com.orhanobut.logger.Logger;

import java.io.IOException;

public class DuplexReadThread extends AbsLoopThread {
    private IStateSender mStateSender;
    private IReader mReader;

    public DuplexReadThread(IReader reader, IStateSender stateSender) {
        super("client_duplex_read_thread");
        mStateSender = stateSender;
        mReader = reader;
    }

    protected void beforeLoop() {
        mStateSender.sendBroadcast("action_read_thread_start");
    }

    protected void runInLoopThread() throws IOException {
        mReader.read();
    }

    public synchronized void shutdown(Exception e) {
        mReader.close();
        super.shutdown(e);
    }

    protected void loopFinish(Exception e) {
        e = e instanceof ManuallyDisconnectException ? null : e;
        if (e != null) {
            Logger.e("duplex read error,thread is dead with exception:" + e.getMessage());
            e.printStackTrace();
        }
        mStateSender.sendBroadcast("action_read_thread_shutdown", e);
    }
}

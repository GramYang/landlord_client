package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.bean.OriginalData;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;
import com.gram.landlord_client.sdk.socket.interfaces.ISocketActionListener;

import java.util.LinkedHashSet;
import java.util.Set;

public abstract class AbsReconnectionManager implements ISocketActionListener {
    protected volatile IConnectionManager mConnectionManager;
    protected PulseManager mPulseManager;
    protected volatile boolean mDetach;
    protected volatile Set<Class<? extends Exception>> mIgnoreDisconnectExceptionList = new LinkedHashSet();

    public AbsReconnectionManager() {
    }

    public synchronized void attach(IConnectionManager manager) {
        if (mDetach) {
            detach();
        }

        mDetach = false;
        mConnectionManager = manager;
        mPulseManager = manager.getPulseManager();
        mConnectionManager.registerReceiver(this);
    }

    public synchronized void detach() {
        mDetach = true;
        if (mConnectionManager != null) {
            mConnectionManager.unRegisterReceiver(this);
        }

    }

    public final void addIgnoreException(Class<? extends Exception> e) {
        synchronized(mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.add(e);
        }
    }

    public final void removeIgnoreException(Exception e) {
        synchronized(mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e.getClass());
        }
    }

    public final void removeIgnoreException(Class<? extends Exception> e) {
        synchronized(mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.remove(e);
        }
    }

    public final void removeAll() {
        synchronized(mIgnoreDisconnectExceptionList) {
            mIgnoreDisconnectExceptionList.clear();
        }
    }

    public void onSocketIOThreadStart(String action) {
    }

    public void onSocketIOThreadShutdown(String action, Exception e) {
    }

    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
    }

    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
    }

    public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
    }
}

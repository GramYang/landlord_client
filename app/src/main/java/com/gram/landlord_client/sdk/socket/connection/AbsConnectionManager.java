package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IConnectionSwitchListener;
import com.gram.landlord_client.sdk.socket.interfaces.ISocketActionListener;

public abstract class AbsConnectionManager implements IConnectionManager {
    protected ConnectionInfo mRemoteConnectionInfo;
    protected ConnectionInfo mLocalConnectionInfo;
    private IConnectionSwitchListener mConnectionSwitchListener;
    protected ActionDispatcher mActionDispatcher;

    public AbsConnectionManager(ConnectionInfo info) {
        this(info, null);
    }

    public AbsConnectionManager(ConnectionInfo remoteInfo, ConnectionInfo localInfo) {
        mRemoteConnectionInfo = remoteInfo;
        mLocalConnectionInfo = localInfo;
        mActionDispatcher = new ActionDispatcher(remoteInfo, this);
    }

    public IConnectionManager registerReceiver(ISocketActionListener socketResponseHandler) {
        mActionDispatcher.registerReceiver(socketResponseHandler);
        return this;
    }

    public IConnectionManager unRegisterReceiver(ISocketActionListener socketResponseHandler) {
        mActionDispatcher.unRegisterReceiver(socketResponseHandler);
        return this;
    }

    protected void sendBroadcast(String action, Object object) {
        mActionDispatcher.sendBroadcast(action, object);
    }

    protected void sendBroadcast(String action) {
        mActionDispatcher.sendBroadcast(action);
    }

    public ConnectionInfo getRemoteConnectionInfo() {
        return mRemoteConnectionInfo != null ? mRemoteConnectionInfo.clone() : null;
    }

    public ConnectionInfo getLocalConnectionInfo() {
        return mLocalConnectionInfo != null ? mLocalConnectionInfo : null;
    }

    public synchronized void switchConnectionInfo(ConnectionInfo info) {
        if (info != null) {
            ConnectionInfo tempOldInfo = mRemoteConnectionInfo;
            mRemoteConnectionInfo = info.clone();
            if (mActionDispatcher != null) {
                mActionDispatcher.setConnectionInfo(mRemoteConnectionInfo);
            }

            if (mConnectionSwitchListener != null) {
                mConnectionSwitchListener.onSwitchConnectionInfo(this, tempOldInfo, mRemoteConnectionInfo);
            }
        }

    }

    protected void setOnConnectionSwitchListener(IConnectionSwitchListener listener) {
        mConnectionSwitchListener = listener;
    }
}

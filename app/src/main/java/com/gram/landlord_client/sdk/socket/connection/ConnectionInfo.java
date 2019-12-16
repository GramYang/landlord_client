package com.gram.landlord_client.sdk.socket.connection;

import java.io.Serializable;

public final class ConnectionInfo implements Serializable, Cloneable {
    private String mIp;
    private int mPort;
    private ConnectionInfo mBackupInfo;

    public ConnectionInfo(String ip, int port) {
        mIp = ip;
        mPort = port;
    }

    public String getIp() {
        return mIp;
    }

    public int getPort() {
        return mPort;
    }

    public ConnectionInfo getBackupInfo() {
        return mBackupInfo;
    }

    public void setBackupInfo(ConnectionInfo backupInfo) {
        mBackupInfo = backupInfo;
    }

    public ConnectionInfo clone() {
        ConnectionInfo connectionInfo = new ConnectionInfo(mIp, mPort);
        if (mBackupInfo != null) {
            connectionInfo.setBackupInfo(mBackupInfo.clone());
        }

        return connectionInfo;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof ConnectionInfo)) {
            return false;
        } else {
            ConnectionInfo connectInfo = (ConnectionInfo)o;
            return mPort != connectInfo.mPort ? false : mIp.equals(connectInfo.mIp);
        }
    }

    public int hashCode() {
        int result = mIp.hashCode();
        result = 31 * result + mPort;
        return result;
    }
}

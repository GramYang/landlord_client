package com.gram.landlord_client.sdk.socket.interfaces;


import com.gram.landlord_client.sdk.socket.connection.AbsReconnectionManager;
import com.gram.landlord_client.sdk.socket.connection.ConnectionInfo;
import com.gram.landlord_client.sdk.socket.connection.PulseManager;

public interface IConnectionManager extends IConfiguration, IConnectable, IDisConnectable, ISender, IRegister<ISocketActionListener, IConnectionManager> {
    boolean isConnect();

    boolean isDisconnecting();

    PulseManager getPulseManager();

    void setIsConnectionHolder(boolean var1);

    ConnectionInfo getRemoteConnectionInfo();

    ConnectionInfo getLocalConnectionInfo();

    void setLocalConnectionInfo(ConnectionInfo var1);

    void switchConnectionInfo(ConnectionInfo var1);

    AbsReconnectionManager getReconnectionManager();
}
package com.gram.landlord_client.sdk.socket.interfaces;


import com.gram.landlord_client.sdk.socket.connection.ConnectionInfo;

public interface IConnectionSwitchListener {
    void onSwitchConnectionInfo(IConnectionManager var1, ConnectionInfo var2, ConnectionInfo var3);
}

package com.gram.landlord_client.sdk.socket.connection;

import java.net.Socket;

public abstract class OkSocketFactory {
    public OkSocketFactory() {
    }

    public abstract Socket createSocket(ConnectionInfo var1, OkSocketOptions var2) throws Exception;
}

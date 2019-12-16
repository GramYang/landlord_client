package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.interfaces.IConnectionManager;
import com.gram.landlord_client.sdk.socket.interfaces.IRegister;
import com.gram.landlord_client.sdk.socket.interfaces.IServerActionListener;
import com.gram.landlord_client.sdk.socket.interfaces.IServerManager;

public class OkSocket {
    private static ManagerHolder holder = ManagerHolder.getInstance();

    public OkSocket() {
    }

    public static IRegister<IServerActionListener, IServerManager> server(int serverPort) {
        return (IRegister)holder.getServer(serverPort);
    }

    public static IConnectionManager open(ConnectionInfo connectInfo) {
        return holder.getConnection(connectInfo);
    }

    public static IConnectionManager open(String ip, int port) {
        ConnectionInfo info = new ConnectionInfo(ip, port);
        return holder.getConnection(info);
    }
}
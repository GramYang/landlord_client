package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.bean.OriginalData;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;
import com.gram.landlord_client.sdk.socket.interfaces.ISocketActionListener;

public abstract class SocketActionAdapter implements ISocketActionListener {
    public SocketActionAdapter() {
    }

    public void onSocketIOThreadStart(String action) {
    }

    public void onSocketIOThreadShutdown(String action, Exception e) {
    }

    public void onSocketDisconnection(ConnectionInfo info, String action, Exception e) {
    }

    public void onSocketConnectionSuccess(ConnectionInfo info, String action) {
    }

    public void onSocketConnectionFailed(ConnectionInfo info, String action, Exception e) {
    }

    public void onSocketReadResponse(ConnectionInfo info, String action, OriginalData data) {
    }

    public void onSocketWriteResponse(ConnectionInfo info, String action, ISendable data) {
    }

    public void onPulseSend(ConnectionInfo info, IPulseSendable data) {
    }
}

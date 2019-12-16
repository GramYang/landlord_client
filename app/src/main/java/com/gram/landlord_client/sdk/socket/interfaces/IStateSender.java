package com.gram.landlord_client.sdk.socket.interfaces;

public interface IStateSender {

    void sendBroadcast(String action, Object object);

    void sendBroadcast(String action);
}

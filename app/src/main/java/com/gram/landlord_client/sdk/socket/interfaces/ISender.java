package com.gram.landlord_client.sdk.socket.interfaces;

public interface ISender {
    void send(ISendable sendable);
    void sendAfterConnect(ISendable sendable);
}

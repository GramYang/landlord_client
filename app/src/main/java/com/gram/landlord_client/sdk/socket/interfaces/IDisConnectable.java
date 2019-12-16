package com.gram.landlord_client.sdk.socket.interfaces;

public interface IDisConnectable {
    void disconnect(Exception e);

    void disconnect();
}

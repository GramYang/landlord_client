package com.gram.landlord_client.sdk.socket.interfaces;

public interface IServerManagerPrivate<E extends IIOCoreOptions> extends IServerManager<E> {
    void initServerPrivate(int serverPort);
}

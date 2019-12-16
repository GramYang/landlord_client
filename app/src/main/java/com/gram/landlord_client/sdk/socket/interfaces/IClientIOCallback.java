package com.gram.landlord_client.sdk.socket.interfaces;

import com.gram.landlord_client.sdk.socket.bean.OriginalData;

public interface IClientIOCallback {

    void onClientRead(OriginalData originalData, IClient client, IClientPool<IClient, String> clientPool);

    void onClientWrite(ISendable sendable, IClient client, IClientPool<IClient, String> clientPool);

}

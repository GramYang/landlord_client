package com.gram.landlord_client.sdk.socket.interfaces;

import java.io.Serializable;

public interface IClient extends IDisConnectable, ISender, Serializable {

    String getHostIp();

    String getHostName();

    String getUniqueTag();

    void setReaderProtocol(IReaderProtocol protocol);

    void addIOCallback(IClientIOCallback clientIOCallback);

    void removeIOCallback(IClientIOCallback clientIOCallback);

    void removeAllIOCallback();

}

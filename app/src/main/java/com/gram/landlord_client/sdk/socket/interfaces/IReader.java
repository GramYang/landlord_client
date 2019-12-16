package com.gram.landlord_client.sdk.socket.interfaces;

import java.io.InputStream;

public interface IReader<T extends IIOCoreOptions> {

    void initialize(InputStream inputStream, IStateSender stateSender);

    void read() throws RuntimeException;

    void setOption(T option);

    void close();
}

package com.gram.landlord_client.sdk.socket.interfaces;

import java.nio.ByteOrder;

public interface IIOCoreOptions {

    ByteOrder getReadByteOrder();

    int getMaxReadDataMB();

    IReaderProtocol getReaderProtocol();

    ByteOrder getWriteByteOrder();

    int getReadPackageBytes();

    int getWritePackageBytes();

    boolean isDebug();

}
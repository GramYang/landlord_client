package com.gram.landlord_client.sdk.socket.interfaces;

import java.nio.ByteOrder;

/**
 * 配合cellnet的ltv包格式
 */
public interface IReaderProtocol {
    int getHeaderLength();

    int getTypeLength();

    int getBodyLength(byte[] header, ByteOrder byteOrder);
}

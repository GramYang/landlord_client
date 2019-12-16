package com.gram.landlord_client.sdk.socket.connection;

import com.gram.landlord_client.sdk.socket.interfaces.IReaderProtocol;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class ReaderProtocol1 implements IReaderProtocol {
    @Override
    public int getHeaderLength() {
        return 2;
    }

    @Override
    public int getTypeLength() {
        return 2;
    }

    @Override
    public int getBodyLength(byte[] header, ByteOrder byteOrder) {
        if (header == null || header.length < getHeaderLength()) {
            return 0;
        }
        ByteBuffer bb = ByteBuffer.wrap(header);
        bb.order(byteOrder);
        return bb.getShort() - 2;
    }
}

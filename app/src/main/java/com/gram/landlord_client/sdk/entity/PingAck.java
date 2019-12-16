package com.gram.landlord_client.sdk.entity;

import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.interfaces.IPulseSendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class PingAck implements IPulseSendable {
    public PingAck() {}

    @Override
    public byte[] parse() {
        Landlord.PingACK.Builder builder = Landlord.PingACK.newBuilder();
        Landlord.PingACK ping = builder.build();
        byte[] body = ping.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4+body.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short)(body.length+2));
        bb.putShort(Constants.PING_ACK_TYPE);
        bb.put(body);
        return bb.array();
    }
}

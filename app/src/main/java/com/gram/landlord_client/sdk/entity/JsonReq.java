package com.gram.landlord_client.sdk.entity;

import com.google.protobuf.ByteString;
import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class JsonReq implements ISendable {
    private int jsonType;
    private byte[] content;

    public JsonReq(int jsonType, byte[] content) {
        this.jsonType = jsonType;
        this.content = content;
    }

    public int getJsonType() {
        return jsonType;
    }

    public byte[] getContent() {
        return content;
    }

    @Override
    public byte[] parse() {
        Landlord.JsonREQ.Builder builder = Landlord.JsonREQ.newBuilder();
        builder.setJsonType(jsonType);
        builder.setContent(ByteString.copyFrom(content));
        Landlord.JsonREQ req = builder.build();
        byte[] body = req.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4+body.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short)(body.length+2));
        bb.putShort(Constants.JSON_REQ_TYPE);
        bb.put(body);
        return bb.array();
    }
}

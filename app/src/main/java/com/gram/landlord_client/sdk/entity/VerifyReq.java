package com.gram.landlord_client.sdk.entity;

import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class VerifyReq implements ISendable {
    private String gameToken;
    private String gameSvcID;

    public VerifyReq(String gameToken, String gameSvcID) {
        this.gameToken = gameToken;
        this.gameSvcID = gameSvcID;
    }

    public String getGameToken() {
        return gameToken;
    }

    public String getGameSvcID() {
        return gameSvcID;
    }


    @Override
    public byte[] parse() {
        Landlord.VerifyREQ.Builder builder = Landlord.VerifyREQ.newBuilder();
        builder.setGameToken(gameToken);
        builder.setGameSvcID(gameSvcID);
        Landlord.VerifyREQ req = builder.build();
        byte[] body = req.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4+body.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short)(body.length+2));
        bb.putShort(Constants.VERIFY_REQ_TYPE);
        bb.put(body);
        return bb.array();
    }
}

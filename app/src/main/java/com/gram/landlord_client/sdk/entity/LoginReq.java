package com.gram.landlord_client.sdk.entity;

import com.google.protobuf.ByteString;
import com.gram.landlord_client.sdk.proto.Landlord;
import com.gram.landlord_client.sdk.protocol.Constants;
import com.gram.landlord_client.sdk.socket.interfaces.ISendable;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LoginReq implements ISendable {
    private String version;
    private String platform;
    private byte[] tokenReq; //用户名:密码的utf8字节数组

    public LoginReq(String version, String platform, byte[] tokenReq) {
        this.version = version;
        this.platform = platform;
        this.tokenReq = tokenReq;
    }

    public String getVersion() {
        return version;
    }

    public String getPlatform() {
        return platform;
    }

    public byte[] getTokenReq() {
        return tokenReq;
    }

    @Override
    public byte[] parse() {
        Landlord.LoginREQ.Builder builder = Landlord.LoginREQ.newBuilder();
        builder.setVersion(version);
        builder.setPlatform(platform);
        builder.setTokenReq(ByteString.copyFrom(tokenReq));
        Landlord.LoginREQ req = builder.build();
        byte[] body = req.toByteArray();
        ByteBuffer bb = ByteBuffer.allocate(4+body.length);
        bb.order(ByteOrder.LITTLE_ENDIAN);
        bb.putShort((short)(body.length+2)); //length
        bb.putShort(Constants.LOGIN_REQ_TYPE); //type
        bb.put(body); //content
        return bb.array();
    }
}

package com.gram.landlord_client.sdk.socket.bean;

public class OriginalData {
    private byte[] mHeadBytes;
    private byte[] mTypeBytes;
    private byte[] mBodyBytes;

    public OriginalData() {
    }

    public byte[] getHeadBytes() {
        return this.mHeadBytes;
    }

    public void setHeadBytes(byte[] headBytes) {
        this.mHeadBytes = headBytes;
    }

    public byte[] getTypeBytes() {
        return mTypeBytes;
    }

    public void setTypeBytes(byte[] mTypeBytes) {
        this.mTypeBytes = mTypeBytes;
    }

    public byte[] getBodyBytes() {
        return this.mBodyBytes;
    }

    public void setBodyBytes(byte[] bodyBytes) {
        this.mBodyBytes = bodyBytes;
    }
}

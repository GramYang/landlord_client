package com.gram.landlord_client.sdk.entity;

public class VerifyAck {
    private int result;

    public VerifyAck(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }
}

package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class MultipleWagerRequest implements Request {
    @SerializedName("Agreed")
    private int agreed;

    public MultipleWagerRequest() {
    }

    public MultipleWagerRequest(int agreed) {
        this.agreed = agreed;
    }

    public int getAgreed() {
        return agreed;
    }

    public void setAgreed(int agreed) {
        this.agreed = agreed;
    }
}

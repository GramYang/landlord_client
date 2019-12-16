package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class GiveUpLandlordResponse implements Response {
    @SerializedName("NextLandlordSeatNum")
    private int nextLandlordSeatNum;

    public GiveUpLandlordResponse() {
    }

    public GiveUpLandlordResponse(int nextLandlordSeatNum) {
        this.nextLandlordSeatNum = nextLandlordSeatNum;
    }

    public int getNextLandlordSeatNum() {
        return nextLandlordSeatNum;
    }

    public void setNextLandlordSeatNum(int nextLandlordSeatNum) {
        this.nextLandlordSeatNum = nextLandlordSeatNum;
    }
}

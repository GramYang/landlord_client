package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class LandlordMultipleWagerRequest implements Request {
    @SerializedName("LandlordSeatNum")
    private int landlordSeatNum;
    @SerializedName("MultipleNum")
    private int multipleNum;

    public LandlordMultipleWagerRequest() {
    }

    public LandlordMultipleWagerRequest(int landlordSeatNum, int multipleNum) {
        this.landlordSeatNum = landlordSeatNum;
        this.multipleNum = multipleNum;
    }

    public int getLandlordSeatNum() {
        return landlordSeatNum;
    }

    public void setLandlordSeatNum(int landlordSeatNum) {
        this.landlordSeatNum = landlordSeatNum;
    }

    public int getMultipleNum() {
        return multipleNum;
    }

    public void setMultipleNum(int multipleNum) {
        this.multipleNum = multipleNum;
    }
}

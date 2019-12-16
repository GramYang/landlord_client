package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class ExitSeatRequest implements Request {
    @SerializedName("YourSeatNum")
    private int yourSeatNum;

    public ExitSeatRequest() {
    }

    public ExitSeatRequest(int yourSeatNum) {
        this.yourSeatNum = yourSeatNum;
    }

    public int getYourSeatNum() {
        return yourSeatNum;
    }

    public void setYourSeatNum(int yourSeatNum) {
        this.yourSeatNum = yourSeatNum;
    }
}

package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class GiveUpLandlordRequest implements Request {
    @SerializedName("SeatNum")
    private int seatNum;

    public GiveUpLandlordRequest() {
    }

    public GiveUpLandlordRequest(int seatNum) {
        this.seatNum = seatNum;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }
}

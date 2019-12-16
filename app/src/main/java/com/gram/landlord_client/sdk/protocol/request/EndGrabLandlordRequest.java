package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class EndGrabLandlordRequest implements Request {
    @SerializedName("MeSeatNum")
    private int meSeatNum;

    public EndGrabLandlordRequest() {
    }

    public EndGrabLandlordRequest(int meSeatNum) {
        this.meSeatNum = meSeatNum;
    }

    public int getMeSeatNum() {
        return meSeatNum;
    }

    public void setMeSeatNum(int meSeatNum) {
        this.meSeatNum = meSeatNum;
    }
}

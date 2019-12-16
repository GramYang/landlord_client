package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EndGrabLandlordResponse implements Response {
    @SerializedName("FinalLandlordSeatNum")
    private int finalLandlordSeatNum; //地主的座位号（最终确认）
    @SerializedName("ThreeCards")
    private ArrayList<Integer> threeCards;

    public EndGrabLandlordResponse() {
    }

    public EndGrabLandlordResponse(int finalLandlordSeatNum, ArrayList<Integer> threeCards) {
        this.finalLandlordSeatNum = finalLandlordSeatNum;
        this.threeCards = threeCards;
    }

    public int getFinalLandlordSeatNum() {
        return finalLandlordSeatNum;
    }

    public void setFinalLandlordSeatNum(int finalLandlordSeatNum) {
        this.finalLandlordSeatNum = finalLandlordSeatNum;
    }

    public ArrayList<Integer> getThreeCards() {
        return threeCards;
    }

    public void setThreeCards(ArrayList<Integer> threeCards) {
        this.threeCards = threeCards;
    }
}

package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CardsOutRequest implements Request {
    @SerializedName("IsPass")
    private boolean isPass;
    @SerializedName("FromSeatNum")
    private int fromSeatNum;
    @SerializedName("ToSeatNum")
    private int toSeatNum;
    @SerializedName("CardsOut")
    private List<Integer> cardsOut;

    public CardsOutRequest() {
    }

    public CardsOutRequest(boolean isPass, int fromSeatNum, int toSeatNum, List<Integer> cardsOut) {
        this.isPass = isPass;
        this.fromSeatNum = fromSeatNum;
        this.toSeatNum = toSeatNum;
        this.cardsOut = cardsOut;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public int getFromSeatNum() {
        return fromSeatNum;
    }

    public void setFromSeatNum(int fromSeatNum) {
        this.fromSeatNum = fromSeatNum;
    }

    public int getToSeatNum() {
        return toSeatNum;
    }

    public void setToSeatNum(int toSeatNum) {
        this.toSeatNum = toSeatNum;
    }

    public List<Integer> getCardsOut() {
        return cardsOut;
    }

    public void setCardsOut(List<Integer> cardsOut) {
        this.cardsOut = cardsOut;
    }
}

package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class EndGameResponse implements Response {
    @SerializedName("WinnerSeatNum")
    private int winnerSeatNum;

    public EndGameResponse() {
    }

    public EndGameResponse(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }

    public int getWinnerSeatNum() {
        return winnerSeatNum;
    }

    public void setWinnerSeatNum(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }
}

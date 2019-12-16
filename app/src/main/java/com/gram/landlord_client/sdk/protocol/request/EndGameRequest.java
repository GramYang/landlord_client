package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class EndGameRequest implements Request {
    @SerializedName("WinnerSeatNum")
    private int winnerSeatNum;

    public EndGameRequest() {
    }

    public EndGameRequest(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }

    public int getWinnerSeatNum() {
        return winnerSeatNum;
    }

    public void setWinnerSeatNum(int winnerSeatNum) {
        this.winnerSeatNum = winnerSeatNum;
    }
}

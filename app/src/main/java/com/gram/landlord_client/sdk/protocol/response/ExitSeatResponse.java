package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class ExitSeatResponse implements Response {
    @SerializedName("UserName")
    private String userName;//用户名
    @SerializedName("SeatNum")
    private int seatNum; //座位号
    @SerializedName("TablePlayers")
    private HashMap<Integer, String> tablePlayers;

    public ExitSeatResponse() {
    }

    public ExitSeatResponse(String userName, int seatNum, HashMap<Integer, String> tablePlayers) {
        this.userName = userName;
        this.seatNum = seatNum;
        this.tablePlayers = tablePlayers;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getSeatNum() {
        return seatNum;
    }

    public void setSeatNum(int seatNum) {
        this.seatNum = seatNum;
    }

    public HashMap<Integer, String> getTablePlayers() {
        return tablePlayers;
    }

    public void setTablePlayers(HashMap<Integer, String> tablePlayers) {
        this.tablePlayers = tablePlayers;
    }
}

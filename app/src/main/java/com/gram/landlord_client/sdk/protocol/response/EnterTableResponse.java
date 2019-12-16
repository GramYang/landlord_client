package com.gram.landlord_client.sdk.protocol.response;



import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

public class EnterTableResponse implements Response {
    @SerializedName("IsSuccess")
    private boolean isSuccess;
    @SerializedName("TablePlayers")
    private HashMap<Integer, String> tablePlayers;

    public EnterTableResponse() {
    }

    public EnterTableResponse(boolean isSuccess, HashMap<Integer, String> tablePlayers) {
        this.isSuccess = isSuccess;
        this.tablePlayers = tablePlayers;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public HashMap<Integer, String> getTablePlayers() {
        return tablePlayers;
    }

    public void setTablePlayers(HashMap<Integer, String> tablePlayers) {
        this.tablePlayers = tablePlayers;
    }
}

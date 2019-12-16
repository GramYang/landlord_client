package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class GameResultResponse implements Response {
    @SerializedName("Status")
    private int status;

    public GameResultResponse() {
    }

    public GameResultResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}

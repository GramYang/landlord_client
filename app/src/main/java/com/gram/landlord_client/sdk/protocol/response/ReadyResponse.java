package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class ReadyResponse implements Response {
    @SerializedName("Ready")
    private boolean ready;

    public ReadyResponse() {
    }

    public ReadyResponse(boolean isReady) {
        this.ready = isReady;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}

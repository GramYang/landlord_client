package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class ReadyRequest implements Request {
    @SerializedName("IsReady")
    private boolean ready;

    public ReadyRequest() {
    }

    public ReadyRequest(boolean isReady) {
        this.ready = isReady;
    }

    public boolean isReady() {
        return ready;
    }

    public void setReady(boolean ready) {
        this.ready = ready;
    }
}

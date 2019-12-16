package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class CancelReadyResponse implements Response {
    @SerializedName("IsCancelReady")
    private boolean isCancelReady;

    public CancelReadyResponse() {
    }

    public CancelReadyResponse(boolean isCancelReady) {
        this.isCancelReady = isCancelReady;
    }

    public boolean isCancelReady() {
        return isCancelReady;
    }

    public void setCancelReady(boolean cancelReady) {
        isCancelReady = cancelReady;
    }
}

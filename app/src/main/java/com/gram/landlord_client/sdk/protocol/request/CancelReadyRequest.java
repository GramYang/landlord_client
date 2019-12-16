package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class CancelReadyRequest implements Request {
    @SerializedName("IsCancelReady")
    private boolean isCancelReady;

    public CancelReadyRequest() {
    }

    public CancelReadyRequest(boolean isCancelReady) {
        this.isCancelReady = isCancelReady;
    }

    public boolean isCancelReady() {
        return isCancelReady;
    }
}

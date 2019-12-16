package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class ExitHallRequest implements Request {
    @SerializedName("UserName")
    private String userName;

    public ExitHallRequest() {
    }

    public ExitHallRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

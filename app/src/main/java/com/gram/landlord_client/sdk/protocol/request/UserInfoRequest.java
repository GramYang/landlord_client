package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class UserInfoRequest implements Request {
    @SerializedName("UserName")
    private String userName;

    public UserInfoRequest() {
    }

    public UserInfoRequest(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}

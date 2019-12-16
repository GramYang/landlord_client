package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

public class UserInfoResponse implements Response {
    @SerializedName("Name")
    private String userName;
    @SerializedName("Avatar")
    private String avatar;
    @SerializedName("Win")
    private String win;
    @SerializedName("Lose")
    private String lose;
    @SerializedName("Money")
    private String money;

    public UserInfoResponse() {
    }

    public UserInfoResponse(String userName, String avatar, String win, String lose, String money) {
        this.userName = userName;
        this.avatar = avatar;
        this.win = win;
        this.lose = lose;
        this.money = money;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}

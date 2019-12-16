package com.gram.landlord_client.sdk.protocol.request;

import com.google.gson.annotations.SerializedName;

public class EnterTableRequest implements Request {
    @SerializedName("UserName")
    private String userName;
    @SerializedName("TableNum")
    private int tableNum;

    public EnterTableRequest() {
    }

    public EnterTableRequest(String userName, int tableNum) {
        this.userName = userName;
        this.tableNum = tableNum;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }
}

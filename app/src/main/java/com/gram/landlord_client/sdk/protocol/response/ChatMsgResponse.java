package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;

/**
 * 群发消息
 */
public class ChatMsgResponse implements Response {
    @SerializedName("ChatFlag")
    private int chatFlag;//1游戏大厅群聊 2斗地主房间群聊
    @SerializedName("UserName")
    private String userName;
    @SerializedName("Msg")
    private String msg;
    @SerializedName("TableNum")
    private int tableNum;

    public ChatMsgResponse() {
    }

    public ChatMsgResponse(int chatFlag, String userName, String msg, int tableNum) {
        this.chatFlag = chatFlag;
        this.userName = userName;
        this.msg = msg;
        this.tableNum = tableNum;
    }

    public int getChatFlag() {
        return chatFlag;
    }

    public void setChatFlag(int chatFlag) {
        this.chatFlag = chatFlag;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }
}

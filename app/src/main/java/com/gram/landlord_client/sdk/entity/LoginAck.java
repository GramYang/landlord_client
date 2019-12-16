package com.gram.landlord_client.sdk.entity;

public class LoginAck {
    private String serverInfo;//ip:port
    private boolean isSuccessFul;//登录是否成功
    private int resultCode; //登陆反馈状态码
    private String gameSvcID; //游戏服务ID

    public LoginAck(String serverInfo, boolean isSuccessFul, int resultCode, String gameSvcID) {
        this.serverInfo = serverInfo;
        this.isSuccessFul = isSuccessFul;
        this.resultCode = resultCode;
        this.gameSvcID = gameSvcID;
    }

    public String getServerInfo() {
        return serverInfo;
    }

    public boolean isSuccessFul() {
        return isSuccessFul;
    }

    public int getResultCode() {
        return resultCode;
    }

    public String getGameSvcID() {
        return gameSvcID;
    }
}

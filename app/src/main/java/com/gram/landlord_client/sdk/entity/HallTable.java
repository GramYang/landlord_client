package com.gram.landlord_client.sdk.entity;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 游戏大厅内的牌桌信息
 */
public class HallTable {
    @SerializedName("TableNum")
    private int tableNum; //牌桌号
    @SerializedName("UserNames")
    private ArrayList<String> userNames; //用户名（最多3个）
    @SerializedName("IsPlay")
    private boolean isPlay; //是否在游戏中
    @SerializedName("IsFull")
    private boolean isFull; //是否满员

    public HallTable() {
    }

    public HallTable(int tableNum, ArrayList<String> userNames, boolean isPlay, boolean isFull) {
        this.tableNum = tableNum;
        this.userNames = userNames;
        this.isPlay = isPlay;
        this.isFull = isFull;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public ArrayList<String> getUserNames() {
        return userNames;
    }

    public void setUserNames(ArrayList<String> userNames) {
        this.userNames = userNames;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
    }

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }
}

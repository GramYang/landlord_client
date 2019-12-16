package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;
import com.gram.landlord_client.sdk.entity.HallTable;
import java.util.ArrayList;

/**
 * 初始化大厅座位信息
 */
public class InitHallResponse implements Response {
    @SerializedName("HallTables")
    private ArrayList<HallTable> hallTables;

    public InitHallResponse() {
        this.hallTables = new ArrayList<>();
    }

    public ArrayList<HallTable> getTableList() {
        return hallTables;
    }

    public void setTableList(ArrayList<HallTable> hallTables) {
        this.hallTables = hallTables;
    }
}

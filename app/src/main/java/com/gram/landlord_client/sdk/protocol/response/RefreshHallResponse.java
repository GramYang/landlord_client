package com.gram.landlord_client.sdk.protocol.response;

import com.google.gson.annotations.SerializedName;
import com.gram.landlord_client.sdk.entity.HallTable;
import java.util.ArrayList;

public class RefreshHallResponse implements Response {
    @SerializedName("HallTables")
    private ArrayList<HallTable> hallTables;

    public RefreshHallResponse() {
    }

    public RefreshHallResponse(ArrayList<HallTable> hallTables) {
        this.hallTables = hallTables;
    }

    public ArrayList<HallTable> getHallTables() {
        return hallTables;
    }

    public void setHallTables(ArrayList<HallTable> hallTables) {
        this.hallTables = hallTables;
    }
}

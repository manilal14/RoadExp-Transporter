package com.road.roadexp_transporter.groupingVehicles;

public class StatusItem extends MyListItem {

    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public int getType() {
        return TYPE_STATUS;
    }
}

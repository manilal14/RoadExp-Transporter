package com.road.roadexp_transporter.groupingVehicles;

import com.road.roadexp_transporter.Review.Vehicle;

public class GeneralItems extends MyListItem {

    private Vehicle vehicle;

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}

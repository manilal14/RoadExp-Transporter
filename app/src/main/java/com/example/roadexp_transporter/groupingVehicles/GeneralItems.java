package com.example.roadexp_transporter.groupingVehicles;

import com.example.roadexp_transporter.HomeFragments.Vehicle;

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

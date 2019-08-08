package com.road.roadexp_transporter.Reports.TravelReport;

public class TravelHistoryModel {

    private int loadId;
    private String date;
    private String loadType;
    private String amount;
    private String vehicleType;
    private String pickupLoc;
    private String pickupCity;

    private String lastPoint;
    private String lastCity;

    private String weight;
    private String intermediateLoc;
    private String driveName;

    public TravelHistoryModel(int loadId, String date, String loadType, String amount, String vehicleType, String pickupLoc, String pickupCity,
                              String lastPoint, String lastCity, String weight, String intermediateLoc, String driveName) {
        this.loadId = loadId;
        this.date = date;
        this.loadType = loadType;
        this.amount = amount;
        this.vehicleType = vehicleType;
        this.pickupLoc = pickupLoc;
        this.pickupCity = pickupCity;
        this.lastPoint = lastPoint;
        this.lastCity = lastCity;
        this.weight = weight;
        this.intermediateLoc = intermediateLoc;
        this.driveName = driveName;
    }

    public int getLoadId() {
        return loadId;
    }

    public String getDate() {
        return date;
    }

    public String getLoadType() {
        return loadType;
    }

    public String getAmount() {
        return amount;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPickupLoc() {
        return pickupLoc;
    }

    public String getPickupCity() {
        return pickupCity;
    }

    public String getLastPoint() {
        return lastPoint;
    }

    public String getLastCity() {
        return lastCity;
    }

    public String getWeight() {
        return weight;
    }

    public String getIntermediateLoc() {
        return intermediateLoc;
    }

    public String getDriveName() {
        return driveName;
    }
}

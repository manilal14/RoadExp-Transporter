package com.road.roadexp_transporter.Reports.MissedLoad;

public class MissedLoad {
    private String id;
    private String date;
    private String driverId;
    private String interMediateLoc;
    private String pickup;
    private String lastPont;
    private String weight;
    private String driverName;

    public MissedLoad(String id, String date, String driverId, String interMediateLoc,
                      String pickup, String lastPont, String weight, String driverName) {
        this.id = id;
        this.date = date;
        this.driverId = driverId;
        this.interMediateLoc = interMediateLoc;
        this.pickup = pickup;
        this.lastPont = lastPont;
        this.weight = weight;
        this.driverName = driverName;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getInterMediateLoc() {
        return interMediateLoc;
    }

    public String getPickup() {
        return pickup;
    }

    public String getLastPont() {
        return lastPont;
    }

    public String getWeight() {
        return weight;
    }

    public String getDriverName() {
        return driverName;
    }
}

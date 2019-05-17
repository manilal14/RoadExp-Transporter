package com.example.roadexp_transporter.NotificationPackage;

public class Notification {


    private int loadId;
    private String vehicleType;

    private String pickupPoint;
    private String startPhone;

    private String endPoint;
    private String endPhone;

    private String city;
    private String state;

    private String dimention;

    private String intermediatePoints;
    private String intermediateMobile;

    private String orderedBy;

    private String loadWeight;
    private String loadType;

    private String startOn;
    private String expireOn;

    private String amount;
    private String capacity;


    public Notification(int loadId, String vehicleType, String pickupPoint, String startPhone, String endPoint, String endPhone,
                        String city, String state, String dimention, String intermediatePoints, String intermediateMobile, String orderedBy,
                        String loadWeight, String loadType, String startOn, String expireOn, String amount, String capacity) {
        this.loadId = loadId;
        this.vehicleType = vehicleType;
        this.pickupPoint = pickupPoint;
        this.startPhone = startPhone;
        this.endPoint = endPoint;
        this.endPhone = endPhone;
        this.city = city;
        this.state = state;
        this.dimention = dimention;
        this.intermediatePoints = intermediatePoints;
        this.intermediateMobile = intermediateMobile;
        this.orderedBy = orderedBy;
        this.loadWeight = loadWeight;
        this.loadType = loadType;
        this.startOn = startOn;
        this.expireOn = expireOn;
        this.amount = amount;
        this.capacity = capacity;
    }

    public int getLoadId() {
        return loadId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getPickupPoint() {
        return pickupPoint;
    }

    public String getStartPhone() {
        return startPhone;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getEndPhone() {
        return endPhone;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getDimention() {
        return dimention;
    }

    public String getIntermediatePoints() {
        return intermediatePoints;
    }

    public String getIntermediateMobile() {
        return intermediateMobile;
    }

    public String getOrderedBy() {
        return orderedBy;
    }

    public String getLoadWeight() {
        return loadWeight;
    }

    public String getLoadType() {
        return loadType;
    }

    public String getStartOn() {
        return startOn;
    }

    public String getExpireOn() {
        return expireOn;
    }

    public String getAmount() {
        return amount;
    }

    public String getCapacity() {
        return capacity;
    }
}

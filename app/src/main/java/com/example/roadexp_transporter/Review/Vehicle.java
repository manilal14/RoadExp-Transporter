package com.example.roadexp_transporter.Review;

import java.io.Serializable;

public class Vehicle implements Serializable {

    //VehicleInfo
    private int vehicleId;
    private String plateNumber;
    private String picRcFront;
    private String picRcBack;
    private String picVehicle;
    private String insuranceNum;
    private String addedOn;
    private String permitType;
    private String rcExpireOn;
    private String vehicleType;
    private String dimention;
    private String capacity;
    private String mappedDriver;

    private int isVerified;
    private String driverId;
    private String tripId;

    private int status;

    public Vehicle(int vehicleId, String plateNumber, String picRcFront, String picRcBack, String picVehicle, String insuranceNum, String addedOn, String permitType, String rcExpireOn, String vehicleType, String dimention,
                   String capacity, String mappedDriver, int isVerified, String driverId, String tripId, int status) {
        this.vehicleId = vehicleId;
        this.plateNumber = plateNumber;
        this.picRcFront = picRcFront;
        this.picRcBack = picRcBack;
        this.picVehicle = picVehicle;
        this.insuranceNum = insuranceNum;
        this.addedOn = addedOn;
        this.permitType = permitType;
        this.rcExpireOn = rcExpireOn;
        this.vehicleType = vehicleType;
        this.dimention = dimention;
        this.capacity = capacity;
        this.mappedDriver = mappedDriver;
        this.isVerified = isVerified;
        this.driverId = driverId;
        this.tripId = tripId;
        this.status = status;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getPicRcFront() {
        return picRcFront;
    }

    public String getPicRcBack() {
        return picRcBack;
    }

    public String getPicVehicle() {
        return picVehicle;
    }

    public String getInsuranceNum() {
        return insuranceNum;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public String getPermitType() {
        return permitType;
    }

    public String getRcExpireOn() {
        return rcExpireOn;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getDimention() {
        return dimention;
    }

    public String getCapacity() {
        return capacity;
    }

    public String getMappedDriver() {
        return mappedDriver;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public String getDriverId() {
        return driverId;
    }

    public String getTripId() {
        return tripId;
    }

    public int getStatus() {
        return status;
    }
}

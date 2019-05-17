package com.example.roadexp_transporter.Review;

import java.io.Serializable;

public class Vehicle implements Serializable {

    //VehicleInfo
    private int vehicleId;
    private String vehicleType;
    private String insuranceNum;

    //private String vehicleInvoice;

    private String plateNumber;
    private String mapedDriverName; // not found

    private String picRcFront;
    private String picRcBack;
    private String picVehicle;

    private int status;

    public Vehicle(int vehicleId, String vehicleType, String insuranceNum, String plateNumber,
                   String mapedDriverName, String picRcFront, String picRcBack, String picVehicle, int status) {
        this.vehicleId = vehicleId;
        this.vehicleType = vehicleType;
        this.insuranceNum = insuranceNum;
        this.plateNumber = plateNumber;
        this.mapedDriverName = mapedDriverName;
        this.picRcFront = picRcFront;
        this.picRcBack = picRcBack;
        this.picVehicle = picVehicle;
        this.status = status;
    }

    public int getVehicleId() {
        return vehicleId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public String getInsuranceNum() {
        return insuranceNum;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public String getMapedDriverName() {
        return mapedDriverName;
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

    public int getStatus() {
        return status;
    }
}

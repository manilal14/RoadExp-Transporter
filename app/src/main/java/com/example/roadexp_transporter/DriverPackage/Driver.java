package com.example.roadexp_transporter.DriverPackage;

import java.io.Serializable;

public class Driver implements Serializable {

    private String id;
    private String name;

    private String mobile;
    private String city;
    private String state;

    private int ageInYear;
    private String joingDate;
    private String noOfSuccesTrip;

    private String vehicleName;
    private String vehicleNumber;

    private String profilePic;
    private String dlPicFront;
    private String dlPicBack;

    private int t_av;
    private int d_av;

    private int status;

    private int isVerified;
    private int vehicleId;

    public Driver(String id, String name, String mobile, String city, String state, int ageInYear, String joingDate, String noOfSuccesTrip, String vehicleName, String vehicleNumber, String profilePic,
                  String dlPicFront, String dlPicBack, int t_av, int d_av, int status, int isVerified, int vehicleId) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.city = city;
        this.state = state;
        this.ageInYear = ageInYear;
        this.joingDate = joingDate;
        this.noOfSuccesTrip = noOfSuccesTrip;
        this.vehicleName = vehicleName;
        this.vehicleNumber = vehicleNumber;
        this.profilePic = profilePic;
        this.dlPicFront = dlPicFront;
        this.dlPicBack = dlPicBack;
        this.t_av = t_av;
        this.d_av = d_av;
        this.status = status;
        this.isVerified = isVerified;
        this.vehicleId = vehicleId;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public int getAgeInYear() {
        return ageInYear;
    }

    public String getJoingDate() {
        return joingDate;
    }

    public String getNoOfSuccesTrip() {
        return noOfSuccesTrip;
    }

    public String getVehicleName() {
        return vehicleName;
    }

    public String getVehicleNumber() {
        return vehicleNumber;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public String getDlPicFront() {
        return dlPicFront;
    }

    public String getDlPicBack() {
        return dlPicBack;
    }

    public int getT_av() {
        return t_av;
    }

    public int getD_av() {
        return d_av;
    }

    public int getStatus() {
        return status;
    }

    public int getIsVerified() {
        return isVerified;
    }

    public int getVehicleId() {
        return vehicleId;
    }
}

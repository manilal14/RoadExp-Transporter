package com.example.roadexp_transporter.DriverPackage;

public class Driver {

    private int id;
    private String name;


    private String mobile;
    private String gender;
    private String age;

    private String joingDate;
    private int noOfSuccessfullTrips;

    private int status;
    private String mappedTo;

    private String imageProfilePic;
    private String imageDrivingLicense;


    // Temp Constructor
    public Driver(int id, String name, int status) {
        this.id = id;
        this.name = name;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getStatus() {
        return status;
    }
}

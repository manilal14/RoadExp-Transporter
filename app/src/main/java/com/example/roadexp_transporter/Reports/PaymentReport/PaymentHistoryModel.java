package com.example.roadexp_transporter.Reports.PaymentReport;

public class PaymentHistoryModel {

    private int tripId;
    private String startDate;
    private int stops;
    private String startPoint;
    private String endPoint;
    private String totalFare;
    private String driverCut;
    private String remaining;
    private String driveName;


    public PaymentHistoryModel(int tripId, String startDate, int stops, String startPoint,
                               String endPoint, String totalFare, String driverCut, String remaining, String driverName) {
        this.tripId = tripId;
        this.startDate = startDate;
        this.stops = stops;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.totalFare = totalFare;
        this.driverCut = driverCut;
        this.remaining = remaining;
        this.driveName = driverName;
    }

    public int getTripId() {
        return tripId;
    }

    public String getStartDate() {
        return startDate;
    }

    public int getStops() {
        return stops;
    }

    public String getStartPoint() {
        return startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getTotalFare() {
        return totalFare;
    }

    public String getDriverCut() {
        return driverCut;
    }

    public String getRemaining() {
        return remaining;
    }

    public String getDriveName() {
        return driveName;
    }
}

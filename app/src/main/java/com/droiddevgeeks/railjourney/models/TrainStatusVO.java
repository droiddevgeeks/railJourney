package com.droiddevgeeks.railjourney.models;

/**
 * Created by kunal on 12-10-2016.
 */
public class TrainStatusVO
{
    private String fullname;
    private String departureTime;
    private int distance;
    private String arrivalTime;

    public TrainStatusVO()
    {

    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public TrainStatusVO(String fullname, String departureTime, int distance, String arrivalTime)
    {
        this.fullname      = fullname;

        this.departureTime = departureTime;
        this.distance      = distance;
        this.arrivalTime   = arrivalTime;
    }

    public int getDistance() {
        return distance;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public String getFullname() {
        return fullname;
    }
}

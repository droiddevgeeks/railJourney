package com.droiddevgeeks.railjourney.models;

/**
 * Created by kishan.maurya on 20-10-2016.
 */

public class MidStationInfo
{
    private String stationName;
    private String arrivalTime;
    private String deptTime;
    private String distanceFromSource;
    private String latemin;


    private boolean arrived;

    public MidStationInfo(String stationName , String arrivalTime , String deptTime , String distanceFromSource)
    {
        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.deptTime = deptTime ;
        this.distanceFromSource = distanceFromSource;
    }

    public MidStationInfo(String stationName , String arrivalTime , String deptTime ,boolean arrived ,String latemin)
    {

        this.stationName = stationName;
        this.arrivalTime = arrivalTime;
        this.deptTime = deptTime ;
        this.latemin = latemin;
        this.arrived = arrived;
    }

    public boolean isArrived()
    {
        return arrived;
    }

    public int getLatemin()
    {
        return Integer.valueOf(latemin);
    }

    public String getStationName()
    {
        return stationName;
    }

    public String getArrivalTime()
    {
        return arrivalTime;
    }

    public String getDeptTime()
    {
        return deptTime;
    }

    public String getDistanceFromSource()
    {
        return distanceFromSource;
    }
}
package com.droiddevgeeks.railjourney.models;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchVO
{
    private String trainName;
    private String trainNumber;
    private String travelTime;
    private String trainDeparture;
    private String trainArrival;
    private String  days;
    private String classInfo;


    public SearchVO( String trainName, String trainNumber, String travelTime, String trainDeparture, String trainArrival, String daysVOs, String classVOs)
    {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.travelTime = travelTime;
        this.trainDeparture = trainDeparture;
        this.trainArrival = trainArrival;
        days = daysVOs;
        classInfo = classVOs;

    }

    public String getDays()
    {
        return days;
    }

    public String getClassInfo()
    {
        return classInfo;
    }
    public String getTrainName()
    {
        return trainName;
    }

    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getTravelTime()
    {
        return travelTime;
    }

    public String getTrainDeparture()
    {
        return trainDeparture;
    }

    public String getTrainArrival()
    {
        return trainArrival;
    }

}

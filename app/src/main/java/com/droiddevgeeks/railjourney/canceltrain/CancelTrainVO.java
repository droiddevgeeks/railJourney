package com.droiddevgeeks.railjourney.canceltrain;

/**
 * Created by Vampire on 2016-10-13.
 */

public class CancelTrainVO
{
    private String trainName,fromStation, toStation,trainTime;
    public CancelTrainVO(String trainName , String fromStation , String toStation, String trainTime)
    {
        this.trainName = trainName;
        this.fromStation = fromStation;
        this.toStation = toStation;
        this.trainTime = trainTime;
    }

    public String getTrainName()
    {
        return trainName;
    }

    public String getFromStation()
    {
        return fromStation;
    }

    public String getToStation()
    {
        return toStation;
    }

    public String getTrainTime()
    {
        return trainTime;
    }
}

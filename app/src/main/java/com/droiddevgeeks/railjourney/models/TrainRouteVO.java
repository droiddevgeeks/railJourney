package com.droiddevgeeks.railjourney.models;

import java.util.List;

/**
 * Created by kunal on 10-10-2016.
 */
public class TrainRouteVO
{

    private String trainName;
    private String trainNumber;
    private String trainRunsOn ;
    private List<MidStationInfo> list;
    public TrainRouteVO(String trainName, String trainNumber , String trainRunsOn , List<MidStationInfo> list)
    {
        this.trainName = trainName ;
        this.trainNumber = trainNumber ;
        this.trainRunsOn = trainRunsOn ;
        this.list = list ;
    }

    public String getTrainName()
    {
        return trainName;
    }

    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getTrainRunsOn()
    {
        return trainRunsOn;
    }

    public List<MidStationInfo> getList()
    {
        return list;
    }


}

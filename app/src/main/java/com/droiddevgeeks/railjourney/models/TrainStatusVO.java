package com.droiddevgeeks.railjourney.models;

import java.util.List;

/**
 * Created by kunal on 12-10-2016.
 */
public class TrainStatusVO
{
    private boolean arrived;
    private String trainNumber;
    private String currentStation;
    private String currentStatus;
    private List<MidStationInfo> midStationList;


    public TrainStatusVO(String trainNo,String currentStation, boolean arrived, String status, List<MidStationInfo> list)
    {
        this.trainNumber = trainNo;
        this.currentStation = currentStation;
        this.arrived = arrived;
        this.currentStatus = status;
        this.midStationList = list;
    }

    public boolean isArrived()
    {
        return arrived;
    }

    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getCurrentStation()
    {
        return currentStation;
    }

    public String getCurrentStatus()
    {
        return currentStatus;
    }

    public List<MidStationInfo> getMidStationList()
    {
        return midStationList;
    }
}

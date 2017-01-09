package com.droiddevgeeks.railjourney.models;

import java.util.List;

/**
 * Created by Vampire on 2017-01-04.
 */

public class SeatAvailableVO
{
    private String trainName;
    private String trainNumber;
    private String source;
    private String destination;
    private String travelClass;
    private String quota;
    private List<SeatDateVO> seatDateVOs;

    public SeatAvailableVO(String trainName , String trainNumber , String source, String destination , String travelClass, String quota, List<SeatDateVO> list)
    {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.source = source;
        this.destination = destination;
        this.travelClass = travelClass;
        this.quota = quota;
        this.seatDateVOs = list;
    }

    public List<SeatDateVO> getSeatDateVOs()
    {
        return seatDateVOs;
    }

    public String getQuota()
    {
        return quota;
    }

    public String getTravelClass()
    {
        return travelClass;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getSource()
    {
        return source;
    }

    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getTrainName()
    {
        return trainName;
    }
}

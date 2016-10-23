package com.droiddevgeeks.railjourney.models;
/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class CancelledTrainVO
{

    private String source;
    private String destination;
    private String trainName;
    private String trainNumber;
    private String trainStartTime;

    public CancelledTrainVO(String trainName , String trainNumber, String time , String source , String destination)
    {
        this.trainName = trainName;
        this.trainNumber = trainNumber ;
        this.trainStartTime = time;
        this.source = source;
        this.destination = destination;

    }


    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getSource()
    {
        return source;
    }

    public String getDestination()
    {
        return destination;
    }

    public String getTrainName()
    {
        return trainName;
    }

    public String getTrainStartTime()
    {
        return trainStartTime;
    }
}

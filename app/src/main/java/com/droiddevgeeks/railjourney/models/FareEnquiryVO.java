package com.droiddevgeeks.railjourney.models;

import java.util.List;

/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class FareEnquiryVO
{
    private String trainName;
    private String trainNumber;
    private String source;
    private String destination;
    private String bookingQuota;
    private List<FareVO> fareVOList;

    public FareEnquiryVO(String trainName, String trainNumber, String source, String destination, String bookingQuota, List<FareVO> list)
    {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.source = source;
        this.destination = destination;
        this.bookingQuota = bookingQuota;
        this.fareVOList = list;
    }

    public List<FareVO> getFareVOList()
    {
        return fareVOList;
    }

    public String getBookingQuota()
    {
        return bookingQuota;
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

    public class FareVO
    {
        private String travelClassName;
        private String fare;

        public FareVO(String travelClassName, String fare)
        {
            this.travelClassName = travelClassName;
            this.fare = fare;
        }

        public String getTravelClassName()
        {
            return travelClassName;
        }

        public String getFare()
        {
            return fare;
        }
    }
}

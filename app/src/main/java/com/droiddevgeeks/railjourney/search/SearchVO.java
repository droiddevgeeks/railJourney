package com.droiddevgeeks.railjourney.search;

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

    public String getDays()
    {
        return days;
    }

    public String getClassInfo()
    {
        return classInfo;
    }

    public SearchVO( String trainName, String trainNumber, String travelTime, String trainDeparture, String trainArrival, String daysVOs, String classVOs)
    {
        this.trainName = trainName;
        this.trainNumber = trainNumber;
        this.travelTime = travelTime;
        this.trainDeparture = trainDeparture;
        this.trainArrival = trainArrival;
        days = classVOs;
        classInfo = daysVOs;

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

    public static class DaysVO
    {
        private String runs;
        private String dayCode;

        public DaysVO( String runs, String dayCode )
        {
            this.runs = runs;
            this.dayCode = dayCode;
        }

        public String getRuns()
        {
            return runs;
        }

        public String getDayCode()
        {
            return dayCode;
        }
    }
    public static class ClassVO
    {
        private String classCode;
        private String available;

        public ClassVO( String classCode, String available )
        {
            this.classCode = classCode;
            this.available = available;
        }

        public String getClassCode()
        {
            return classCode;
        }

        public String getAvailable()
        {
            return available;
        }
    }
}

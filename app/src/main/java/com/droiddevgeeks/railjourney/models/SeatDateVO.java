package com.droiddevgeeks.railjourney.models;

/**
 * Created by Vampire on 2017-01-04.
 */

public class SeatDateVO
{

    private String date;
    private String seatsStatus;

    public SeatDateVO(String date , String seatsStatus)
    {
        this.date = date;
        this.seatsStatus = seatsStatus;
    }

    public String getDate()
    {
        return date;
    }

    public String getSeatsStatus()
    {
        return seatsStatus;
    }
}

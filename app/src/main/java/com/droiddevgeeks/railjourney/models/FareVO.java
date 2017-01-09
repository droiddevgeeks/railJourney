package com.droiddevgeeks.railjourney.models;

/**
 * Created by Vampire on 2016-12-29.
 */

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

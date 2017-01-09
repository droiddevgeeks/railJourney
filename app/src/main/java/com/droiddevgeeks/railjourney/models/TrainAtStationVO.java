package com.droiddevgeeks.railjourney.models;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationVO
{
    private String scharr;
    private String delayarr;
    private String schdep;
    private String name;
    private String actdep;
    private String delaydep;
    private String actarr;
    private String number;

    public TrainAtStationVO(String scharr, String delayarr , String schdep, String name , String actdep, String delaydep, String actarr, String number)
    {
        this.scharr = scharr;
        this.delayarr = delayarr;
        this.schdep = schdep;
        this.name = name;
        this.actdep = actdep;
        this.delaydep = delaydep;
        this.actarr = actarr;
        this.number = number;
    }

    public String getScharr()
    {
        return scharr;
    }

    public String getDelayarr()
    {
        return delayarr;
    }

    public String getSchdep()
    {
        return schdep;
    }

    public String getName()
    {
        return name;
    }

    public String getActdep()
    {
        return actdep;
    }

    public String getDelaydep()
    {
        return delaydep;
    }

    public String getActarr()
    {
        return actarr;
    }

    public String getNumber()
    {
        return number;
    }
}

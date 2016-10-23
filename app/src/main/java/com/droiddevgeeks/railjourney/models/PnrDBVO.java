package com.droiddevgeeks.railjourney.models;

/**
 * Created by kunal.sale on 10/19/2016.
 */

public class PnrDBVO
{
    private String pnr;
    private String time;

    public PnrDBVO( String pnr, String time )
    {
        this.pnr = pnr;
        this.time = time;
    }

    public String getPnr()
    {
        return pnr;
    }

    public String getTime()
    {
        return time;
    }
}

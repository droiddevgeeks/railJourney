package com.droiddevgeeks.railjourney.models;

/**
 * Created by kishan.maurya on 04-10-2016.
 */
public class NavigationDrawerVO
{
    private String rowtitle;
    private int rowImage;

    public NavigationDrawerVO(int rowImage , String rowtitle)
    {
        this.rowImage = rowImage;
        this.rowtitle = rowtitle;

    }

    public int getRowImage()
    {
        return rowImage;
    }

    public String getRowtitle()
    {
        return rowtitle;
    }


}

package com.droiddevgeeks.railjourney.models;

/**
 * Created by kunal.sale on 10/4/2016.
 */
public class SectionVO
{
    private int resId;
    private String sectionName;

    public SectionVO( int resId, String sectionName )
    {

        this.resId = resId;
        this.sectionName = sectionName;
    }

    public int getResId()
    {
        return resId;
    }

    public String getSectionName()
    {
        return sectionName;
    }
}

package com.droiddevgeeks.railjourney.models;

/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class AutoCompleteVO
{

    private String code;
    private String name;

    public AutoCompleteVO(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
}

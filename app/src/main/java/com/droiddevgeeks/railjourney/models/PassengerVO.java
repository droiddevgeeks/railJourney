package com.droiddevgeeks.railjourney.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Vampire on 2016-12-25.
 */

public class PassengerVO
{

    private String passengerNumber;
    private String coachNumber;
    private String currentStatus;
    private String bookingStatus;

    public PassengerVO(JSONObject jsonObject)
    {
        try
        {
            passengerNumber = jsonObject.getString("no");
            coachNumber = jsonObject.getString("coach_position");
            currentStatus = jsonObject.getString("current_status");
            bookingStatus = jsonObject.getString("booking_status");
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

    }

    public String getPassengerNumber()
    {
        return passengerNumber;
    }

    public String getCoachNumber()
    {
        return coachNumber;
    }

    public String getCurrentStatus()
    {
        return currentStatus;
    }

    public String getBookingStatus()
    {
        return bookingStatus;
    }
}

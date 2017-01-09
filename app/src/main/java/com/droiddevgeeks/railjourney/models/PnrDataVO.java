package com.droiddevgeeks.railjourney.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2016-12-25.
 */

public class PnrDataVO
{

    private String trainSourceStation;
    private String trainDestinationStation;
    private String yourBoardingStation;
    private String yourLastStation;
    private String trainNumber;
    private String dateOfJourney;
    private List<PassengerVO> passengerVOs;

    public PnrDataVO(String trainSourceStation,String trainDestinationStation,String yourBoardingStation,String yourLastStation,String trainNumber,String dateOfJourney, JSONArray jsonArray)
    {
        passengerVOs = new ArrayList<>();
        this.trainSourceStation= trainSourceStation;
        this.trainDestinationStation= trainDestinationStation;
        this.yourBoardingStation= yourBoardingStation;
        this.yourLastStation= yourLastStation;
        this.trainNumber= trainNumber;
        this.dateOfJourney= dateOfJourney;
        for(int i = 0 ; i < jsonArray.length();i++)
        {
            try
            {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                passengerVOs.add(new PassengerVO(jsonObject));
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

    }

    public String getTrainSourceStation()
    {
        return trainSourceStation;
    }

    public String getTrainDestinationStation()
    {
        return trainDestinationStation;
    }

    public String getYourBoardingStation()
    {
        return yourBoardingStation;
    }

    public String getYourLastStation()
    {
        return yourLastStation;
    }

    public String getTrainNumber()
    {
        return trainNumber;
    }

    public String getDateOfJourney()
    {
        return dateOfJourney;
    }

    public List<PassengerVO> getPassengerVOs()
    {
        return passengerVOs;
    }
}

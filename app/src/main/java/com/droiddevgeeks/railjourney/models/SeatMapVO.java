package com.droiddevgeeks.railjourney.models;

/**
 * Created by Vampire on 2016-10-08.
 */
public class SeatMapVO
{

    private String trainType;
    private int seatArrangement;

    public SeatMapVO(String trainType , int seatLayout)
    {
        this.trainType = trainType;
        seatArrangement = seatLayout;
    }

    public String getTrainType()
    {
        return trainType;
    }

    public int getSeatArrangement()
    {
        return seatArrangement;
    }
}

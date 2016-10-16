package com.droiddevgeeks.railjourney.train_status;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droiddevgeeks.railjourney.models.TrainRouteVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;

import java.util.ArrayList;

/**
 * Created by kunal on 11-10-2016.
 */
public class TrainStatusDisplayFragment extends Fragment
{
    private ArrayList<TrainStatusVO> _trainStatusVOs;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return null;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }

    public void setTrainStatusVoList(ArrayList<TrainStatusVO> trainStatusVoList)
    {
        _trainStatusVOs = trainStatusVoList;
    }
}

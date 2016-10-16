package com.droiddevgeeks.railjourney.trainroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-15.
 */

public class TrainRouteResultFragment extends Fragment
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View trainRouteResult = inflater.inflate(R.layout.train_route_result , container, false);
        return trainRouteResult;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
    }
}

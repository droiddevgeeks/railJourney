package com.droiddevgeeks.railjourney.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.train_at_station.TrainAtStationInfo;

/**
 * Created by Vampire on 2017-01-01.
 */

public class SearchTrainInfo extends DialogFragment
{
    private TextView scharr;
    private TextView schdep;
    private TextView travelTime;
    private TextView runsOn;
    private TextView classes;

    public SearchTrainInfo()
    {

    }

    public static SearchTrainInfo newInstance(String name,String scharr,String schdep,String travelTime,String runsOn,String classes)
    {
        SearchTrainInfo frag = new SearchTrainInfo();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("scharr", scharr);
        args.putString("schdep", schdep);
        args.putString("travelTime", travelTime);
        args.putString("runsOn", runsOn);
        args.putString("classes", classes);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.search_result_info, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle(getArguments().getString("name", ""));

        scharr = (TextView)view.findViewById(R.id.scharr);
        schdep = (TextView)view.findViewById(R.id.schdep);
        travelTime = (TextView)view.findViewById(R.id.trvelTimeValue);
        runsOn = (TextView)view.findViewById(R.id.txtRunsOn);
        classes = (TextView)view.findViewById(R.id.txtClasses);

        scharr.setText(getArguments().getString("scharr", ""));
        schdep.setText(getArguments().getString("schdep", ""));
        travelTime.setText(getArguments().getString("travelTime", ""));
        runsOn.setText(getArguments().getString("runsOn", ""));
        classes.setText(getArguments().getString("classes", ""));

    }
}
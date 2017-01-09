package com.droiddevgeeks.railjourney.train_at_station;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationInfo extends DialogFragment
{
    private TextView scharr;
    private TextView delayarr;
    private TextView schdep;
    private TextView actdep;
    private TextView delaydep;
    private TextView actarr;

    public TrainAtStationInfo()
    {

    }

    public static TrainAtStationInfo newInstance(String scharr,String delayarr,String schdep,String actdep,String delaydep,String actarr)
    {
        TrainAtStationInfo frag = new TrainAtStationInfo();
        Bundle args = new Bundle();
        args.putString("scharr", scharr);
        args.putString("delayarr", delayarr);
        args.putString("schdep", schdep);
        args.putString("actdep", actdep);
        args.putString("delaydep", delaydep);
        args.putString("actarr", actarr);
        frag.setArguments(args);
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.train_at_station_info, container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        getDialog().setTitle("Train Info");
        scharr = (TextView)view.findViewById(R.id.scharr);
        delayarr = (TextView)view.findViewById(R.id.delayarr);
        schdep = (TextView)view.findViewById(R.id.schdep);
        actdep = (TextView)view.findViewById(R.id.actdep);
        delaydep = (TextView)view.findViewById(R.id.delaydep);
        actarr = (TextView)view.findViewById(R.id.actarr);

        scharr.setText(getArguments().getString("scharr", ""));
        delayarr.setText(getArguments().getString("delayarr", ""));
        schdep.setText(getArguments().getString("schdep", ""));
        actdep.setText(getArguments().getString("actdep", ""));
        delaydep.setText(getArguments().getString("delaydep", ""));
        actarr.setText(getArguments().getString("actarr", ""));

    }
}

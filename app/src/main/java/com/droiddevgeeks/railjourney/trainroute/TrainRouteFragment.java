package com.droiddevgeeks.railjourney.trainroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-15.
 */

public class TrainRouteFragment extends Fragment implements View.OnClickListener
{
    private Button _trainRoute;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View trainRoute = inflater.inflate(R.layout.train_route_layout , container, false);
        return trainRoute;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _trainRoute = (Button)view.findViewById(R.id.btnTrainRoute);
        _trainRoute.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnTrainRoute :
                getFragmentManager().beginTransaction().replace(R.id.container , new TrainRouteResultFragment()).addToBackStack(null).commit();
                break;

        }
    }
}

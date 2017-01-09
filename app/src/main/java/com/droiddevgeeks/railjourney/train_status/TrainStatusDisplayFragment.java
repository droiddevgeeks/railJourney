package com.droiddevgeeks.railjourney.train_status;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.PnrDataVO;
import com.droiddevgeeks.railjourney.models.TrainRouteVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;
import com.droiddevgeeks.railjourney.pnr.PassengerListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kunal on 11-10-2016.
 */
public class TrainStatusDisplayFragment extends Fragment
{
    private ArrayList<TrainStatusVO> _trainStatusVOs;
    private TextView trainNumber;
    private TextView currentStation;
    private TextView arrived;
    private TextView currentStatus;
    private ListView midStationList;
    private TrainStatusAdapter adapter;
    private TextView _pageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.live_train_status_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Status");
        trainNumber = (TextView)view.findViewById(R.id.txtTrainName);
        currentStation = (TextView)view.findViewById(R.id.txtCurrentStation);
        arrived = (TextView)view.findViewById(R.id.txtIsArrived);
        currentStatus = (TextView)view.findViewById(R.id.txtCurrentStatus);
        midStationList   = (ListView)view.findViewById(R.id.listViewTrainRoute);
        setLayoutData();
        setMidStationAdapter();
    }

    public void setTrainStatusVoList(ArrayList<TrainStatusVO> trainStatusVoList)
    {
        _trainStatusVOs = trainStatusVoList;
    }

    private void setLayoutData()
    {
        if(_trainStatusVOs !=null)
        {
            trainNumber.setText(_trainStatusVOs.get(0).getTrainNumber());
            currentStation.setText(_trainStatusVOs.get(0).getCurrentStation());
            if(_trainStatusVOs.get(0).isArrived())
            {
                arrived.setText("Arrived");
            }
            else
            {
                arrived.setText("Delayed");
            }

            currentStatus.setText(_trainStatusVOs.get(0).getCurrentStatus());
        }
    }

    private void setMidStationAdapter()
    {
        adapter = new TrainStatusAdapter(getContext(), _trainStatusVOs.get(0).getMidStationList() );
        midStationList.setAdapter(adapter);
    }
}

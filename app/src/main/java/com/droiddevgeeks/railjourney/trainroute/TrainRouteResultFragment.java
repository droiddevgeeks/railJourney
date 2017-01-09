package com.droiddevgeeks.railjourney.trainroute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.TrainRouteVO;

import java.util.List;

import static com.droiddevgeeks.railjourney.R.id.txtDestinationStation;
import static com.droiddevgeeks.railjourney.R.id.txtSourceStation;
import static com.droiddevgeeks.railjourney.R.id.txtTrainRunsOn;

/**
 * Created by Vampire on 2016-10-15.
 */

public class TrainRouteResultFragment extends Fragment
{

    private List<TrainRouteVO> _list;
    private TextView _txtTrainName;
    private TextView _txtTrainNumber;
    private TextView _txtSourceStation, _txtDestinationStation, _txtTrainRunsOn;
    private ListView _listViewTrainRoute ;
    private TrainRouteAdapter _adapter;
    private TextView _pageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate( R.layout.train_route_result , container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Route");
        _txtTrainName = (TextView)view.findViewById( R.id.txtTrainName );
        _txtTrainName.setText( _list.get( 0 ).getTrainName() );

        _txtSourceStation = (TextView)view.findViewById( R.id.txtSourceStation );
        _txtSourceStation.setText( _list.get( 0 ).getList().get( 0 ).getStationName() );

        _txtDestinationStation = (TextView)view.findViewById( R.id.txtDestinationStation );
        _txtDestinationStation.setText( _list.get( 0 ).getList().get(_list.get( 0 ).getList().size() -1  ).getStationName() );

        _txtTrainNumber = (TextView)view.findViewById( R.id.txtTrainNumber );
        _txtTrainNumber.setText( _list.get( 0 ).getTrainNumber() );

        _txtSourceStation = (TextView)view.findViewById( txtSourceStation );
        _txtDestinationStation = (TextView)view.findViewById( txtDestinationStation );

        _txtTrainRunsOn =  (TextView)view.findViewById( txtTrainRunsOn );
        _txtTrainRunsOn.setText( _list.get( 0 ).getTrainRunsOn() );

        _listViewTrainRoute = (ListView)view.findViewById( R.id.listViewTrainRoute );
        _adapter = new TrainRouteAdapter(getContext() , _list.get( 0 ).getList()  );
        _listViewTrainRoute.setAdapter( _adapter );
    }

    public void setTrainRouteListData(List<TrainRouteVO> list)
    {
        _list = list ;
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _list = null;
        _adapter = null;
        _txtTrainName = null;
        _txtTrainRunsOn = null;
        _txtSourceStation = null ;
        _listViewTrainRoute = null;
        _txtDestinationStation = null;
    }
}

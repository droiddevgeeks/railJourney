package com.droiddevgeeks.railjourney.trainroute;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.MidStationInfo;

import java.util.List;

/**
 * Created by kishan.maurya on 20-10-2016.
 */

public class TrainRouteAdapter extends BaseAdapter
{

    private Context _context;
    private List<MidStationInfo> _list;

    public TrainRouteAdapter(Context context, List<MidStationInfo> list)
    {
        _context = context;
        _list = list;
    }

    @Override
    public int getCount()
    {
        return _list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return _list.get( i );
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TrainRouteViewHolder holder ;
        if ( view == null )
        {
            view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.train_route_station_list_layout, viewGroup, false );
            holder = new TrainRouteAdapter.TrainRouteViewHolder( view );
            view.setTag( holder );
        }
        else
        {
            holder = (TrainRouteAdapter.TrainRouteViewHolder) view.getTag();
        }

        holder.stationName.setText( _list.get( i ).getStationName() );
        holder.arrivalTime.setText( _list.get( i ).getArrivalTime() );
        holder.deptTime.setText( _list.get( i ).getDeptTime() );
        holder.distanceFromSource.setText( _list.get( i ).getDistanceFromSource() );

        return view;
    }


    private class TrainRouteViewHolder
    {
        TextView stationName , arrivalTime , deptTime , distanceFromSource;
        public TrainRouteViewHolder(View view)
        {
            stationName = (TextView)view.findViewById( R.id.txtStationName );
            arrivalTime = (TextView)view.findViewById( R.id.txtArrivalTime );
            deptTime = (TextView)view.findViewById( R.id.txtDeptTime );
            distanceFromSource = (TextView)view.findViewById( R.id.txtDistanceFromSource );
        }
    }


}

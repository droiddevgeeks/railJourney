package com.droiddevgeeks.railjourney.train_status;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.MidStationInfo;
import com.droiddevgeeks.railjourney.trainroute.TrainRouteAdapter;

import java.util.List;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainStatusAdapter extends BaseAdapter
{

    private Context _context;
    private List<MidStationInfo> _list;

    public TrainStatusAdapter(Context context, List<MidStationInfo> list)
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
        return _list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TrainStatusViewHolder holder;
        String time = null;
        if (view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.live_train_mid_station, viewGroup, false);
            holder = new TrainStatusAdapter.TrainStatusViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (TrainStatusAdapter.TrainStatusViewHolder) view.getTag();
        }

        holder.stationName.setText(_list.get(i).getStationName());
        holder.arrivalTime.setText(_list.get(i).getArrivalTime());
        holder.deptTime.setText(_list.get(i).getDeptTime());
        if (_list.get(i).isArrived())
        {
            holder.isArrived.setText("Yes");
        }
        else
        {
            holder.isArrived.setText("Not Yet");
        }

        if (_list.get(i).getLatemin() == 60)
        {
            time = "1 Hr";
        }
        else if (_list.get(i).getLatemin() < 60)
        {
            if (_list.get(i).getLatemin() == 0)
            {
                time = "Source";
            }
            else
            {
                time = _list.get(i).getLatemin() + " min";
            }
        }
        else
        {
            int hr = _list.get(i).getLatemin() / 60;
            int min = _list.get(i).getLatemin() % 60;
            time = hr + " Hr " + min + " min";
        }
        holder.lateMin.setText(time);

        return view;
    }


    private class TrainStatusViewHolder
    {
        TextView stationName, arrivalTime, deptTime, isArrived, lateMin;

        public TrainStatusViewHolder(View view)
        {
            stationName = (TextView) view.findViewById(R.id.txtStationName);
            arrivalTime = (TextView) view.findViewById(R.id.txtArrivalTime);
            deptTime = (TextView) view.findViewById(R.id.txtDeptTime);
            isArrived = (TextView) view.findViewById(R.id.txtIsArrived);
            lateMin = (TextView) view.findViewById(R.id.txtLateMin);
        }
    }


}


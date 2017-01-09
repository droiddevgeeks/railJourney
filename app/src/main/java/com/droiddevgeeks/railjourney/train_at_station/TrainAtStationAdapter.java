package com.droiddevgeeks.railjourney.train_at_station;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.TrainAtStationVO;

import java.util.List;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationAdapter extends BaseAdapter
{

    private Context context;
    private List<TrainAtStationVO> list;

    public TrainAtStationAdapter(Context context, List<TrainAtStationVO> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int i)
    {
        return list.get(i);
    }

    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        TrainAtStationViewHolder viewHolder;
        if (view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.train_at_list_layout, viewGroup, false);
            viewHolder = new TrainAtStationViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (TrainAtStationViewHolder) view.getTag();
        }

        viewHolder.trainName.setText(list.get(i).getName());
        viewHolder.trainNumber.setText(list.get(i).getNumber());
    return view;
}


private class TrainAtStationViewHolder
{
    TextView trainName, trainNumber;

    public TrainAtStationViewHolder(View view)
    {
        trainName = (TextView) view.findViewById(R.id.txtTrainName);
        trainNumber = (TextView) view.findViewById(R.id.txtTrainNumber);
    }
}
}

package com.droiddevgeeks.railjourney.canceltrain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.navigation_drawer.NavigationDrawerListAdapter;

import java.util.List;

/**
 * Created by Vampire on 2016-10-13.
 */

public class CancelTrainAdapter extends BaseAdapter
{
    private Context _context;
    private List<CancelTrainVO> _list;

    public CancelTrainAdapter(Context context, List<CancelTrainVO> list)
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
    public Object getItem(int position)
    {
        return _list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        CancelTrainViewHolder holder;
        if (convertView == null)
        {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cancel_train_result, parent, false);
            holder = new CancelTrainViewHolder(convertView);
            convertView.setTag(holder);
        }
        else
        {
            holder = (CancelTrainAdapter.CancelTrainViewHolder) convertView.getTag();
        }

        return convertView;

    }


    private class CancelTrainViewHolder
    {
        private TextView trainName, fromStation, toStation, trainTime;

        public CancelTrainViewHolder(View itemView)
        {
            trainName = (TextView) itemView.findViewById(R.id.txtTrainNameValue);
            fromStation = (TextView) itemView.findViewById(R.id.txtSourceStation);
            toStation = (TextView) itemView.findViewById(R.id.txtDestinationStation);
            trainTime = (TextView) itemView.findViewById(R.id.txtTrainTimeValue);
        }
    }
}

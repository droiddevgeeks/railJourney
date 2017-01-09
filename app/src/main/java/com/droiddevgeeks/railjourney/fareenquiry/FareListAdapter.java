package com.droiddevgeeks.railjourney.fareenquiry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.FareVO;
import com.droiddevgeeks.railjourney.models.PassengerVO;
import com.droiddevgeeks.railjourney.pnr.PassengerListAdapter;

import java.util.List;

/**
 * Created by Vampire on 2016-12-29.
 */

public class FareListAdapter extends BaseAdapter
{

    private Context context;
    private List<FareVO> list;
    public FareListAdapter(Context context , List<FareVO> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        FareListAdapter.FareViewHolder mViewHolder;

        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.pnr_status_layout,null);
            mViewHolder = new FareListAdapter.FareViewHolder(view);
            view.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (FareListAdapter.FareViewHolder) view.getTag();
        }

        FareVO currentListData = list.get(i);

        mViewHolder.fare.setText(currentListData.getFare());
        mViewHolder.travelClass.setText(currentListData.getTravelClassName());
        return view;
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


    private class FareViewHolder
    {
        TextView fare, travelClass;
        public FareViewHolder(View item)
        {
            travelClass = (TextView) item.findViewById(R.id.txtTravelClass);
            fare = (TextView) item.findViewById(R.id.txtTravelClassFare);


        }
    }
}


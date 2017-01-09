package com.droiddevgeeks.railjourney.pnr;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.PassengerVO;

import java.util.List;

/**
 * Created by Vampire on 2016-12-25.
 */

public class PassengerListAdapter extends BaseAdapter
{

    private Context context;
    private List<PassengerVO> list;
    public PassengerListAdapter(Context context , List<PassengerVO> list)
    {
        this.context = context;
        this.list = list;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        PassengerViewHolder mViewHolder;

        if (view == null)
        {
            view = LayoutInflater.from(context).inflate(R.layout.pnr_status_layout,null);
            mViewHolder = new PassengerViewHolder(view);
            view.setTag(mViewHolder);
        }
        else
        {
            mViewHolder = (PassengerViewHolder) view.getTag();
        }

        PassengerVO currentListData = list.get(i);

        mViewHolder.number.setText(currentListData.getPassengerNumber());
        mViewHolder.coachNumber.setText(currentListData.getCoachNumber());
        mViewHolder.currentStatus.setText(currentListData.getCurrentStatus());
        mViewHolder.bookingStatus.setText(currentListData.getBookingStatus());

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


    private class PassengerViewHolder
    {
        TextView number, coachNumber,currentStatus, bookingStatus;
        public PassengerViewHolder(View item)
        {
            number = (TextView) item.findViewById(R.id.txtPassengerNumber);
            coachNumber = (TextView) item.findViewById(R.id.txtCoachTitleValue);
            currentStatus = (TextView) item.findViewById(R.id.txtCurrentBookingValue);
            bookingStatus = (TextView) item.findViewById(R.id.txtBookngStatusValue);
        }
    }
}

package com.droiddevgeeks.railjourney.seat_availability;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.SeatDateVO;
import com.droiddevgeeks.railjourney.train_status.TrainStatusAdapter;

import java.util.List;

/**
 * Created by Vampire on 2017-01-04.
 */

public class SeatAvailbilityAdapter extends BaseAdapter
{

    private Context context;
    private List<SeatDateVO> list;
    public SeatAvailbilityAdapter(Context context , List<SeatDateVO> list)
    {
        this.list = list;
        this.context = context;
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
        TrainSeatViewHolder holder;
        String time = null;
        if (view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seat_list_row_layout, viewGroup, false);
            holder = new TrainSeatViewHolder(view);
            view.setTag(holder);
        }
        else
        {
            holder = (TrainSeatViewHolder) view.getTag();
        }

        holder.date.setText(list.get(i).getDate());
        holder.status.setText(list.get(i).getSeatsStatus());

        return  view;
    }

    private class TrainSeatViewHolder
    {
        TextView date, status;

        public TrainSeatViewHolder(View view)
        {
            date = (TextView) view.findViewById(R.id.txtDate);
            status = (TextView) view.findViewById(R.id.txtSeatsCount);
        }
    }
}

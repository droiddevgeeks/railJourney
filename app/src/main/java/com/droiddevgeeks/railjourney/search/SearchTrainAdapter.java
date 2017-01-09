package com.droiddevgeeks.railjourney.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.SearchVO;

import java.util.List;

/**
 * Created by Vampire on 2017-01-01.
 */

public class SearchTrainAdapter extends BaseAdapter
{

    private Context context;
    private List<SearchVO> list;

    public SearchTrainAdapter(Context context, List<SearchVO> list)
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
        SearchViewHolder viewHolder;
        if (view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.search_train_list, viewGroup, false);
            viewHolder = new SearchViewHolder(view);
            view.setTag(viewHolder);
        }
        else
        {
            viewHolder = (SearchViewHolder) view.getTag();
        }

        viewHolder.trainName.setText(list.get(i).getTrainName());
        viewHolder.trainTime.setText(list.get(i).getTrainArrival());
        viewHolder.trainNumber.setText(list.get(i).getTrainNumber());
        return view;
    }


    private class SearchViewHolder
    {
        TextView trainName, trainTime,trainNumber;

        public SearchViewHolder(View view)
        {
            trainName = (TextView) view.findViewById(R.id.txtTrainName);
            trainTime = (TextView) view.findViewById(R.id.txtTime);
            trainNumber = (TextView) view.findViewById(R.id.txtTrainNumber);
        }
    }
}


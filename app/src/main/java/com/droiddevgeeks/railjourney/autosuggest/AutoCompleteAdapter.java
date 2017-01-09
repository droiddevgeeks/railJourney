package com.droiddevgeeks.railjourney.autosuggest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.trainroute.TrainRouteAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class AutoCompleteAdapter extends BaseAdapter
{


    private List<AutoCompleteVO> list;
    private ArrayList<AutoCompleteVO> arrayList;

    public AutoCompleteAdapter(List<AutoCompleteVO> list)
    {
        this.list = list;
        arrayList = new ArrayList<>();
        arrayList.addAll(list);
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

        TextView autoSuggestText;
        if (view == null)
        {
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.auto_complete_layout, viewGroup, false);
        }
        autoSuggestText = (TextView) view.findViewById(R.id.autoSuggestText);
        autoSuggestText.setText(list.get(i).getName());
        return view;

    }

    // Filter Class
    public void filter(String charText)
    {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0)
        {
            list.addAll(arrayList);
        }
        else
        {
            for (AutoCompleteVO autoCompleteVO : arrayList)
            {
                if (autoCompleteVO.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(autoCompleteVO);
                }
            }
        }
        notifyDataSetChanged();
    }
}
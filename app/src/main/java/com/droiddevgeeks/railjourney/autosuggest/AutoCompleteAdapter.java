package com.droiddevgeeks.railjourney.autosuggest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
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

public class AutoCompleteAdapter extends BaseAdapter implements Filterable
{


    private List<AutoCompleteVO> list;
    private ArrayList<AutoCompleteVO> arrayList;
    private AutoSuggestFilter autoSuggestFilter;

    public AutoCompleteAdapter(List<AutoCompleteVO> list)
    {
        this.list = list;
        this.arrayList = new ArrayList<AutoCompleteVO>();
        this.arrayList.addAll(list);
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

    @Override
    public Filter getFilter()
    {
        if(autoSuggestFilter == null)
        {
            autoSuggestFilter = new AutoSuggestFilter();
        }
        return autoSuggestFilter;
    }

    private class AutoSuggestFilter extends Filter
    {


        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            // Create a FilterResults object
            FilterResults results = new FilterResults();

            // If the constraint (search string/pattern) is null
            // or its length is 0, i.e., its empty then
            // we just set the `values` property to the
            // original contacts list which contains all of them
            if (constraint == null || constraint.length() == 0)
            {
                results.values = arrayList;
                results.count = arrayList.size();
            }
            else
            {
                // Some search constraint has been passed
                // so let's filter accordingly
                List<AutoCompleteVO> filteredList = new ArrayList<AutoCompleteVO>();

                // We'll go through all the contacts and see
                // if they contain the supplied string
                for (AutoCompleteVO c : arrayList)
                {
                    if (c.getName().toLowerCase(Locale.getDefault()).contains(constraint.toString().toLowerCase(Locale.getDefault())))
                    {
                        // if `contains` == true then add it
                        // to our filtered list
                        filteredList.add(c);
                    }
                }

                // Finally set the filtered values and size/count
                results.values = filteredList;
                results.count = filteredList.size();
            }

            // Return our FilterResults object
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            list = (List<AutoCompleteVO>) results.values;
            notifyDataSetChanged();
        }

    }

}
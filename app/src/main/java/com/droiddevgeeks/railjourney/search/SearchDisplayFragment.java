package com.droiddevgeeks.railjourney.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchDisplayFragment extends Fragment
{
    private ArrayList<SearchVO> _searchVOArrayList;

    @Override
    public void onCreate( @Nullable Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
    }

    public void setSearchArrayList( ArrayList<SearchVO> searchArrayList )
    {
        _searchVOArrayList = searchArrayList;
    }

    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        View view = inflater.inflate(R.layout.demo_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated( View view, @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.lstView);
        if ( _searchVOArrayList.size() > 0 ) {
            SearchAdapter searchAdapter = new SearchAdapter();
            listView.setAdapter(searchAdapter);
        }
    }

    public class SearchAdapter extends BaseAdapter
    {
        @Override
        public int getCount()
        {
            return _searchVOArrayList.size();
        }

        @Override
        public Object getItem( int position )
        {
            return position;
        }

        @Override
        public long getItemId( int position )
        {
            return 0;
        }

        @Override
        public View getView( int position, View convertView, ViewGroup parent )
        {
            SearchViewHolder searchViewHolder = null;
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            if ( convertView == null ) {
                convertView = inflater.inflate(R.layout.train_btw_stations_display, parent, false);
                searchViewHolder = new SearchViewHolder();
                searchViewHolder.txtTrainName = (TextView)convertView.findViewById(R.id.txtTrainName);
                searchViewHolder.txtTrainNumber = (TextView)convertView.findViewById(R.id.txtTrainNumber);
                searchViewHolder.txtTrainDeparture = (TextView)convertView.findViewById(R.id.txtTrainDepartureTime);
                searchViewHolder.txtTrainArrival = (TextView)convertView.findViewById(R.id.txtTrainArrivalTime);
                searchViewHolder.txtTrainDistance = (TextView)convertView.findViewById(R.id.txtTrainTotalTime);
                searchViewHolder.txtDaysInfo = (TextView)convertView.findViewById(R.id.txtDateInfo);
                searchViewHolder.txtClassInfo = (TextView)convertView.findViewById(R.id.txtClassInfo);
                convertView.setTag(searchViewHolder);
            }
            else
            {
                searchViewHolder = (SearchViewHolder) convertView.getTag();
            }
            searchViewHolder.txtTrainName.setText(_searchVOArrayList.get(position).getTrainName());
            searchViewHolder.txtTrainNumber.setText(_searchVOArrayList.get(position).getTrainNumber());
            searchViewHolder.txtTrainDeparture.setText(_searchVOArrayList.get(position).getTrainDeparture());
            searchViewHolder.txtTrainArrival.setText(_searchVOArrayList.get(position).getTrainArrival());
            searchViewHolder.txtTrainDistance.setText(_searchVOArrayList.get(position).getTravelTime());
            searchViewHolder.txtDaysInfo.setText(_searchVOArrayList.get(position).getDays());
            searchViewHolder.txtClassInfo.setText(_searchVOArrayList.get(position).getClassInfo());

            return convertView;
        }

        public class SearchViewHolder
        {
            private TextView txtTrainName;
            private TextView txtTrainNumber;
            private TextView txtTrainDeparture;
            private TextView txtTrainArrival;
            private TextView txtTrainDistance;
            private TextView txtDaysInfo;
            private TextView txtClassInfo;
        }

    }
}

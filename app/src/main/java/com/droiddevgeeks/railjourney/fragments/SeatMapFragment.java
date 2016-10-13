package com.droiddevgeeks.railjourney.fragments;

import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.adapters.SeatMapAdapter;
import com.droiddevgeeks.railjourney.models.SeatMapVO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2016-10-08.
 */
public class SeatMapFragment extends Fragment implements AdapterView.OnItemClickListener
{


    private ListView _listView;
    private SeatMapAdapter _adapter;
    private List<SeatMapVO> _list;
    private ImageView _trainSeatMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View seatMapView = inflater.inflate(R.layout.seat_map_layout, container, false);
        init(seatMapView);
        setSeatMapListAdapter();
        return seatMapView;
    }


    private void init(View seatMapView)
    {
        _listView = (ListView) seatMapView.findViewById(R.id.seatMapList);
        _trainSeatMap = (ImageView) seatMapView.findViewById(R.id.trainSeatMap);
    }

    private void setSeatMapListAdapter()
    {
        _adapter = new SeatMapAdapter(getContext(), getList());
        _listView.setAdapter(_adapter);

        _listView.setOnItemClickListener(this);

    }

    private List<SeatMapVO> getList()
    {
        _list = new ArrayList<SeatMapVO>();
        _list.add(new SeatMapVO("Sleeper Class", R.drawable.sleeper));
        _list.add(new SeatMapVO("First AC", R.drawable.firstac));
        _list.add(new SeatMapVO("Second AC", R.drawable.secondac));
        _list.add(new SeatMapVO("Third AC", R.drawable.third));
        _list.add(new SeatMapVO("Shatabdi", R.drawable.shatabdi));
        _list.add(new SeatMapVO("Shatabdi CC", R.drawable.shatabdi_cc));

        return _list;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        _listView.setVisibility(View.GONE);
        _trainSeatMap.setVisibility(View.VISIBLE);
        _trainSeatMap.setImageResource(getList().get(position).getSeatArrangement());
    }


    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _list = null;
        _adapter = null;
        _listView = null;
        _trainSeatMap = null;

    }
}

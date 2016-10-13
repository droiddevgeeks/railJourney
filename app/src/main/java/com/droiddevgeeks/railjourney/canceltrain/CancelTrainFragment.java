package com.droiddevgeeks.railjourney.canceltrain;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.droiddevgeeks.railjourney.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2016-10-13.
 */

public class CancelTrainFragment extends Fragment
{

    private ListView _cancelTrainListView;
    private CancelTrainAdapter _adapter;
    private List<CancelTrainVO> _list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View cancelTrainView = inflater.inflate(R.layout.cancel_train_layout , container , false);
        return cancelTrainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _cancelTrainListView = (ListView)view.findViewById(R.id.listViewCancelTrain);
        initList();
        setCancelTrainAdapter();
    }

    private void setCancelTrainAdapter()
    {
        _adapter = new CancelTrainAdapter(getContext() , _list);
        _cancelTrainListView.setAdapter(_adapter);
    }

    private void initList()
    {
        _list = new ArrayList<CancelTrainVO>();
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));
        _list.add(new CancelTrainVO("Pushpak", "Lucknow", "Mumbai" , "07:20"));

    }
}

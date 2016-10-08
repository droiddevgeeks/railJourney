package com.droiddevgeeks.railjourney.fragments;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.GridView;

import com.droiddevgeeks.railjourney.ImplementationClass;
import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.adapters.HomeScreenAdapter;
import com.droiddevgeeks.railjourney.models.SectionVO;
import com.droiddevgeeks.railjourney.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/4/2016.
 */
public class HomeScreenFragment extends Fragment
{
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState )
    {
        View view = inflater.inflate(R.layout.home_screen_fragment, container, false);
        init(view);
        return view;
    }

    private void init( View view )
    {
        int width;
        int height;
        if( Utilities.getDeviceWidth(getContext()) < 500 && Utilities.getDeviceHeight(getContext()) < 1000)
        {
            width = Utilities.getDeviceWidth( getContext())/2 - 15;
            height = Utilities.getDeviceHeight( getContext())/4;
        }
        else
        {
            width = Utilities.getDeviceWidth( getContext())/2 - 40;
            height = Utilities.getDeviceHeight( getContext())/4 - 48;
        }
        Log.v( "HomeScreenFragment", Utilities.getDeviceHeight(getContext())+" "+ Utilities.getDeviceWidth(getContext()));
        GridView homeScreenGridView = (GridView)view.findViewById( R.id.gridViewHomeScreen);
        HomeScreenAdapter homeScreenAdapter = new HomeScreenAdapter( getContext(), R.layout.grid_child_layout, getSectionArrayList(), width, height, new ImplementationClass());
        homeScreenGridView.setAdapter(homeScreenAdapter);
    }

    private ArrayList<SectionVO> getSectionArrayList()
    {
        ArrayList<SectionVO> sectionArrayList = new ArrayList<>();
        sectionArrayList.add( new SectionVO( R.drawable.pnr_check, "PNR Check"));
        sectionArrayList.add( new SectionVO( R.drawable.search, "Search"));
        sectionArrayList.add( new SectionVO( R.drawable.fare_enquiry, "Fare Enquiry"));
        sectionArrayList.add( new SectionVO( R.drawable.seat_availibility, "Seat Availability"));
        sectionArrayList.add( new SectionVO( R.drawable.live_check, "Train Status"));
        sectionArrayList.add( new SectionVO( R.drawable.route, "Train Route"));
        sectionArrayList.add( new SectionVO( R.drawable.cancelled, "Cancelled Train"));
        sectionArrayList.add( new SectionVO( R.drawable.station, "Train At Station"));
        return sectionArrayList;
    }
}

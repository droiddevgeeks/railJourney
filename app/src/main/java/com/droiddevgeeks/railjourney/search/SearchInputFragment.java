package com.droiddevgeeks.railjourney.search;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchInputFragment extends Fragment implements IDownloadListener
{
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
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync( "http://api.railwayapi.com/between/source/gkp/dest/ngp/date/27-08/apikey/h4s56sdg/", new SearchTrainResponse(this));
        downloadJSONAsync.execute();
    }

    @Override
    public void onDownloadSuccess( DownloadParseResponse downloadParseResponse )
    {
        if( downloadParseResponse.getType() == 200)
        {
            SearchTrainResponse searchTrainResponse = (SearchTrainResponse)downloadParseResponse;
            ArrayList<SearchVO> searchVOs = searchTrainResponse.get_searchVOs();
            SearchDisplayFragment searchDisplayFragment = new SearchDisplayFragment();
            searchDisplayFragment.setSearchArrayList(searchVOs);
            getActivity().getSupportFragmentManager().beginTransaction().replace( R.id.container, searchDisplayFragment).commit();
        }

    }

    @Override
    public void onDownloadFailed()
    {

    }
}

package com.droiddevgeeks.railjourney.search;

import android.util.Log;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchTrainResponse extends DownloadParseResponse
{
    ArrayList<SearchVO> _searchVOs;
    public SearchTrainResponse( IDownloadListener iDownloadListener )
    {
        super(iDownloadListener);
    }

    public ArrayList<SearchVO> get_searchVOs()
    {
        return _searchVOs;
    }

    @Override
    public void parseJson( JSONObject jsonObject, DownloadParseResponse downloadParseResponse )
    {
        try {
//            ArrayList<SearchVO.DaysVO> daysVOs = new ArrayList<>();
//            ArrayList<SearchVO.ClassVO> classVOs = new ArrayList<>();
            _searchVOs = new ArrayList<>();

            int res = jsonObject.getInt("response_code");
            if( res == 200 )
            {
                int total = jsonObject.getInt("total");
                for( int i = 0; i < total; i++)
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("train");
                    int length = jsonArray.length();
                    for( int j = 0; j< length; j++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(j);

                        JSONArray jsonArray1 = jsonObject1.getJSONArray("days");
                        int lenDays = jsonArray1.length();
                        StringBuilder stringBuilder = new StringBuilder();
                        for( int k = 0; k < lenDays; k++)
                        {
                            JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                            if( jsonObject2.getString("runs").equalsIgnoreCase("Y"))
                            {
                                stringBuilder = stringBuilder.append(jsonObject2.getString("day-code"));
                            }
                            //daysVOs.add( new SearchVO.DaysVO(  jsonObject2.getString("day-code"), jsonObject2.getString("runs")));
                        }
                        String day = stringBuilder.toString();
                        JSONArray jsonArray2 = jsonObject1.getJSONArray("classes");
                        stringBuilder = new StringBuilder();
                        int lenClass = jsonArray2.length();
                        for( int k = 0; k < lenClass; k++)
                        {
                            JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                            if( jsonObject2.getString("available").equalsIgnoreCase("Y"))
                            {
                                stringBuilder = stringBuilder.append(jsonObject2.getString("class-code"));
                            }
                            //classVOs.add( new SearchVO.ClassVO(  jsonObject2.getString("class-code"), jsonObject2.getString("available")));
                        }
                        String classVo = stringBuilder.toString();
                        _searchVOs.add( new SearchVO( jsonObject1.getString("name"), jsonObject1.getString("number"), jsonObject1.getString("travel_time"), jsonObject1.getString("src_departure_time"), jsonObject1.getString("dest_arrival_time"), day, classVo));
                    }
                }
                iDownloadListener.onDownloadSuccess(downloadParseResponse);
            }
            else
            {
                iDownloadListener.onDownloadFailed();
            }
            Log.v("SearchTrainResponse",res+"");
        }
        catch ( JSONException e ) {
            super.iDownloadListener.onDownloadFailed();
            e.printStackTrace();
        }
    }

    @Override
    public int getType()
    {
        return 200;
    }
}

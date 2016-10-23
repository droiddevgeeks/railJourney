package com.droiddevgeeks.railjourney.trainroute;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.MidStationInfo;
import com.droiddevgeeks.railjourney.models.TrainRouteVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.maurya on 20-10-2016.
 */

public class TrainRouteResponse extends DownloadParseResponse
{

    private List<TrainRouteVO> _trainRouteList;
    private List<MidStationInfo> _midStationList;

    public TrainRouteResponse(IDownloadListener iDownloadListener)
    {
        super( iDownloadListener );
        _trainRouteList = new ArrayList<>();
        _midStationList = new ArrayList<>();
    }


    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        StringBuilder trainRunsOnDays = null;
        try
        {
            int responseCode = jsonObject.getInt( "response_code" );
            if ( responseCode == 200 )
            {
                JSONArray jsonArray = jsonObject.getJSONArray( "route" );
                int len = jsonArray.length();
                if ( len > 0 )
                {
                    for ( int i = 0; i < len; i++ )
                    {
                        JSONObject trainStationObject = jsonArray.getJSONObject( i );
                        _midStationList.add( new MidStationInfo(
                                trainStationObject.getString( "fullname" ),
                                trainStationObject.getString( "scharr" ),
                                trainStationObject.getString( "schdep" ),
                                trainStationObject.getString( "distance" )
                        ) );
                        ;
                    }

                }
                JSONObject trainInfoObject = jsonObject.getJSONObject( "train" );
                JSONArray trainRunsOn = trainInfoObject.getJSONArray( "days" );
                trainRunsOnDays = new StringBuilder();
                if ( trainRunsOn.length() > 0 )
                {
                    for ( int i = 0; i < trainRunsOn.length(); i++ )
                    {
                        JSONObject days = trainRunsOn.getJSONObject( i );
                        if ( days.getString( "runs" ).equalsIgnoreCase( "Y" ) )
                        {
                            trainRunsOnDays.append( days.getString( "day-code" ) );
                            trainRunsOnDays.append( "," );
                        }
                    }

                }

                _trainRouteList.add( new TrainRouteVO(
                        trainInfoObject.getString( "name" ),
                        trainInfoObject.getString( "number" ),
                        trainRunsOnDays.toString(),
                        _midStationList ) );

                super.iDownloadListener.onDownloadSuccess( downloadParseResponse );

            }
            else
            {
                super.iDownloadListener.onDownloadFailed();
            }

        }
        catch ( JSONException e )
        {
            e.printStackTrace();
        }

    }


    public List<TrainRouteVO> getTrainRouteList()
    {
        return _trainRouteList;
    }


    @Override
    public int getType()
    {
        return 600;
    }
}

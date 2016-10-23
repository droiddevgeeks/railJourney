package com.droiddevgeeks.railjourney.canceltrain;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.CancelledTrainVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class CancelTrainResponse extends DownloadParseResponse
{
    private List<CancelledTrainVO> _cancelTrainList;
    private Context _context;

    public CancelTrainResponse(IDownloadListener iDownloadListener, Context context)
    {
        super( iDownloadListener );
        _context = context;
        _cancelTrainList = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        String todaysDate = (new SimpleDateFormat( "dd-MM-yyyy" ).format( new Date() )).toString();
        String jsonString = JsonStorage.getJsonFileData( _context, todaysDate );
        if ( jsonString == null )
        {
            JsonStorage.saveJsonToFile( _context, jsonObject.toString(), todaysDate );
        }
        try
        {
            int responseCode = jsonObject.getInt( "response_code" );
            if ( responseCode == 200 )
            {
                JSONArray jsonArray = jsonObject.getJSONArray( "trains" );
                int len = jsonArray.length();
                if ( len > 0 )
                {
                    for ( int i = 0; i < len; i++ )
                    {
                        JSONObject trainInfoJsonObject = jsonArray.getJSONObject( i );

                        JSONObject trainName = trainInfoJsonObject.getJSONObject( "train" );
                        JSONObject trainSource = trainInfoJsonObject.getJSONObject( "source" );
                        JSONObject trainDest = trainInfoJsonObject.getJSONObject( "dest" );

                        _cancelTrainList.add( new CancelledTrainVO(
                                trainName.getString( "name" ),
                                trainName.getString( "number" ),
                                trainName.getString( "start_time" ),
                                trainSource.getString( "name" ),
                                trainDest.getString( "name" )
                        ) );
                    }
                    super.iDownloadListener.onDownloadSuccess( downloadParseResponse );
                }
                else
                {
                    super.iDownloadListener.onDownloadFailed();
                }
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

    @Override
    public int getType()
    {
        return 0;
    }

    public List<CancelledTrainVO> getCancelTrainStatusVOs()
    {
        return _cancelTrainList;
    }


}

package com.droiddevgeeks.railjourney.autosuggest;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vampire on 2016-12-27.
 */

public class AutoSuggestStationResponse extends DownloadParseResponse
{

    private List<AutoCompleteVO> _autoCompleTeList;
    private Context _context;

    public AutoSuggestStationResponse(IDownloadListener iDownloadListener , Context context)
    {
        super( iDownloadListener );
        _autoCompleTeList = new ArrayList<>();
        _context = context;
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {

        /*String todaysDate = (new SimpleDateFormat( "dd-MM-yyyy" ).format( new Date() )).toString();
        String jsonString = JsonStorage.getJsonFileData( _context, "AutoSuggestStation"+todaysDate );
        if ( jsonString == null )
        {
            JsonStorage.saveJsonToFile( _context, jsonObject.toString(), "AutoSuggestStation"+todaysDate );
        }*/
        try
        {
            int responseCode = jsonObject.getInt( "response_code" );
            if ( responseCode == 200 )
            {
                JSONArray jsonArray = jsonObject.getJSONArray( "station" );
                int len = jsonArray.length();
                if ( len > 0 )
                {
                    for ( int i = 0; i < len; i++ )
                    {
                        JSONObject trainObject = jsonArray.getJSONObject( i );
                        _autoCompleTeList.add( new AutoCompleteVO( trainObject.getString( "code" ), trainObject.getString( "fullname" ) ) );
                    }
                    super.iDownloadListener.onDownloadSuccess( downloadParseResponse );
                }
                else
                {
                    super.iDownloadListener.onDownloadFailed(204, "Not able to fetch required data");
                }
            }
            else if( responseCode == 204)
            {
                super.iDownloadListener.onDownloadFailed(204, "Not able to fetch required data");
            }
            else if( responseCode == 403)
            {
                super.iDownloadListener.onDownloadFailed(403, "Not able to fetch data, Please try later");
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
        return 250;
    }

    public List<AutoCompleteVO> getAutoSuggestList()
    {
        return _autoCompleTeList;
    }
}


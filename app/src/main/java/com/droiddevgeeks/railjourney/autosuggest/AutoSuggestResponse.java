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
 * Created by kishan.maurya on 19-10-2016.
 */

public class AutoSuggestResponse extends DownloadParseResponse
{

    private List<AutoCompleteVO> _autoCompleTeList;
    private Context _context;

    public AutoSuggestResponse(IDownloadListener iDownloadListener , Context context)
    {
        super( iDownloadListener );
        _autoCompleTeList = new ArrayList<>();
        _context = context;
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {

       /* String todaysDate = (new SimpleDateFormat( "dd-MM-yyyy" ).format( new Date() )).toString();
        String jsonString = JsonStorage.getJsonFileData( _context, "AutoSuggest"+todaysDate );
        if ( jsonString == null )
        {
            JsonStorage.saveJsonToFile( _context, jsonObject.toString(), "AutoSuggest"+todaysDate );
        }*/
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
                        JSONObject trainObject = jsonArray.getJSONObject( i );
                        _autoCompleTeList.add( new AutoCompleteVO( trainObject.getString( "number" ), trainObject.getString( "name" ) ) );
                    }
                    super.iDownloadListener.onDownloadSuccess( downloadParseResponse );
                }
                else
                {
                    super.iDownloadListener.onDownloadFailed(204, "Empty response ,Not able to fetch required data");
                }
            }
            else if( responseCode == 204)
            {
                super.iDownloadListener.onDownloadFailed(204, "Empty response ,Not able to fetch required data");
            }
            else if( responseCode == 403)
            {
                super.iDownloadListener.onDownloadFailed(100, "Not able to fetch data, Please try later");
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
        return 200;
    }

    public List<AutoCompleteVO> getAutoSuggestList()
    {
        return _autoCompleTeList;
    }
}


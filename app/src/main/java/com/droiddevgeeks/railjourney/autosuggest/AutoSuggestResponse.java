package com.droiddevgeeks.railjourney.autosuggest;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan.maurya on 19-10-2016.
 */

public class AutoSuggestResponse extends DownloadParseResponse
{

    private List<AutoCompleteVO> _autoCompleTeList;

    public AutoSuggestResponse(IDownloadListener iDownloadListener)
    {
        super( iDownloadListener );
        _autoCompleTeList = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {

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
        return 200;
    }

    public List<AutoCompleteVO> getAutoSuggestList()
    {
        return _autoCompleTeList;
    }
}


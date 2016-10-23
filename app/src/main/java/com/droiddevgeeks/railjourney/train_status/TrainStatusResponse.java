package com.droiddevgeeks.railjourney.train_status;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kunal on 10-10-2016.
 */
public class TrainStatusResponse extends DownloadParseResponse
{
    private ArrayList<TrainStatusVO> _trainStatusVOs;
    public TrainStatusResponse(IDownloadListener iDownloadListener)
    {
        super(iDownloadListener);
        _trainStatusVOs = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        try
        {

            int responseCode = jsonObject.getInt("response_code");
            if( responseCode == 200)
            {
                JSONArray jsonArray = jsonObject.getJSONArray("route");
                int len = jsonArray.length();
                if( len > 0)
                {
                    for( int i=0; i<len; i++)
                    {
                        JSONObject routeJson = jsonArray.getJSONObject(i);
                        _trainStatusVOs.add( new TrainStatusVO( routeJson.getString("fullname"), routeJson.getString("schdep"), routeJson.getInt("distance"), routeJson.getString("scharr")));
                    }
                    super.iDownloadListener.onDownloadSuccess(downloadParseResponse);
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
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getType()
    {
        return 0;
    }

    public ArrayList<TrainStatusVO> get_trainStatusVOs()
    {
        return _trainStatusVOs;
    }
}

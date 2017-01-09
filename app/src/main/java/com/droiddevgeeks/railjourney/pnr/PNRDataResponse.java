package com.droiddevgeeks.railjourney.pnr;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.CancelledTrainVO;
import com.droiddevgeeks.railjourney.models.PnrDataVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2016-12-25.
 */

public class PNRDataResponse extends DownloadParseResponse
{
    private Context _context;
    private List<PnrDataVO> _pnrInfoList;

    public PNRDataResponse(IDownloadListener iDownloadListener, Context context)
    {
        super(iDownloadListener);
        _context = context;
        _pnrInfoList = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        try
        {
            int responseCode = jsonObject.getInt("response_code");
            if (responseCode == 200)
            {
                _pnrInfoList.add(new PnrDataVO(jsonObject.getJSONObject("from_station").getString("name"),
                        jsonObject.getJSONObject("to_station").getString("name"),
                        jsonObject.getJSONObject("boarding_point").getString("name"),
                        jsonObject.getJSONObject("reservation_upto").getString("name"),
                        jsonObject.getString("train_num"),
                        jsonObject.getString("doj"),
                        jsonObject.getJSONArray("passengers")));
                super.iDownloadListener.onDownloadSuccess(downloadParseResponse);
            }
            else if(responseCode == 204)
            {
                super.iDownloadListener.onDownloadFailed(204, "Server not responding, Please try again");
            }
            else if(responseCode == 410)
            {
                super.iDownloadListener.onDownloadFailed(410, "PNR not generated yet");
            }
            else if(responseCode == 404)
            {
                super.iDownloadListener.onDownloadFailed(404, "Server not responding, Service Down");
            }
            else if( responseCode == 403)
            {
                super.iDownloadListener.onDownloadFailed(403, "Not able to fetch data, Please try later");
            }

        }
        catch (JSONException e)
        {
            super.iDownloadListener.onDownloadFailed(404, "Server not responding, Service Down");
            e.printStackTrace();
        }

    }

    @Override
    public int getType()
    {
        return 200;
    }

    public List<PnrDataVO> getPNRInfoList()
    {
        return _pnrInfoList;
    }
}

package com.droiddevgeeks.railjourney.fareenquiry;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.FareEnquiryVO;
import com.droiddevgeeks.railjourney.models.FareVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2016-12-29.
 */

public class FareEnquiryResponse extends DownloadParseResponse
{

    private Context _context;
    private List<FareEnquiryVO> fareEnquiryVOs;
    private List<FareVO> fareVOs;

    public FareEnquiryResponse(IDownloadListener iDownloadListener, Context context)
    {
        super(iDownloadListener);
        _context = context;
        fareVOs = new ArrayList<>();
        fareEnquiryVOs = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        try
        {
            int responseCode = jsonObject.getInt("response_code");
            if (responseCode == 200 || responseCode == 204)
            {
                JSONArray jsonArray = jsonObject.getJSONArray("fare");
                for (int i = 0; i < jsonArray.length(); i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    fareVOs.add(new FareVO(jsonObject1.getString("name"), jsonObject1.getString("fare")));
                }

                fareEnquiryVOs.add(new FareEnquiryVO(
                        jsonObject.getJSONObject("train").getString("name"),
                        jsonObject.getJSONObject("train").getString("number"),
                        jsonObject.getJSONObject("from").getString("name"),
                        jsonObject.getJSONObject("to").getString("name"),
                        jsonObject.getJSONObject("quota").getString("name"),
                        fareVOs));

                super.iDownloadListener.onDownloadSuccess(downloadParseResponse);
            }
            else if (responseCode == 404)
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
        return 300;
    }

    public List<FareEnquiryVO> getFareEnquiryVOs()
    {
        return fareEnquiryVOs;
    }
}

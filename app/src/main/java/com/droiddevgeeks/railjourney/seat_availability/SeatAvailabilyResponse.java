package com.droiddevgeeks.railjourney.seat_availability;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.MidStationInfo;
import com.droiddevgeeks.railjourney.models.SeatAvailableVO;
import com.droiddevgeeks.railjourney.models.SeatDateVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vampire on 2017-01-04.
 */

public class SeatAvailabilyResponse extends DownloadParseResponse
{
    private ArrayList<SeatAvailableVO> _seatAvailableVOs;
    private List<SeatDateVO> seatDateVOs;

    public SeatAvailabilyResponse(IDownloadListener iDownloadListener, Context context)
    {
        super(iDownloadListener);
        _seatAvailableVOs = new ArrayList<>();
        seatDateVOs = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        try
        {

            int responseCode = jsonObject.getInt("response_code");
            if (responseCode == 200)
            {
                JSONArray jsonArray = jsonObject.getJSONArray("availability");
                int len = jsonArray.length();
                if (len > 0)
                {
                    for (int i = 0; i < len; i++)
                    {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        seatDateVOs.add(new SeatDateVO(jsonObject1.getString("date"),jsonObject1.getString("status")));
                    }
                }


                _seatAvailableVOs.add( new SeatAvailableVO(
                        jsonObject.getString("train_name"),
                        jsonObject.getString("train_number"),
                        jsonObject.getJSONObject("from").getString("name"),
                        jsonObject.getJSONObject("to").getString("name"),
                        jsonObject.getJSONObject("class").getString("class_name"),
                        jsonObject.getJSONObject("quota").getString("quota_name"),
                        seatDateVOs));
                super.iDownloadListener.onDownloadSuccess(downloadParseResponse);
            }
            else if (responseCode == 204)
            {
                super.iDownloadListener.onDownloadFailed(204, "Empty response ,Not able to fetch required data");
            }
            else if (responseCode == 403)
            {
                super.iDownloadListener.onDownloadFailed(403, "Not able to fetch data, Please try later");
            }
            else
            {
                super.iDownloadListener.onDownloadFailed(510, "Train not scheduled to run on the given date");
            }

        }

        catch (
                JSONException e
                )

        {
            e.printStackTrace();
        }

    }

    @Override
    public int getType()
    {
        return 400;
    }

    public ArrayList<SeatAvailableVO> get_seatAvailableVOs()
    {
        return _seatAvailableVOs;
    }
}

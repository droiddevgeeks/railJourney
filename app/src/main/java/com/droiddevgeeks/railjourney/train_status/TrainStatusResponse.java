package com.droiddevgeeks.railjourney.train_status;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.MidStationInfo;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kishan on 10-10-2016.
 */
public class TrainStatusResponse extends DownloadParseResponse
{
    private ArrayList<TrainStatusVO> _trainStatusVOs;
    private List<MidStationInfo> midStationInfos;

    public TrainStatusResponse(IDownloadListener iDownloadListener)
    {
        super(iDownloadListener);
        _trainStatusVOs = new ArrayList<>();
        midStationInfos = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        try
        {

            int responseCode = jsonObject.getInt("response_code");
            if (responseCode == 200)
            {
                JSONArray jsonArray = jsonObject.getJSONArray("route");
                int len = jsonArray.length();
                if (len > 0)
                {
                    for (int i = 0; i < len; i++)
                    {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        midStationInfos.add(new MidStationInfo(
                                jsonObject1.getJSONObject("station_").getString("name"),
                                jsonObject1.getString("scharr"),
                                jsonObject1.getString("schdep"),
                                jsonObject1.getBoolean("has_arrived"),
                                jsonObject1.getString("latemin")));
                    }
                }


                        _trainStatusVOs.add( new TrainStatusVO(
                                jsonObject.getString("train_number"),
                                jsonObject.getJSONObject("current_station").getJSONObject("station_").getString("name"),
                                jsonObject.getJSONObject("current_station").getBoolean("has_arrived"),
                                jsonObject.getJSONObject("current_station").getString("status"),
                                midStationInfos));
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
        return 500;
    }

    public ArrayList<TrainStatusVO> get_trainStatusVOs()
    {
        return _trainStatusVOs;
    }
}

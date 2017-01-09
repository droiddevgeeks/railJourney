package com.droiddevgeeks.railjourney.train_at_station;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.MidStationInfo;
import com.droiddevgeeks.railjourney.models.TrainAtStationVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationResponse extends DownloadParseResponse
{

    private List<TrainAtStationVO> trainAtStationVOs;
    private Context _context;

    public TrainAtStationResponse(IDownloadListener iDownloadListener, Context context)
    {
        super(iDownloadListener);
        trainAtStationVOs = new ArrayList<>();
        _context = context;
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
       /* String todaysDate = (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString();
        String jsonString = JsonStorage.getJsonFileData(_context, "TrainAt" + todaysDate);
        if (jsonString == null)
        {
            JsonStorage.saveJsonToFile(_context, jsonObject.toString(), "TrainAt" + todaysDate);
        }*/
        try
        {

            int responseCode = jsonObject.getInt("response_code");
            if (responseCode == 200)
            {
                JSONArray jsonArray = jsonObject.getJSONArray("train");
                int len = jsonArray.length();
                if (len > 0)
                {
                    for (int i = 0; i < len; i++)
                    {

                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        trainAtStationVOs.add(new TrainAtStationVO(
                                jsonObject1.getString("scharr"),
                                jsonObject1.getString("delayarr"),
                                jsonObject1.getString("schdep"),
                                jsonObject1.getString("name"),
                                jsonObject1.getString("actdep"),
                                jsonObject1.getString("delaydep"),
                                jsonObject1.getString("actarr"),
                                jsonObject1.getString("number")

                        ));
                    }
                }
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
        return 200;
    }

    public List<TrainAtStationVO> getTrainAtStationVOs()
    {
        return trainAtStationVOs;
    }
}


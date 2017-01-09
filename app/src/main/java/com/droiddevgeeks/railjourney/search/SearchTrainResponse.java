package com.droiddevgeeks.railjourney.search;

import android.content.Context;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.SearchVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchTrainResponse extends DownloadParseResponse
{
    private Context context;
    private List<SearchVO> searchVOs;

    public SearchTrainResponse(IDownloadListener iDownloadListener, Context context)
    {
        super(iDownloadListener);
        this.context = context;
        searchVOs = new ArrayList<>();
    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse)
    {
        String todaysDate = (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString();
       /* String jsonString = JsonStorage.getJsonFileData(context, "TrainBetween" + todaysDate);
        if (jsonString == null)
        {
            JsonStorage.saveJsonToFile(context, jsonObject.toString(), "TrainBetween" + todaysDate);
        }*/
        try
        {

            int res = jsonObject.getInt("response_code");
            if (res == 200)
            {

                JSONArray jsonArray = jsonObject.getJSONArray("train");
                int length = jsonArray.length();
                for (int j = 0; j < length; j++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(j);

                    JSONArray jsonArray1 = jsonObject1.getJSONArray("days");
                    int lenDays = jsonArray1.length();
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int k = 0; k < lenDays; k++)
                    {
                        JSONObject jsonObject2 = jsonArray1.getJSONObject(k);
                        if (jsonObject2.getString("runs").equalsIgnoreCase("Y"))
                        {
                            stringBuilder = stringBuilder.append(jsonObject2.getString("day-code"));
                            if (k == lenDays - 1)
                            {

                            }
                            else
                            {
                                stringBuilder = stringBuilder.append(" , ");
                            }
                        }
                    }

                    String day = stringBuilder.toString();
                    JSONArray jsonArray2 = jsonObject1.getJSONArray("classes");
                    stringBuilder = new StringBuilder();
                    int lenClass = jsonArray2.length();
                    for (int k = 0; k < lenClass; k++)
                    {
                        JSONObject jsonObject2 = jsonArray2.getJSONObject(k);
                        if (jsonObject2.getString("available").equalsIgnoreCase("Y"))
                        {
                            stringBuilder = stringBuilder.append(jsonObject2.getString("class-code"));
                            if (k == lenClass - 1)
                            {

                            }
                            else
                            {
                                stringBuilder = stringBuilder.append(" , ");
                            }
                        }
                    }
                    String travelClass = stringBuilder.toString();
                    searchVOs.add(new SearchVO(jsonObject1.getString("name"), jsonObject1.getString("number"), jsonObject1.getString("travel_time"), jsonObject1.getString("src_departure_time"), jsonObject1.getString("dest_arrival_time"), day, travelClass));
                }
                iDownloadListener.onDownloadSuccess(downloadParseResponse);
            }

            else if (res == 403)
            {
                super.iDownloadListener.onDownloadFailed(403, "Not able to fetch data, Please try later");
            }
            else
            {
                iDownloadListener.onDownloadFailed(204, "Empty response ,Not able to fetch required data");
            }

        }

        catch (JSONException e)
        {
            super.iDownloadListener.onDownloadFailed(204, "Empty response ,Not able to fetch required data");
            e.printStackTrace();
        }

    }

    @Override
    public int getType()
    {
        return 200;
    }

    public List<SearchVO> getSearchVOs()
    {
        return searchVOs;
    }
}

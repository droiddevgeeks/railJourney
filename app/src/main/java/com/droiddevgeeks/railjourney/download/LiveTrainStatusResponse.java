package com.droiddevgeeks.railjourney.download;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;


import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kunal on 10-10-2016.
 */
public class LiveTrainStatusResponse extends DownloadParseResponse
{

    public LiveTrainStatusResponse(IDownloadListener iDownloadListener)
    {
        super(iDownloadListener);

    }

    @Override
    public void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse) {

    }
}

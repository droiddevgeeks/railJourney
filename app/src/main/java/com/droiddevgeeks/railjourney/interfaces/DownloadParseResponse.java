package com.droiddevgeeks.railjourney.interfaces;

import org.json.JSONObject;

/**
 * Created by kunal on 10-10-2016.
 */
public abstract class DownloadParseResponse
{
    public IDownloadListener iDownloadListener;
    public DownloadParseResponse( IDownloadListener iDownloadListener)
    {
        this.iDownloadListener = iDownloadListener;
    }
    public abstract void parseJson(JSONObject jsonObject, DownloadParseResponse downloadParseResponse);
    public abstract int getType();
}

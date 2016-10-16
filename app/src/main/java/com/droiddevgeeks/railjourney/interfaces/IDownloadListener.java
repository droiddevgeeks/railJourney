package com.droiddevgeeks.railjourney.interfaces;

/**
 * Created by kunal on 10-10-2016.
 */
public interface IDownloadListener
{
    void onDownloadSuccess(DownloadParseResponse downloadParseResponse);
    void onDownloadFailed();
}

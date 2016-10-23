package com.droiddevgeeks.railjourney.download;

import android.os.AsyncTask;

import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kunal on 10-10-2016.
 */
public class DownloadJSONAsync extends AsyncTask<String, Integer, String>
{
    private String _downloadURL;
    private WeakReference<DownloadParseResponse> _downloadParseResponse;

    public DownloadJSONAsync(String url, DownloadParseResponse downloadParseResponse)
    {
        _downloadURL = url;
        _downloadParseResponse = new WeakReference<>( downloadParseResponse );
    }

    @Override
    protected String doInBackground(String... params)
    {
        URL url = null;
        try
        {
            url = new URL(_downloadURL);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setDoInput(true);
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String res = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((res = bufferedReader.readLine()) != null)
            {
                stringBuilder = stringBuilder.append(res);
            }
            res = stringBuilder.toString();
            httpURLConnection.disconnect();
            inputStream.close();
            return res;
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String s)
    {
        super.onPostExecute(s);
        if (s != null)
        {
            try
            {
                _downloadURL = null;
                DownloadParseResponse downloadParseResponse =  _downloadParseResponse.get();
                if( downloadParseResponse != null)
                {
                    downloadParseResponse.parseJson(new JSONObject(s), downloadParseResponse);
                }
                _downloadParseResponse = null;
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
    }
}

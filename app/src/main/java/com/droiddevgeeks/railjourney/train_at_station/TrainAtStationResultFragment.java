package com.droiddevgeeks.railjourney.train_at_station;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.TrainAtStationVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationResultFragment extends Fragment implements AdapterView.OnItemClickListener, IDownloadListener, View.OnClickListener
{
    private ListView trainListView;
    private String station, time;
    private TrainAtStationAdapter adapter;
    private List<TrainAtStationVO> list;

    private ProgressDialog waitingProgress;
    private TextView _pageTitle;
    private TextView errorMessage;
    private TextView errorRetry;
    private RelativeLayout rlError;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.train_at_station_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rlError = (RelativeLayout)view.findViewById(R.id.rlError);
        errorMessage = (TextView) view.findViewById(R.id.errorMessage);
        errorRetry = (TextView) view.findViewById(R.id.errorRetry);
        errorRetry.setOnClickListener(this);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("At Station");
        trainListView = (ListView) view.findViewById(R.id.trainListView);
        trainListView.setOnItemClickListener(this);
        callTrainAtStationApi(station, time);
    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog(view.getContext());
        waitingProgress.setCancelable(false);
        waitingProgress.setMessage("Fetching trains information ...");
        waitingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingProgress.setProgress(0);
        waitingProgress.setMax(100);
        waitingProgress.show();
    }

    public void setInputDataForApi(String station, String time)
    {
        this.station = station;
        this.time = time;
    }

    private void callTrainAtStationApi(String station, String time)
    {
        showProgressDialog(trainListView);
        String url = APIUrls.BASE_PREFIX_URL + APIUrls.TRAIN_AT_STATION + "station/" + station + "/hours/" + time + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new TrainAtStationResponse(this, getContext());
        /*String jsonString = JsonStorage.getJsonFileData(getContext(), "TrainAt" + (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString());
        if (jsonString != null)
        {
            try
            {
                downloadParseResponse.parseJson(new JSONObject(jsonString), downloadParseResponse);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }
        else
        {*/
            DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(url, downloadParseResponse);
            downloadJSONAsync.execute();
    //    }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        FragmentManager fm = getFragmentManager();
        TrainAtStationInfo dialog = TrainAtStationInfo.newInstance(list.get(i).getScharr(),list.get(i).getDelayarr(),list.get(i).getSchdep(),list.get(i).getActdep(),list.get(i).getDelaydep(),list.get(i).getActarr());
        dialog.show(fm, "train_at_station_info");

    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        waitingProgress.dismiss();
        if (downloadParseResponse.getType() == 200)
        {
            TrainAtStationResponse trainAtStationResponse;
            list = new ArrayList<>();
            if (downloadParseResponse instanceof TrainAtStationResponse)
            {
                trainAtStationResponse = (TrainAtStationResponse) downloadParseResponse;
                list = trainAtStationResponse.getTrainAtStationVOs();
                adapter = new TrainAtStationAdapter(getContext(), list);
                trainListView.setAdapter(adapter);
            }

        }
    }

    @Override
    public void onDownloadFailed(int errorCode, String message)
    {
        if(waitingProgress !=null)
        {
            waitingProgress.dismiss();
        }
        trainListView.setVisibility(GONE);
        rlError.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
        errorRetry.setText("Retry later!");

    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.errorRetry:
                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();
        }
    }
}

package com.droiddevgeeks.railjourney.search;

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
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.SearchVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchDisplayFragment extends Fragment implements AdapterView.OnItemClickListener, IDownloadListener
{
    private String souceCode;
    private String destinationCode;
    private String doj;
    private ListView trainListView;
    private SearchTrainAdapter adapter;
    private List<SearchVO> list;
    private TextView _pageTitle;
    private ProgressDialog waitingProgress;


    public void setInputDataForApi(String souceCode, String destinationCode, String doj)
    {
        this.souceCode = souceCode;
        this.destinationCode = destinationCode;
        this.doj = doj;
    }

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
        _pageTitle = (TextView) getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Search");
        trainListView = (ListView) view.findViewById(R.id.trainListView);
        trainListView.setOnItemClickListener(this);
        showProgressDialog(view);
        callTrainAtStationApi(souceCode, destinationCode, doj);
    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog(view.getContext());
        waitingProgress.setCancelable(false);
        waitingProgress.setMessage("Fetching train information ...");
        waitingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingProgress.setProgress(0);
        waitingProgress.setMax(100);
        waitingProgress.show();
    }

    private void callTrainAtStationApi(String source, String destination, String doj)
    {
        //   showProgressDialog(trainListView);
        String url = APIUrls.BASE_PREFIX_URL + APIUrls.TRAIN_BTW_STN + "source/" + source + "/dest/" + destination + "/date/" + doj + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new SearchTrainResponse(this, getContext());
       /* String jsonString = JsonStorage.getJsonFileData(getContext(), "TrainBetween" + (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString());
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
        // }

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        FragmentManager fm = getFragmentManager();
        SearchTrainInfo dialog = SearchTrainInfo.newInstance(list.get(i).getTrainName(), list.get(i).getTrainArrival(), list.get(i).getTrainDeparture(), list.get(i).getTravelTime(), list.get(i).getDays(), list.get(i).getClassInfo());
        dialog.show(fm, "search_train_info");
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        waitingProgress.dismiss();
        if (downloadParseResponse.getType() == 200)
        {
            SearchTrainResponse searchTrainResponse;
            list = new ArrayList<>();
            if (downloadParseResponse instanceof SearchTrainResponse)
            {
                searchTrainResponse = (SearchTrainResponse) downloadParseResponse;
                list = searchTrainResponse.getSearchVOs();
                Collections.sort(list, new Comparator<SearchVO>()
                {
                    @Override
                    public int compare(SearchVO searchVO, SearchVO t1)
                    {
                        return searchVO.getTrainArrival().compareTo(t1.getTrainArrival());
                    }
                });
                adapter = new SearchTrainAdapter(getContext(), list);
                trainListView.setAdapter(adapter);
            }

        }
    }

    @Override
    public void onDownloadFailed(int errorCode, String message)
    {
        if(waitingProgress!=null)
        {
            waitingProgress.dismiss();
        }
    }
}

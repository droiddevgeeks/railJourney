package com.droiddevgeeks.railjourney.seat_availability;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.SeatAvailableVO;


import com.droiddevgeeks.railjourney.utils.APIUrls;
import com.droiddevgeeks.railjourney.utils.Utilities;

import java.util.ArrayList;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2017-01-03.
 */

public class SeatAvailabilityResultFragment extends Fragment implements IDownloadListener, View.OnClickListener
{
    private ArrayList<SeatAvailableVO> _seatAvailableVOs;
    private TextView txtTrainName, txtTrainNumber, fromSource, toDestination, travelClass, travelQuota;
    private ListView midStationList;
    private SeatAvailbilityAdapter adapter;
    private TextView _pageTitle;
    private RelativeLayout rlError;
    private TextView errorMessage;
    private TextView errorRetry;
    private RelativeLayout rlInfo;
    private ProgressDialog waitingProgress;
    private String trainNumber, source, dest, classTravel, quota, doj;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.seat_availabily_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        rlError = (RelativeLayout) view.findViewById(R.id.rlError);
        errorMessage = (TextView) view.findViewById(R.id.errorMessage);
        errorRetry = (TextView) view.findViewById(R.id.errorRetry);
        errorRetry.setOnClickListener(this);
        rlInfo = (RelativeLayout) view.findViewById(R.id.rlInfo);
        _pageTitle = (TextView) getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Seat");
        txtTrainName = (TextView) view.findViewById(R.id.txtTrainName);
        txtTrainNumber = (TextView) view.findViewById(R.id.txtTrainNumber);
        fromSource = (TextView) view.findViewById(R.id.FromSource);
        toDestination = (TextView) view.findViewById(R.id.ToDestination);
        travelClass = (TextView) view.findViewById(R.id.travelClass);
        travelQuota = (TextView) view.findViewById(R.id.travelQuota);
        midStationList = (ListView) view.findViewById(R.id.listViewTrainAvail);
        showProgressDialog(view);
        callApiForData(trainNumber, source, dest, classTravel, quota, doj);
    }

    public void setInputDataForApi(String trainNumber, String source, String dest, String travelClass, String quota, String doj)
    {
        this.trainNumber  = trainNumber;
        this.source = source;
        this.dest = dest;
        this.classTravel = travelClass;
        this.quota = quota;
        this.doj = doj;
    }

    private void callApiForData(String trainNumber, String source, String dest, String travelClass, String quota, String doj)
    {
        if (Utilities.isConnectedToInternet(getContext()))
        {
            String fareCheckUrl = APIUrls.BASE_PREFIX_URL + APIUrls.SEAT_AVAIL + "train/" + trainNumber + "/source/" + source + "/dest/" + dest + "/date/" + doj + "/class/" + travelClass + "/quota/" + quota + APIUrls.BASE_SUFFIX_URL;
            DownloadParseResponse downloadParseResponse = new SeatAvailabilyResponse(this, getContext());
            DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(fareCheckUrl, downloadParseResponse);
            downloadJSONAsync.execute();
        }
        else
        {
            this.onDownloadFailed(101, "Please check Internet connection");
        }

    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog(view.getContext());
        waitingProgress.setCancelable(false);
        waitingProgress.setMessage("Fetching Seats information ...");
        waitingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingProgress.setProgress(0);
        waitingProgress.setMax(100);
        waitingProgress.show();
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        waitingProgress.dismiss();
        rlError.setVisibility(GONE);
        rlInfo.setVisibility(View.VISIBLE);

        SeatAvailabilyResponse seatAvailabilyResponse;
        if (downloadParseResponse instanceof SeatAvailabilyResponse)
        {
            seatAvailabilyResponse = (SeatAvailabilyResponse) downloadParseResponse;
            if (seatAvailabilyResponse.getType() == 400)
            {
                _seatAvailableVOs = seatAvailabilyResponse.get_seatAvailableVOs();
                if (_seatAvailableVOs != null && _seatAvailableVOs.size() > 0)
                {
                    setLayoutData();
                    setSeatAvalibilityAdapter();
                }

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
        rlError.setVisibility(View.VISIBLE);
        errorMessage.setText(message);
        errorRetry.setText("Retry");
        if (errorCode == 101)
        {
            Toast.makeText(getContext(), "Please check internet Connection", Toast.LENGTH_SHORT).show();
        }

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

    private void setLayoutData()
    {
        if (_seatAvailableVOs != null)
        {
            txtTrainName.setText(_seatAvailableVOs.get(0).getTrainName());
            txtTrainNumber.setText(_seatAvailableVOs.get(0).getTrainNumber());
            fromSource.setText(_seatAvailableVOs.get(0).getSource());
            toDestination.setText(_seatAvailableVOs.get(0).getDestination());
            travelClass.setText(_seatAvailableVOs.get(0).getTravelClass());
            travelQuota.setText(_seatAvailableVOs.get(0).getQuota());

        }
    }

    private void setSeatAvalibilityAdapter()
    {
        adapter = new SeatAvailbilityAdapter(getContext(), _seatAvailableVOs.get(0).getSeatDateVOs());
        midStationList.setAdapter(adapter);
    }

}

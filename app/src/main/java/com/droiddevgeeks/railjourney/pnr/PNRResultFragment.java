package com.droiddevgeeks.railjourney.pnr;

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
import com.droiddevgeeks.railjourney.canceltrain.CancelTrainResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.fragments.HomeScreenFragment;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.PnrDataVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2016-10-14.
 */

public class PNRResultFragment extends Fragment implements IDownloadListener
{

    private List<PnrDataVO> _list;
    private ProgressDialog waitingProgress;
    private String _enterPNR;
    private TextView _pageTitle;
    private TextView trainSource;
    private TextView trainDestination;
    private TextView yourBoarding;
    private TextView yourDestination;
    private TextView trainNumber;
    private TextView doj;
    private TextView errorMessage;
    private TextView errorRetry;
    private ListView passengerList;
    private RelativeLayout rlError;
    private RelativeLayout rlInfo;
    private PassengerListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.pnr_display_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("PNR");
        rlError = (RelativeLayout) view.findViewById(R.id.rlError);
        rlInfo = (RelativeLayout) view.findViewById(R.id.rlInfo);
        trainSource = (TextView)view.findViewById(R.id.txtSourceStation);
        trainDestination = (TextView)view.findViewById(R.id.txtDestinationStation);
        yourBoarding = (TextView)view.findViewById(R.id.txtPassengerSourceStation);
        yourDestination = (TextView)view.findViewById(R.id.txtPassengerDestinationStation);
        trainNumber = (TextView)view.findViewById(R.id.txtTrainNumber);
        doj = (TextView)view.findViewById(R.id.txtDateOfJourney);
        errorMessage = (TextView)view.findViewById(R.id.errorMessage);
        errorRetry = (TextView)view.findViewById(R.id.errorRetry);
        passengerList = (ListView)view.findViewById(R.id.listViewPnrStatus);
        showProgressDialog(view);
        callApiForData();
    }

    public void sendPNRNumber(String pnr)
    {
        _enterPNR = pnr;
    }

    private void callApiForData()
    {
        String pnrCheckUrl = APIUrls.BASE_PREFIX_URL + APIUrls.PNR_URL + _enterPNR + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new PNRDataResponse(this, getContext());
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(pnrCheckUrl, downloadParseResponse);
        downloadJSONAsync.execute();
    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog(view.getContext());
        waitingProgress.setCancelable(false);
        waitingProgress.setMessage("Fetching PNR information ...");
        waitingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingProgress.setProgress(0);
        waitingProgress.setMax(100);
        waitingProgress.show();
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        PNRDataResponse pnrDataResponse;
        if (downloadParseResponse instanceof PNRDataResponse)
        {
            pnrDataResponse = (PNRDataResponse) downloadParseResponse;
            _list = pnrDataResponse.getPNRInfoList();
            if (_list != null)
            {
                waitingProgress.dismiss();
                rlError.setVisibility(GONE);
                rlInfo.setVisibility(View.VISIBLE);
                setPNRData();
                setPassengerList();
            }
            else
            {
                Toast.makeText(getContext(), "Please check internet Connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onDownloadFailed(int errorCode , String message)
    {
        if(waitingProgress!=null)
        {
            waitingProgress.dismiss();
        }
        rlError.setVisibility(View.VISIBLE);
        rlInfo.setVisibility(View.GONE);

        switch (errorCode)
        {
            case 404:
                errorMessage.setText("Service Down , Please try later");
                errorRetry.setText("Please Retry later");
                errorRetry.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                        fm.popBackStack();
                    }
                });
                break;
            case 410:
                errorMessage.setText("Flushed PNR / PNR not yet generated");
                errorRetry.setText("Back to Home");
                errorRetry.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                        fm.popBackStack();
                    }
                });
                break;
            case 403:
                errorMessage.setText("Service timeout, Please try again");
                errorRetry.setText("Retry later!");
                errorRetry.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        FragmentManager fm = getActivity().getSupportFragmentManager();
                        fm.popBackStack();
                        fm.popBackStack();
                    }
                });
                break;
            case 204:
                errorMessage.setText("Service timeout, Please try again");
                errorRetry.setText("Retry later!");
                errorRetry.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View view)
                    {
                        callApiForData();
                    }
                });
        }


    }

    public void setPNRData()
    {
        PnrDataVO dataVo = _list.get(0);
        trainSource.setText(dataVo.getTrainSourceStation());
        trainDestination.setText(dataVo.getTrainDestinationStation());
        yourBoarding.setText(dataVo.getYourBoardingStation());
        yourDestination.setText(dataVo.getYourLastStation());
        trainNumber.setText(dataVo.getTrainNumber());
        doj.setText(dataVo.getDateOfJourney());
    }


    private void setPassengerList()
    {
        adapter = new PassengerListAdapter(getContext() , _list.get(0).getPassengerVOs());
        passengerList.setAdapter(adapter);
    }


}

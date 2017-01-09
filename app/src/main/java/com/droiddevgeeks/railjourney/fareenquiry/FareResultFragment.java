package com.droiddevgeeks.railjourney.fareenquiry;

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
import com.droiddevgeeks.railjourney.models.FareEnquiryVO;
import com.droiddevgeeks.railjourney.models.FareVO;
import com.droiddevgeeks.railjourney.models.PnrDataVO;
import com.droiddevgeeks.railjourney.pnr.PNRDataResponse;
import com.droiddevgeeks.railjourney.pnr.PassengerListAdapter;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import java.util.List;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2016-10-14.
 */

public class FareResultFragment extends Fragment implements IDownloadListener
{

    private List<FareVO> fareVOs;
    private List<FareEnquiryVO> fareEnquiryVOs;
    private ProgressDialog waitingProgress;
    private TextView trainSource;
    private TextView trainDestination;
    private TextView trainNumber;
    private TextView qutoa;
    private TextView errorMessage;
    private TextView errorRetry;
    private RelativeLayout rlError;
    private RelativeLayout rlInfo;
    private ListView fareListView;
    private FareListAdapter adapter;
    private TextView _pageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fare_result_page_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Fare");
        rlError = (RelativeLayout) view.findViewById(R.id.rlError);
        rlInfo = (RelativeLayout) view.findViewById(R.id.rlInfo);
        trainSource = (TextView) view.findViewById(R.id.txtSourceStation);
        trainDestination = (TextView) view.findViewById(R.id.txtDestinationStation);
        trainNumber = (TextView) view.findViewById(R.id.txtTrainNumber);
        qutoa = (TextView) view.findViewById(R.id.txtBookingQuotaValue);


        errorMessage = (TextView) view.findViewById(R.id.errorMessage);
        errorRetry = (TextView) view.findViewById(R.id.errorRetry);
        fareListView = (ListView) view.findViewById(R.id.listViewTrainFare);
        showProgressDialog(view);
    }

    public void setInputDataForApi(String trainNumber , String source, String dest ,String age ,String quota , String doj)
    {
        callApiForData(trainNumber,source,dest,age,quota,doj);
    }

    private void callApiForData(String trainNumber , String source, String dest ,String age ,String quota , String doj)
    {
        String fareCheckUrl = APIUrls.BASE_PREFIX_URL + APIUrls.FARE_ENQUIRY +
                "train/"+trainNumber + "/source/" + source + "/dest/"+ dest + "/age/"+ age + "/quota/"+ quota +"/doj/"+doj
                + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new FareEnquiryResponse(this, getContext());
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(fareCheckUrl, downloadParseResponse);
        downloadJSONAsync.execute();
    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog(view.getContext());
        waitingProgress.setCancelable(false);
        waitingProgress.setMessage("Fetching Fare information ...");
        waitingProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        waitingProgress.setProgress(0);
        waitingProgress.setMax(100);
        waitingProgress.show();
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        FareEnquiryResponse fareEnquiryResponse;
        if (downloadParseResponse instanceof FareEnquiryResponse)
        {
            fareEnquiryResponse = (FareEnquiryResponse) downloadParseResponse;
            fareEnquiryVOs = fareEnquiryResponse.getFareEnquiryVOs();
            if (fareEnquiryVOs != null)
            {
                waitingProgress.dismiss();
                rlError.setVisibility(GONE);
                rlInfo.setVisibility(View.VISIBLE);
                setTrainData();
                if(fareEnquiryVOs.get(0).getFareVOList().size()>0)
                {
                    setFareList();
                }
            }
            else
            {
                Toast.makeText(getContext(), "Please check internet Connection", Toast.LENGTH_SHORT).show();
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
        }


    }

    public void setTrainData()
    {
        FareEnquiryVO dataVo = fareEnquiryVOs.get(0);
        trainSource.setText(dataVo.getSource());
        trainDestination.setText(dataVo.getDestination());
        trainNumber.setText(dataVo.getTrainNumber());
        qutoa.setText(dataVo.getBookingQuota());
    }


    private void setFareList()
    {
        adapter = new FareListAdapter(getContext(), fareEnquiryVOs.get(0).getFareVOList());
        fareListView.setAdapter(adapter);
    }


}

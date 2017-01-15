package com.droiddevgeeks.railjourney.trainroute;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoCompleteAdapter;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.models.TrainRouteVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;
import com.droiddevgeeks.railjourney.utils.Utilities;

import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2016-10-15.
 */

public class TrainRouteFragment extends Fragment implements View.OnClickListener, IDownloadListener, AdapterView.OnItemClickListener
{
    private TextView _trainRoute;
    private ImageView clearTrain;
    private EditText _txtTrainNameNumber;
    private ListView trainRouteList;

    private List<AutoCompleteVO> _autoCompleteList;
    private AutoCompleteAdapter _autoCompleteAdapter;

    private List<TrainRouteVO> _trainRouteList;
    private boolean stopTextWatcher = false;
    private String trainNumber;
    private TextView _pageTitle;
    private RelativeLayout rlError;
    private TextView errorMessage;
    private TextView errorRetry;
    private LinearLayout llInfo;
    private ProgressDialog waitingProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.train_route_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        rlError = (RelativeLayout) view.findViewById(R.id.rlError);
        errorMessage = (TextView) view.findViewById(R.id.errorMessage);
        errorRetry = (TextView) view.findViewById(R.id.errorRetry);
        errorRetry.setOnClickListener(this);
        llInfo = (LinearLayout) view.findViewById(R.id.rlPNRCheckLayout);
        _pageTitle = (TextView) getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Route");
        _txtTrainNameNumber = (EditText) view.findViewById(R.id.edtStationName);
        _txtTrainNameNumber.setTextColor(Color.BLACK);
        _txtTrainNameNumber.addTextChangedListener(txtChecker);
        _txtTrainNameNumber.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if (_txtTrainNameNumber.getText().length() < 3)
                    {
                        trainRouteList.setVisibility(GONE);
                        stopTextWatcher = false;
                    }
                    else
                    {
                        String text = _txtTrainNameNumber.getText().toString();
                        if (_autoCompleteAdapter != null)
                        {
                            _autoCompleteAdapter.getFilter().filter(text);
                        }
                    }
                }
                return false;
            }
        });

        trainRouteList = (ListView) view.findViewById(R.id.stationAutoList);
        trainRouteList.setOnItemClickListener(this);/*new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                _txtTrainNameNumber.setText(_autoCompleteList.get(i).getName());
                trainNumber = _autoCompleteList.get(i).getCode();
                trainRouteList.setVisibility(GONE);
            }
        });*/

        clearTrain = (ImageView) view.findViewById(R.id.imgClearTrain);
        clearTrain.setOnClickListener(this);

        _trainRoute = (TextView) view.findViewById(R.id.btnTrainRoute);
        _trainRoute.setOnClickListener(this);

    }

    TextWatcher txtChecker = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            if (count < 3)
            {
                trainRouteList.setVisibility(GONE);
                stopTextWatcher = false;
            }
            if (_txtTrainNameNumber.getText().length() == 3)
            {
                if (stopTextWatcher)
                {

                }
                else
                {
                    callAutoSuggestAPI(_txtTrainNameNumber.getText().toString());
                }

            }
            if (count > 3)
            {
                String text = _txtTrainNameNumber.getText().toString().toLowerCase(Locale.getDefault());
                if (_autoCompleteAdapter != null)
                {
                    _autoCompleteAdapter.getFilter().filter(text);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = s.toString().trim();
            if (_autoCompleteAdapter != null)
            {
                _autoCompleteAdapter.getFilter().filter(text);
            }
        }
    };


    private void callTrainRouteAPI(String url)
    {
        if (Utilities.isConnectedToInternet(getContext()))
        {
            DownloadParseResponse downloadParseResponse = new TrainRouteResponse(this);
            DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(url, downloadParseResponse);
            downloadJSONAsync.execute();
        }
        else
        {
            this.onDownloadFailed(101, "Please connect to working internet connection");
        }
    }


    private void callAutoSuggestAPI(String keyword)
    {

        String url = APIUrls.BASE_PREFIX_URL + APIUrls.AUTO_SUGGEST_LIST + keyword + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new AutoSuggestResponse(this, getContext());
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(url, downloadParseResponse);
        downloadJSONAsync.execute();

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnTrainRoute:
                if (checkInputValidation())
                {
                    Utilities.hideSoftKeyboard(getContext(), v);
                    llInfo.setVisibility(GONE);
                    showProgressDialog(v);
                    String trainRouteURL = APIUrls.BASE_PREFIX_URL + APIUrls.TRAIN_ROUTE + trainNumber + APIUrls.BASE_SUFFIX_URL;
                    callTrainRouteAPI(trainRouteURL);
                }
                break;
            case R.id.imgClearTrain:
                _txtTrainNameNumber.getText().clear();
                trainRouteList.setVisibility(GONE);
                stopTextWatcher = false;
                break;
            case R.id.errorRetry:
                llInfo.setVisibility(View.VISIBLE);
                rlError.setVisibility(GONE);

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

    private boolean checkInputValidation()
    {
        if (_txtTrainNameNumber.getText().toString().length() >= 5)
        {
            return true;
        }
        else
        {
            Toast.makeText(getContext(), "Please enter train number or name", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {

        if (downloadParseResponse.getType() == 200)
        {
            AutoSuggestResponse autoSuggestResponse;
            if (downloadParseResponse instanceof AutoSuggestResponse)
            {
                autoSuggestResponse = (AutoSuggestResponse) downloadParseResponse;
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();
                if (_autoCompleteList != null)
                {
                    trainRouteList.setVisibility(View.VISIBLE);
                    _autoCompleteAdapter = new AutoCompleteAdapter(_autoCompleteList);
                    trainRouteList.setAdapter(_autoCompleteAdapter);
                    stopTextWatcher = true;
                }
            }
        }
        else if (downloadParseResponse.getType() == 600)
        {
            waitingProgress.dismiss();
            TrainRouteResponse trainRouteResponse;
            if (downloadParseResponse instanceof TrainRouteResponse)
            {
                trainRouteResponse = (TrainRouteResponse) downloadParseResponse;
                _trainRouteList = trainRouteResponse.getTrainRouteList();
                if (_trainRouteList != null)
                {
                    TrainRouteResultFragment trainRouteResultFragment = new TrainRouteResultFragment();
                    trainRouteResultFragment.setTrainRouteListData(_trainRouteList);
                    getFragmentManager().beginTransaction().replace(R.id.container, trainRouteResultFragment).addToBackStack(null).commit();

                }

            }
        }


    }

    @Override
    public void onDownloadFailed(int errorCode, String message)
    {
        if (waitingProgress != null)
        {
            waitingProgress.dismiss();
        }
        if (errorCode == 100)
        {
            rlError.setVisibility(View.GONE);
            llInfo.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), "Not able to get data", Toast.LENGTH_SHORT).show();
        }
        else
        {
            rlError.setVisibility(View.VISIBLE);
            errorMessage.setText(message);
            errorRetry.setText("Retry");
        }


    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        AutoCompleteVO model = (AutoCompleteVO) adapterView.getItemAtPosition(i);
        _txtTrainNameNumber.setText(model.getName());
        trainNumber = model.getCode();
        trainRouteList.setVisibility(GONE);
    }
}

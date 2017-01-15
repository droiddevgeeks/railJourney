package com.droiddevgeeks.railjourney.train_at_station;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Keep;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.MainActivity;
import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoCompleteAdapter;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestStationResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.fareenquiry.FareResultFragment;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.view.View.GONE;

/**
 * Created by Vampire on 2016-12-31.
 */

public class TrainAtStationInputFragment extends Fragment implements View.OnClickListener, IDownloadListener
{
    private EditText station;
    private ListView listViewStation;
    private Spinner timeRange;
    private ImageView clearTextImage;
    private TextView trainAtStation;
    private boolean stopTextWatcher = false;
    private List<AutoCompleteVO> _autoCompleteList;
    private AutoCompleteAdapter _autoCompleteAdapter;
    private String time;
    private String stationCode;
    private TextView _pageTitle;

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.train_at_station, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("At Station");
        station = (EditText) view.findViewById(R.id.edtStationName);
        station.addTextChangedListener(_textStationWatcher);
        station.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if(station.getText().length()<3)
                    {
                        listViewStation.setVisibility(GONE);
                        stopTextWatcher = false;
                    }
                    else
                    {
                        String text = station.getText().toString();
                        if (_autoCompleteAdapter != null)
                        {
                            _autoCompleteAdapter.getFilter().filter(text);
                        }
                    }
                }
                return false;
            }
        });

        listViewStation = (ListView)view.findViewById(R.id.stationAutoList);
        listViewStation.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                AutoCompleteVO model = (AutoCompleteVO) adapterView.getItemAtPosition(i);
                listViewStation.setVisibility(GONE);
                station.setText(model.getName());
                stationCode = model.getCode();
            }
        });

        clearTextImage = (ImageView) view.findViewById(R.id.imgClearTrain);
        timeRange = (Spinner) view.findViewById(R.id.spinnerTimeRange);
        trainAtStation = (TextView) view.findViewById(R.id.btnTrainStatus);
        timeRange.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                String[] time_range = getContext().getResources().getStringArray(R.array.time_range_vlaue);
                time = time_range[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                time = "" + 2;
            }
        });

        clearTextImage.setOnClickListener(this);
        trainAtStation.setOnClickListener(this);

        initMobileAds(view);

    }

    private void initMobileAds(View view)
    {
        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                /*.addTestDevice("4d0a3b4bca93c000")
                .addTestDevice("bb23ad1a6aab7f0")*/
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume()
    {
        super.onResume();
        if (mAdView != null)
        {
            mAdView.resume();
        }
    }

    @Override
    public void onPause()
    {
        super.onPause();
        if (mAdView != null)
        {
            mAdView.pause();
        }
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (mAdView != null)
        {
            mAdView.destroy();
        }
    }


    TextWatcher _textStationWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {

            if(count < 3)
            {
                listViewStation.setVisibility(GONE);
                stopTextWatcher = false;
            }
            if (station.getText().length() == 3)
            {
                if (stopTextWatcher)
                {

                }
                else
                {
                    callStationAutoSuggestAPI(station.getText().toString());
                }

            }
            if (count > 3)
            {
                String text = station.getText().toString().toLowerCase(Locale.getDefault());
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

    private void callStationAutoSuggestAPI(String stationKeyword)
    {

        String url = APIUrls.BASE_PREFIX_URL + APIUrls.AUTO_SUGGEST_STATION + stationKeyword + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new AutoSuggestStationResponse(this, getContext());
       /* String jsonString = JsonStorage.getJsonFileData(getContext(), "AutoSuggestStation" + (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString());
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
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.imgClearTrain:
                station.getText().clear();
                stopTextWatcher = false;
                break;
            case R.id.btnTrainStatus:
                if(validateInput())
                {
                    ((MainActivity)getActivity()).setAtstationCheck();
                    TrainAtStationResultFragment trainAtStationResultFragment = new TrainAtStationResultFragment();
                    trainAtStationResultFragment.setInputDataForApi(stationCode,time);
                    getFragmentManager().beginTransaction().replace(R.id.container, trainAtStationResultFragment).addToBackStack(null).commit();
                }
                else
                {
                    Toast.makeText(getContext(), "Please enter value", Toast.LENGTH_SHORT).show();
                }

        }

    }

    private boolean validateInput()
    {

        if(station.getText().toString().equalsIgnoreCase("")||station.getText().length() <=3)
        {
            showKeyBoard(station);
            return false;
        }
        return true;
    }

    private void  showKeyBoard(View view)
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        _autoCompleteList = new ArrayList<>();
        AutoSuggestStationResponse autoSuggestResponse;
        if (downloadParseResponse instanceof AutoSuggestStationResponse)
        {
            autoSuggestResponse = (AutoSuggestStationResponse) downloadParseResponse;
            _autoCompleteList.clear();
            _autoCompleteList = autoSuggestResponse.getAutoSuggestList();
            if (_autoCompleteList != null)
            {

                listViewStation.setVisibility(View.VISIBLE);
                _autoCompleteAdapter = new AutoCompleteAdapter( _autoCompleteList);
                listViewStation.setAdapter(_autoCompleteAdapter);
                stopTextWatcher = true;

            }
        }

    }

    @Override
    public void onDownloadFailed(int errorCode, String message)
    {

    }
}

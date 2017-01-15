package com.droiddevgeeks.railjourney.search;

import android.app.DatePickerDialog;
import android.content.Context;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoCompleteAdapter;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static android.view.View.GONE;

/**
 * Created by kunal.sale on 10/20/2016.
 */

public class SearchInputFragment extends Fragment implements View.OnClickListener, IDownloadListener, DatePickerDialog.OnDateSetListener
{
    private EditText sourceStation;
    private EditText destinationStation;
    private ImageView sourceClear;
    private ImageView destinationClear;
    private RelativeLayout rlCalender;
    private TextView doj;
    private TextView getTrain;
    private TextView _pageTitle;
    private ListView sourceAutoList;
    private ListView destinationAutoList;

    private String sourceCode, destinationCode;
    private int day, month, year;
    private String date;
    private StringBuilder sb;
    private boolean stopSourceWatcher = false;
    private boolean isSource = false;
    private boolean isDestination = false;
    private boolean stopDestinationWatcher = false;

    private List<AutoCompleteVO> _autoCompleteList;
    private AutoCompleteAdapter _autoCompleteSourceAdapter;
    private AutoCompleteAdapter _autoCompleteDestinationAdapter;

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {

        return inflater.inflate(R.layout.search_input_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView) getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Search");
        sourceStation = (EditText) view.findViewById(R.id.edtFromStation);
        sourceStation.addTextChangedListener(_textSourceWatcher);
        sourceStation.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if (sourceStation.getText().length() < 3)
                    {
                        sourceAutoList.setVisibility(GONE);
                        stopSourceWatcher = false;
                    }
                    else
                    {
                        String text = sourceStation.getText().toString();
                        if (_autoCompleteSourceAdapter != null)
                        {
                            _autoCompleteSourceAdapter.getFilter().filter(text);
                        }
                    }


                }
                return false;
            }
        });

        sourceAutoList = (ListView) view.findViewById(R.id.sourceSuggestList);
        sourceAutoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                AutoCompleteVO model = (AutoCompleteVO) adapterView.getItemAtPosition(i);
                sourceCode = model.getCode();
                sourceStation.setText(model.getName());
                sourceAutoList.setVisibility(View.GONE);
            }
        });

        destinationStation = (EditText) view.findViewById(R.id.edtToStation);
        destinationStation.addTextChangedListener(_textDestinationWatcher);
        destinationStation.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if (destinationStation.getText().length() < 3)
                    {
                        destinationAutoList.setVisibility(GONE);
                        stopDestinationWatcher = false;
                    }
                    else
                    {
                        String text = destinationStation.getText().toString();
                        if (_autoCompleteDestinationAdapter != null)
                        {
                            _autoCompleteDestinationAdapter.getFilter().filter(text);
                        }
                    }
                }
                return false;
            }
        });

        destinationAutoList = (ListView) view.findViewById(R.id.destinationSuggestList);
        destinationAutoList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                AutoCompleteVO model = (AutoCompleteVO) adapterView.getItemAtPosition(i);
                destinationCode = model.getCode();
                destinationStation.setText(model.getName());
                destinationAutoList.setVisibility(View.GONE);
            }
        });

        sourceClear = (ImageView) view.findViewById(R.id.imgClearSource);
        sourceClear.setOnClickListener(this);
        destinationClear = (ImageView) view.findViewById(R.id.imgClearDestination);
        destinationClear.setOnClickListener(this);


        rlCalender = (RelativeLayout) view.findViewById(R.id.frameEditBoxDOJ);
        rlCalender.setOnClickListener(this);
        doj = (TextView) view.findViewById(R.id.txtDate);

        getTrain = (TextView) view.findViewById(R.id.btnTrainStatus);
        getTrain.setOnClickListener(this);

        initMobileAds(view);


    }

    private void initMobileAds(View view)
    {
        mAdView = (AdView)view.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
               /* .addTestDevice("4d0a3b4bca93c000")
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

    TextWatcher _textSourceWatcher = new TextWatcher()
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
                sourceAutoList.setVisibility(GONE);
                stopSourceWatcher = false;
            }
            if (sourceStation.getText().length() == 3)
            {
                if (stopSourceWatcher)
                {

                }
                else
                {
                    isSource = true;
                    destinationAutoList.setVisibility(GONE);
                    callStationAutoSuggestAPI(sourceStation.getText().toString());
                }

            }
            if (count > 3)
            {
                String text = sourceStation.getText().toString().toLowerCase(Locale.getDefault());
                if (_autoCompleteSourceAdapter != null)
                {
                    _autoCompleteSourceAdapter.getFilter().filter(text);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = s.toString().trim();
            if (_autoCompleteSourceAdapter != null)
            {
                _autoCompleteSourceAdapter.getFilter().filter(text);
            }
        }
    };

    TextWatcher _textDestinationWatcher = new TextWatcher()
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
                destinationAutoList.setVisibility(GONE);
                stopDestinationWatcher = false;
            }
            if (destinationStation.getText().length() == 3)
            {
                if (stopDestinationWatcher)
                {

                }
                else
                {
                    isDestination = true;
                    sourceAutoList.setVisibility(GONE);
                    callStationAutoSuggestAPI(destinationStation.getText().toString());

                }

            }
            if (count > 3)
            {
                String text = destinationStation.getText().toString().toLowerCase(Locale.getDefault());
                if (_autoCompleteDestinationAdapter != null)
                {
                    _autoCompleteDestinationAdapter.getFilter().filter(text);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = s.toString().trim();
            if (_autoCompleteDestinationAdapter != null)
            {
                _autoCompleteDestinationAdapter.getFilter().filter(text);
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgClearSource:
                sourceStation.getText().clear();
                sourceAutoList.setVisibility(GONE);
                stopSourceWatcher = false;
                isSource = true;
                break;
            case R.id.imgClearDestination:
                destinationStation.getText().clear();
                destinationAutoList.setVisibility(GONE);
                stopDestinationWatcher = false;
                isDestination = true;
                break;
            case R.id.frameEditBoxDOJ:
                selectDateofJourney();
                break;
            case R.id.btnTrainStatus:
                fetchFareFromServer();
        }

    }

    private void fetchFareFromServer()
    {
        if (checkInputValidation())
        {

            SearchDisplayFragment searchDisplayFragment = new SearchDisplayFragment();
            searchDisplayFragment.setInputDataForApi(sourceCode, destinationCode, "" + date);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, searchDisplayFragment).addToBackStack(null).commit();
        }
        else
        {
            Toast.makeText(getContext(), "Please enter value", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkInputValidation()
    {
        if (sourceStation.getText().toString().equalsIgnoreCase(""))
        {
            showKeyBoard(sourceStation);
            return false;
        }
        if (destinationStation.getText().toString().equalsIgnoreCase(""))
        {
            showKeyBoard(destinationStation);
            return false;
        }

        if (doj.getText() == "")
        {
            showKeyBoard(rlCalender);
            return false;
        }

        return true;

    }

    private void showKeyBoard(View view)
    {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    private void selectDateofJourney()
    {
        sb = new StringBuilder();
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        DatePickerDialog dialog = new DatePickerDialog(getContext(),
                this,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        calendar.set(calendar.get(Calendar.YEAR) + 1, 11, 31);//Year,Mounth -1,Day
        dialog.getDatePicker().setMaxDate(calendar.getTimeInMillis());
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2)
    {
        year = i;
        month = i1 + 1;
        day = i2;

        if (day < 10)
        {
            sb.append("0" + day);
        }
        else
        {
            sb.append(day);
        }
        sb.append("-");
        if (month < 10)
        {
            sb.append("0" + month);
        }
        else
        {
            sb.append(month);
        }
        date = sb.toString();
        sb.append("-");
        sb.append(year);
        doj.setText(sb.toString());

    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        _autoCompleteList = new ArrayList<>();
        if (downloadParseResponse.getType() == 250)
        {
            AutoSuggestStationResponse autoSuggestResponse;
            if (downloadParseResponse instanceof AutoSuggestStationResponse)
            {
                autoSuggestResponse = (AutoSuggestStationResponse) downloadParseResponse;
                _autoCompleteList.clear();
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();
                if (_autoCompleteList != null)
                {
                    if (isSource)
                    {
                        sourceAutoList.setVisibility(View.VISIBLE);
                        _autoCompleteSourceAdapter = new AutoCompleteAdapter(_autoCompleteList);
                        sourceAutoList.setAdapter(_autoCompleteSourceAdapter);
                        stopSourceWatcher = true;
                        isSource = false;
                    }
                    else if (isDestination)
                    {
                        destinationAutoList.setVisibility(View.VISIBLE);
                        _autoCompleteDestinationAdapter = new AutoCompleteAdapter(_autoCompleteList);
                        destinationAutoList.setAdapter(_autoCompleteDestinationAdapter);
                        stopDestinationWatcher = true;
                        isDestination = false;
                    }

                }
            }
        }
    }


    @Override
    public void onDownloadFailed(int errorCode, String message)
    {

    }

}

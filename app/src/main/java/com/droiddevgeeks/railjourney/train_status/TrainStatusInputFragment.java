package com.droiddevgeeks.railjourney.train_status;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.MainActivity;
import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoCompleteAdapter;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;
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

import static android.view.View.GONE;

/**
 * Created by kishan on 11-10-2016.
 */
public class TrainStatusInputFragment extends Fragment implements View.OnClickListener, IDownloadListener
{
    private final String TAG = TrainStatusInputFragment.class.getSimpleName();
    private EditText _edtTrainNumber;
    private ListView trainNameSuggestList;
    private ImageView _imgClearTrain;
    private ImageView _imgCalenderDate;
    private TextView _txtDate;
    private TextView _getStatus;
    Date _currentDate, _selectedDate;
    Calendar _calendar;
    int _year, _month, _day;
    private String _selectedDOJ;
    private StringBuilder doj;
    private List<AutoCompleteVO> _autoCompleteList;
    private AutoCompleteAdapter _autoCompleteAdapter;
    private boolean stopTextWatcher = false;
    String trainNumber;
    private ProgressDialog waitingProgress;
    private TextView _pageTitle;

    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.live_train_status_layout, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView) getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Status");
        _edtTrainNumber = (EditText) view.findViewById(R.id.edtTrainName);
        _edtTrainNumber.addTextChangedListener(_textWatcher);
        _edtTrainNumber.setTextColor(Color.BLACK);
        _edtTrainNumber.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if (_edtTrainNumber.getText().length() < 3)
                    {
                        trainNameSuggestList.setVisibility(GONE);
                        stopTextWatcher = false;
                    }
                    else
                    {
                        String text = _edtTrainNumber.getText().toString();
                        if (_autoCompleteAdapter != null)
                        {
                            _autoCompleteAdapter.getFilter().filter(text);
                        }
                    }
                }
                return false;
            }
        });
        trainNameSuggestList = (ListView) view.findViewById(R.id.trainNameSuggestList);
        trainNameSuggestList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                AutoCompleteVO model = (AutoCompleteVO) adapterView.getItemAtPosition(i);
                trainNameSuggestList.setVisibility(GONE);
                _edtTrainNumber.setText(model.getName());
                trainNumber =model.getCode();
            }
        });

        _imgClearTrain = (ImageView) view.findViewById(R.id.imgClearTrain);
        _imgClearTrain.setOnClickListener(this);

        _txtDate = (TextView) view.findViewById(R.id.txtDate);
        _txtDate.setOnClickListener(this);
        _imgCalenderDate = (ImageView) view.findViewById(R.id.imgCalenderDate);
        _imgCalenderDate.setOnClickListener(this);

        _getStatus = (TextView) view.findViewById(R.id.btnTrainStatus);
        _getStatus.setOnClickListener(this);

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


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtDate:
            case R.id.imgCalenderDate:
                showCalender();
                break;
            case R.id.imgClearTrain:
                trainNameSuggestList.setVisibility(GONE);
                stopTextWatcher = false;
                clearTrainName();
                break;
            case R.id.btnTrainStatus:
                ((MainActivity)getActivity()).setStatusCheck();
                checkInputValidation(v);
                break;
        }
    }

    private void clearTrainName()
    {
        _edtTrainNumber.getText().clear();
        _imgClearTrain.setVisibility(View.INVISIBLE);
    }

    TextWatcher _textWatcher = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after)
        {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            if (count >= 1)
            {
                _imgClearTrain.setVisibility(View.VISIBLE);
            }
            if (count < 3)
            {
                trainNameSuggestList.setVisibility(GONE);
                stopTextWatcher = false;
            }

            if (_edtTrainNumber.getText().length() == 3)
            {

                if (stopTextWatcher)
                {

                }
                else
                {
                    callAutoSuggestAPI(_edtTrainNumber.getText().toString());
                }

            }
            if (count > 3)
            {
                String text = _edtTrainNumber.getText().toString().toLowerCase(Locale.getDefault());
                if (_autoCompleteAdapter != null)
                {
                    _autoCompleteAdapter.getFilter().filter(text);
                }
            }

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (_edtTrainNumber.getText().toString().length() == 0)
            {
                _imgClearTrain.setVisibility(View.INVISIBLE);
                trainNameSuggestList.setVisibility(GONE);
            }
            String text = s.toString().trim();
            if (_autoCompleteAdapter != null)
            {
                _autoCompleteAdapter.getFilter().filter(text);
            }


        }
    };

    private void callAutoSuggestAPI(String keyword)
    {
        String url = APIUrls.BASE_PREFIX_URL + APIUrls.AUTO_SUGGEST_LIST + keyword + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new AutoSuggestResponse(this, getContext());
       /* String jsonString = JsonStorage.getJsonFileData(getContext(), "AutoSuggest" + (new SimpleDateFormat("dd-MM-yyyy").format(new Date())).toString());
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
        //      }

    }

    private void checkInputValidation(View view)
    {
        String trainName = _edtTrainNumber.getText().toString();
        int count = trainName.length();
        if (count == 0 || count == 1 || count == 2)
        {
            Toast.makeText(getContext(), "Please enter correct train name/number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            showProgressDialog(view);
            callApiForData();
        }

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

    public void showCalender()
    {
        setCurrentDate();
        setSelectedDate();
    }

    private void setCurrentDate()
    {
        _calendar = Calendar.getInstance();
        int currentDay = _calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = _calendar.get(Calendar.MONTH);
        int currentYear = _calendar.get(Calendar.YEAR);
        _currentDate = new Date(currentYear, currentMonth, currentDay);
    }

    private void setSelectedDate()
    {
        doj = new StringBuilder();
        _year = _calendar.get(Calendar.YEAR);
        _month = _calendar.get(Calendar.MONTH);
        _day = _calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        _year = year;
                        _month = monthOfYear;
                        _day = dayOfMonth;

                        _selectedDate = new Date(_year, _month, _day);
                        if (_selectedDate.compareTo(_currentDate) < 0)
                        {
                            Toast.makeText(getContext(), "Cannot set previous date", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            _month += 1;
                            doj.append(_selectedDate.getYear());
                            if (_month < 10)
                            {
                                doj.append("0" + _month);
                            }
                            else
                            {
                                doj.append(_month);
                            }
                            if (_day < 10)
                            {
                                doj.append("0" + _day);
                            }
                            else
                            {
                                doj.append(_day);
                            }
                            _selectedDOJ = doj.toString();
                            doj = new StringBuilder();
                            if (_day < 10)
                            {
                                doj.append("0" + _day);

                            }
                            else
                            {
                                doj.append(_day);

                            }
                            doj.append("-");

                            if (_month < 10)
                            {
                                doj.append("0" + _month);
                            }
                            else
                            {
                                doj.append(_month);
                            }
                            doj.append("-");
                            doj.append(_selectedDate.getYear());
                            _txtDate.setText(doj.toString());
                        }
                    }

                }, _year, _month, _day);
        datePickerDialog.show();


    }

    private void callApiForData()
    {
        String url = APIUrls.BASE_PREFIX_URL + APIUrls.LIVE_STATUS + trainNumber + "/doj/" + _selectedDOJ + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new TrainStatusResponse(this);
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(url, downloadParseResponse);
        downloadJSONAsync.execute();
    }


    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        if (downloadParseResponse.getType() == 200)
        {
            hideKeyboardOnResponse();
            _autoCompleteList = new ArrayList<>();
            AutoSuggestResponse autoSuggestResponse;
            if (downloadParseResponse instanceof AutoSuggestResponse)
            {
                autoSuggestResponse = (AutoSuggestResponse) downloadParseResponse;
                _autoCompleteList.clear();
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();

                if (_autoCompleteList != null)
                {
                    trainNameSuggestList.setVisibility(View.VISIBLE);
                    _autoCompleteAdapter = new AutoCompleteAdapter(_autoCompleteList);
                    trainNameSuggestList.setAdapter(_autoCompleteAdapter);
                    stopTextWatcher = true;
                }
            }
        }
        if (downloadParseResponse.getType() == 500)
        {
            waitingProgress.dismiss();
            TrainStatusResponse trainStatusResponse;
            ArrayList<TrainStatusVO> trainStatusVOs = null;
            if (downloadParseResponse instanceof TrainStatusResponse)
            {
                trainStatusResponse = (TrainStatusResponse) downloadParseResponse;
                trainStatusVOs = trainStatusResponse.get_trainStatusVOs();
            }

            TrainStatusDisplayFragment trainStatusDisplayFragment = new TrainStatusDisplayFragment();
            trainStatusDisplayFragment.setTrainStatusVoList(trainStatusVOs);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, trainStatusDisplayFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void onDownloadFailed(int errorCode, String message)

    {
        Log.v(TAG, "onDownloadFailed");
        if (waitingProgress != null)
        {
            waitingProgress.dismiss();
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }


    private void hideKeyboardOnResponse()
    {
        InputMethodManager mgr = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromInputMethod(_edtTrainNumber.getWindowToken(), 0);
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        _txtDate = null;
        _imgClearTrain = null;
        _imgCalenderDate = null;
        _edtTrainNumber = null;
    }

}
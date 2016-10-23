package com.droiddevgeeks.railjourney.train_status;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by kunal on 11-10-2016.
 */
public class TrainStatusInputFragment extends Fragment implements View.OnClickListener, IDownloadListener, AdapterView.OnItemClickListener
{
    private final String TAG = TrainStatusInputFragment.class.getSimpleName();
    private AutoCompleteTextView _edtTrainNumber;
    private ImageView _imgClearTrain;
    private ImageView _imgCalenderDate;
    private TextView _txtDate;
    private Button _getStatus;
    Date _currentDate, _selectedDate;
    Calendar _calendar;
    int _year, _month, _day;
    private String _selectedDOJ;
    private List<AutoCompleteVO> _autoCompleteList;
    private ArrayAdapter<String> _autoCompleteAdapter;
    private boolean stopTextWatcher = false;
    String trainNumber;

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
        _edtTrainNumber = (AutoCompleteTextView) view.findViewById(R.id.edtTrainName);
        _edtTrainNumber.addTextChangedListener(_textWatcher);
        _edtTrainNumber.setTextColor(Color.BLACK);
        _edtTrainNumber.setOnItemClickListener(this);

        _imgClearTrain = (ImageView) view.findViewById(R.id.imgClearTrain);
        _imgClearTrain.setOnClickListener(this);

        _txtDate = (TextView) view.findViewById(R.id.txtDate);
        _txtDate.setOnClickListener(this);
        _imgCalenderDate = (ImageView) view.findViewById(R.id.imgCalenderDate);
        _imgCalenderDate.setOnClickListener(this);

        _getStatus = (Button) view.findViewById(R.id.btnTrainStatus);
        _getStatus.setOnClickListener(this);
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
                clearTrainName();
                break;
            case R.id.btnTrainStatus:
                checkInputValidation();
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
            if ( _edtTrainNumber.getText().length() >= 2 )
            {
                if(stopTextWatcher)
                {

                }
                else
                {
                    callAutoSuggestAPI( _edtTrainNumber.getText().toString() );
                }

            }

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (_edtTrainNumber.getText().toString().length() == 0)
            {
                _imgClearTrain.setVisibility(View.INVISIBLE);
            }
        }
    };

    private void callAutoSuggestAPI(String keyword)
    {

        String url = APIUrls.BASE_PREFIX_URL + APIUrls.AUTOSUGGESTLIST + keyword + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new AutoSuggestResponse( this , getContext());
        String jsonString = JsonStorage.getJsonFileData( getContext() , "AutoSuggest"+(new SimpleDateFormat( "dd-MM-yyyy" ).format( new Date(  ))).toString());
        if( jsonString != null )
        {
            try
            {
                downloadParseResponse.parseJson( new JSONObject( jsonString ), downloadParseResponse );
            }
            catch ( JSONException e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync( url, downloadParseResponse );
            downloadJSONAsync.execute();
        }

    }

    private void checkInputValidation()
    {
        String trainName = _edtTrainNumber.getText().toString();
        int count = trainName.length();
        if (count == 0 || count == 1 || count == 2)
        {
            Toast.makeText(getContext(), "Please enter correct train name/number", Toast.LENGTH_SHORT).show();
        }
        else
        {
            callApiForData();
        }

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

                            _selectedDOJ = _selectedDate.getYear() + "" + "" + Integer.parseInt((_selectedDate.getMonth() + 1) + "") + _selectedDate.getDate() + "";
                            _txtDate.setText(_selectedDate.getDate() + "/" + Integer.parseInt((_selectedDate.getMonth() + 1) + "") + "/" + _selectedDate.getYear());
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        trainNumber = _autoCompleteList.get( position ).getCode();
        hideKeyboardOnResponse();
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        if (downloadParseResponse.getType() == 200)
        {
            hideKeyboardOnResponse();
            AutoSuggestResponse autoSuggestResponse;
            if (downloadParseResponse instanceof AutoSuggestResponse)
            {
                autoSuggestResponse = (AutoSuggestResponse) downloadParseResponse;
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();
                if (_autoCompleteList != null)
                {
                    String[] traincode = new String[_autoCompleteList.size()];
                    String[] train = new String[_autoCompleteList.size()];
                    for (int i = 0; i < _autoCompleteList.size(); i++)
                    {
                        train[i] = _autoCompleteList.get(i).getName();
                    }
                    _autoCompleteAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, train);
                    _edtTrainNumber.setAdapter(_autoCompleteAdapter);
                    stopTextWatcher = true;
                }
            }
        }
        if (downloadParseResponse.getType() == 500)
        {
            TrainStatusResponse trainStatusResponse;
            ArrayList<TrainStatusVO> trainStatusVOs = null;
            if (downloadParseResponse instanceof DownloadParseResponse)
            {
                trainStatusResponse = (TrainStatusResponse) downloadParseResponse;
                trainStatusVOs = trainStatusResponse.get_trainStatusVOs();
            }

            TrainStatusDisplayFragment trainStatusDisplayFragment = new TrainStatusDisplayFragment();
            trainStatusDisplayFragment.setTrainStatusVoList(trainStatusVOs);
            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, trainStatusDisplayFragment).commit();
            Log.v(TAG, "onDownloadSuccess");
        }
    }

    private void hideKeyboardOnResponse()
    {
        InputMethodManager mgr = (InputMethodManager)getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromInputMethod(_edtTrainNumber.getWindowToken(), 0);
    }

    @Override
    public void onDownloadFailed()
    {
        Log.v(TAG, "onDownloadFailed");
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
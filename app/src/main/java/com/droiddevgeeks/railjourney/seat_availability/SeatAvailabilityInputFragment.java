package com.droiddevgeeks.railjourney.seat_availability;

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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoCompleteAdapter;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestStationResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

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
 * Created by Vampire on 2017-01-03.
 */

public class SeatAvailabilityInputFragment extends Fragment implements View.OnClickListener, IDownloadListener, DatePickerDialog.OnDateSetListener
{

    private RelativeLayout rlCalender;
    private EditText trainName;
    private EditText sourceStation;
    private EditText destinationStation;
    private TextView doj, fareButton;
    private Spinner travelClass,journeyQuota,spinnerPassengerAge;
    private ImageView clearTextTrain, clearTextSource, clearTextDestination;
    private ListView trainList, sourceList, destinationList;

    private int day, month, year;
    private String quota;
    private StringBuilder sb;
    private String trainCode, classTravel;
    private String sourceCode, destinationCode;
    private boolean stopTextWatcher = false;
    private boolean stopSourceWatcher = false;
    private boolean isSource = false;
    private boolean isDestination = false;
    private boolean stopDestinationWatcher = false;

    private List<AutoCompleteVO> _autoCompleteList;
    private AutoCompleteAdapter _autoCompleteAdapter;
    private AutoCompleteAdapter _autoCompleteSourceAdapter;
    private AutoCompleteAdapter _autoCompleteDestinationAdapter;
    private TextView _pageTitle;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fare_enquiry_page_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("Seat");
        clearTextTrain = (ImageView) view.findViewById(R.id.imgClearTrain);
        trainList = (ListView) view.findViewById(R.id.trainNameSuggestList);
        trainList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                trainCode = _autoCompleteList.get(i).getCode();
                trainName.setText(_autoCompleteList.get(i).getName());
                trainList.setVisibility(GONE);
            }
        });
        trainName = (EditText) view.findViewById(R.id.edtTrainName);
        trainName.addTextChangedListener(_textTrainWatcher);
        trainName.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent)
            {
                if (i == KeyEvent.KEYCODE_DEL)
                {
                    if (trainName.getText().length() < 3)
                    {
                        trainList.setVisibility(GONE);
                        stopTextWatcher = false;
                    }
                }
                return false;
            }
        });


        clearTextSource = (ImageView) view.findViewById(R.id.imgClearSource);
        sourceList = (ListView) view.findViewById(R.id.sourceSuggestList);
        sourceList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                sourceCode = _autoCompleteList.get(i).getCode();
                sourceStation.setText(_autoCompleteList.get(i).getName());
                sourceList.setVisibility(GONE);
            }
        });
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
                        sourceList.setVisibility(GONE);
                        stopSourceWatcher = false;
                    }
                }
                return false;
            }
        });


        clearTextDestination = (ImageView) view.findViewById(R.id.imgClearDestination);
        destinationList = (ListView) view.findViewById(R.id.destinationSuggestList);
        destinationList.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
            {
                destinationCode = _autoCompleteList.get(i).getCode();
                destinationStation.setText(_autoCompleteList.get(i).getName());
                destinationList.setVisibility(GONE);
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
                        destinationList.setVisibility(GONE);
                        stopDestinationWatcher = false;
                    }
                }
                return false;
            }
        });


        rlCalender = (RelativeLayout) view.findViewById(R.id.frameEditBoxDOJ);
        rlCalender.setOnClickListener(this);
        doj = (TextView) view.findViewById(R.id.txtDate);

        spinnerPassengerAge = (Spinner)view.findViewById(R.id.spinnerPassengerAge);
        spinnerPassengerAge.setVisibility(GONE);

        travelClass = (Spinner)view.findViewById(R.id.spinnerTravelClass);
        travelClass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String[] travel = getContext().getResources().getStringArray(R.array.travel_class_value);;
                classTravel = travel[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });
        journeyQuota = (Spinner) view.findViewById(R.id.spinnerBookingQuota);
        journeyQuota.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {
                String[] quota_values = getContext().getResources().getStringArray(R.array.booking_quota_value);
                quota = quota_values[i];
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {
                quota = "GN";
            }
        });

        fareButton = (TextView) view.findViewById(R.id.txtGetFare);
        fareButton.setText("Get Seats");

        clearTextTrain.setOnClickListener(this);
        clearTextSource.setOnClickListener(this);
        clearTextDestination.setOnClickListener(this);
        fareButton.setOnClickListener(this);
    }

    TextWatcher _textTrainWatcher = new TextWatcher()
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
                trainList.setVisibility(GONE);
                stopTextWatcher = false;
            }
            if (trainName.getText().length() == 3)
            {
                if (stopTextWatcher)
                {

                }
                else
                {
                    callTrainAutoSuggestAPI(trainName.getText().toString());
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = trainName.getText().toString().toLowerCase(Locale.getDefault());
            if(_autoCompleteAdapter!=null)
            {
             //   _autoCompleteAdapter.filter(text);
            }
        }
    };
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
                sourceList.setVisibility(GONE);
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
                    isDestination = false;
                    callStationAutoSuggestAPI(sourceStation.getText().toString());
                }

            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = sourceStation.getText().toString().toLowerCase(Locale.getDefault());
            if(_autoCompleteSourceAdapter!=null)
            {
             //   _autoCompleteSourceAdapter.filter(text);
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
                destinationList.setVisibility(GONE);
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
                    isSource = false;
                    callStationAutoSuggestAPI(destinationStation.getText().toString());

                }

            }
        }

        @Override
        public void afterTextChanged(Editable s)
        {
            String text = destinationStation.getText().toString().toLowerCase(Locale.getDefault());
            if(_autoCompleteDestinationAdapter!=null)
            {
             //   _autoCompleteDestinationAdapter.filter(text);
            }
        }
    };

    private void callTrainAutoSuggestAPI(String keyword)
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
      //  }

    }

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
     //   }

    }



    private void fetchFareFromServer()
    {
        if (checkInputValidation())
        {
            SeatAvailabilityResultFragment fragment = new SeatAvailabilityResultFragment();
            fragment.setInputDataForApi(trainCode, sourceCode, destinationCode, classTravel ,quota, "" + doj.getText());
            getFragmentManager().beginTransaction().replace(R.id.container, fragment).addToBackStack(null).commit();
        }
        else
        {
            Toast.makeText(getContext(), "Please enter value", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean checkInputValidation()
    {
        if (trainName.getText().toString() == "")
        {
            showKeyBoard(trainName);
            return false;
        }
        if (sourceStation.getText().toString() == "")
        {
            showKeyBoard(sourceStation);
            return false;
        }
        if (destinationStation.getText().toString() == "")
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
        sb.append("-");
        sb.append(year);

        doj.setText(sb.toString());

    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgClearTrain:
                trainName.getText().clear();
                trainList.setVisibility(GONE);
                stopTextWatcher = false;
                break;
            case R.id.imgClearSource:
                sourceStation.getText().clear();
                sourceList.setVisibility(GONE);
                stopSourceWatcher = false;
                isSource = true;
                break;
            case R.id.imgClearDestination:
                destinationStation.getText().clear();
                destinationList.setVisibility(GONE);
                stopDestinationWatcher = false;
                isDestination = true;
                break;
            case R.id.frameEditBoxDOJ:
                selectDateofJourney();
                break;
            case R.id.txtGetFare:
                fetchFareFromServer();
        }

    }


    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        _autoCompleteList = new ArrayList<>();
        if (downloadParseResponse.getType() == 200)
        {
            trainList.setVisibility(View.VISIBLE);
            AutoSuggestResponse autoSuggestResponse;
            if (downloadParseResponse instanceof AutoSuggestResponse)
            {
                autoSuggestResponse = (AutoSuggestResponse) downloadParseResponse;
                _autoCompleteList.clear();
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();

                if (_autoCompleteList != null)
                {
                    _autoCompleteAdapter = new AutoCompleteAdapter( _autoCompleteList);
                    trainList.setAdapter(_autoCompleteAdapter);
                    stopTextWatcher = true;
                }
            }
        }
        else if (downloadParseResponse.getType() == 250)
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
                        sourceList.setVisibility(View.VISIBLE);
                        _autoCompleteSourceAdapter = new AutoCompleteAdapter(_autoCompleteList);
                        sourceList.setAdapter(_autoCompleteSourceAdapter);
                        stopSourceWatcher = true;
                        isSource = false;
                    }
                    else if (isDestination)
                    {
                        destinationList.setVisibility(View.VISIBLE);
                        _autoCompleteDestinationAdapter = new AutoCompleteAdapter( _autoCompleteList);
                        destinationList.setAdapter(_autoCompleteDestinationAdapter);
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


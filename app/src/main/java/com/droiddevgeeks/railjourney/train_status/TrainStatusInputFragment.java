package com.droiddevgeeks.railjourney.train_status;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.TrainStatusVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by kunal on 11-10-2016.
 */
public class TrainStatusInputFragment extends Fragment implements View.OnClickListener, IDownloadListener
{
    private final String TAG = TrainStatusInputFragment.class.getSimpleName();
    private EditText _edtTrainNumber;
    private ImageView _imgClearTrain;
    private TextView _txtDate;
    private Button _getStatus;
    Date _currentDate, _selectedDate;
    Calendar _calendar;
    int _year, _month, _day;
    private String _selectedDOJ ;

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
        _edtTrainNumber = (EditText) view.findViewById(R.id.edtTrainName);
        _edtTrainNumber.addTextChangedListener(_textWatcher);

        _imgClearTrain = (ImageView) view.findViewById(R.id.imgClearTrain);
        _imgClearTrain.setOnClickListener(this);

        _txtDate = (TextView) view.findViewById(R.id.txtDate);
        _txtDate.setOnClickListener(this);

        _getStatus = (Button) view.findViewById(R.id.btnTrainStatus);
        _getStatus.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.txtDate:
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

    private void checkInputValidation()
    {
        String trainName = _edtTrainNumber.getText().toString();
        int count = trainName.length();
        if (count == 0 || count == 1 || count ==2)
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

                            _selectedDOJ = _selectedDate.getYear()+"" + "" +Integer.parseInt((_selectedDate.getMonth()+1)+"") + _selectedDate.getDate() +"";
                            _txtDate.setText(_selectedDate.getDate()+"/"+ Integer.parseInt((_selectedDate.getMonth()+1)+"") +"/"+ _selectedDate.getYear());
                        }
                    }

                }, _year, _month, _day);
        datePickerDialog.show();


    }

    private void callApiForData()
    {
        String url  = APIUrls.BASE_PREFIX_URL + APIUrls.LIVE_STATUS + _edtTrainNumber.getText().toString() + "/doj/" + _selectedDOJ + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new TrainStatusResponse(this);
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync(url, downloadParseResponse);
        downloadJSONAsync.execute();
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
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
        _edtTrainNumber = null;
    }
}

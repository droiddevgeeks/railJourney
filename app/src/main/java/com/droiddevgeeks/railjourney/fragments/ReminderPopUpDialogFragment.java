package com.droiddevgeeks.railjourney.fragments;

import android.app.AlarmManager;
import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.receiver.AlarmReceiver;

import org.w3c.dom.Text;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Vampire on 2016-10-09.
 */
public class ReminderPopUpDialogFragment extends DialogFragment implements View.OnClickListener
{

    private TextView _setAlarm;
    private TextView _cancelAlarm;
    private TextView _dateText;
    private TextView _timeText;

    private DatePicker _datePicker;
    private TimePicker _timePicker;
    private View _alarmView;
    Date _currentDate, _selectedDate;
    Calendar _calendar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        _alarmView = inflater.inflate(R.layout.reminder_fragment, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        init(_alarmView);
        return _alarmView;
    }

    @Override
    public void onResume()
    {
        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.80), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }


    private void init(View alarmView)
    {

        _calendar = Calendar.getInstance();
        _datePicker = (DatePicker) alarmView.findViewById(R.id.datePicker);
        _timePicker = (TimePicker) alarmView.findViewById(R.id.timePicker);

        _dateText = (TextView) alarmView.findViewById(R.id.txtDate);
        _timeText = (TextView) alarmView.findViewById(R.id.txtTime);
        _setAlarm = (TextView) alarmView.findViewById(R.id.txtSetAlarm);
        _cancelAlarm = (TextView) alarmView.findViewById(R.id.txtCancelAlarm);

        _dateText.setOnClickListener(this);
        _timeText.setOnClickListener(this);
        _setAlarm.setOnClickListener(this);
        _cancelAlarm.setOnClickListener(this);

    }

    @Override
    public void onClick(View v)
    {

        switch (v.getId())
        {
            case R.id.txtDate:
                _datePicker.setVisibility(View.VISIBLE);
                _timePicker.setVisibility(View.GONE);
                break;
            case R.id.txtTime:
                _datePicker.setVisibility(View.GONE);
                _timePicker.setVisibility(View.VISIBLE);
                break;
            case R.id.txtSetAlarm:
                setAlarm();
                break;
            case R.id.txtCancelAlarm:
                closeAlarmPopUp();
                break;
        }

    }

    private void setAlarm()
    {
        setCurrentDate();
        setSelectedDate();
        if (_selectedDate.compareTo(_currentDate) < 0)
        {
            Toast.makeText(_alarmView.getContext(), "Cannot set previous date time.", Toast.LENGTH_SHORT).show();
        }
        else if( _selectedDate.getTime() < (_currentDate.getTime() + 300000) )
        {
            Toast.makeText(_alarmView.getContext(), "Please make difference of atleast 5 min", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Calendar calendar = new GregorianCalendar(_datePicker.getYear(),
                    _datePicker.getMonth(),
                    _datePicker.getDayOfMonth(),
                    _timePicker.getCurrentHour(),
                    _timePicker.getCurrentMinute());

            setAlarmReceiver(calendar);
        }

        closeAlarmPopUp();
    }

    private void setCurrentDate()
    {
        int currentDay = _calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = _calendar.get(Calendar.MONTH);
        int currentYear = _calendar.get(Calendar.YEAR) - 1900;
        int currentHour = _calendar.get(Calendar.HOUR_OF_DAY);
        int currentMin = _calendar.get(Calendar.MINUTE);
        _currentDate = new Date(currentYear, currentMonth, currentDay, currentHour, currentMin);
    }

    private void setSelectedDate()
    {
        int selectedYear = _datePicker.getYear();
        int selectedDay = _datePicker.getDayOfMonth();
        int selectedMonth = _datePicker.getMonth();
        int selectedHour = _timePicker.getCurrentHour();
        int selectedMin = _timePicker.getCurrentMinute();
        _selectedDate = new Date(selectedYear - 1900, selectedMonth, selectedDay, selectedHour, selectedMin);
    }

    private void closeAlarmPopUp()
    {
        dismiss();
    }

    public void setAlarmReceiver(Calendar alarm)
    {
        Intent intent = new Intent(_alarmView.getContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(_alarmView.getContext(), 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) _alarmView.getContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, alarm.getTimeInMillis(), pendingIntent);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _setAlarm = null;
        _dateText = null;
        _timeText = null;
        _calendar = null;
        _alarmView = null;
        _datePicker = null;
        _timePicker = null;
        _cancelAlarm = null;
        _currentDate = null;
        _selectedDate = null;
    }
}

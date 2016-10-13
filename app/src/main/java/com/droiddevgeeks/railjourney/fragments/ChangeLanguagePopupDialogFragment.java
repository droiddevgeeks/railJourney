package com.droiddevgeeks.railjourney.fragments;

import android.graphics.Point;
import android.os.Bundle;
import android.app.DialogFragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-11.
 */

public class ChangeLanguagePopupDialogFragment extends DialogFragment implements View.OnClickListener
{

    private TextView _hindi;
    private TextView _english;
    private TextView _marathi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.change_language_dailog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        init(rootView);
        return rootView;
    }

    private void init(View rootView)
    {
        _hindi = (TextView) rootView.findViewById(R.id.txtHindi);
        _english = (TextView) rootView.findViewById(R.id.txtEnglish);
        _marathi = (TextView) rootView.findViewById(R.id.txtMarathi);

        _hindi.setOnClickListener(this);
        _english.setOnClickListener(this);
        _marathi.setOnClickListener(this);

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

    @Override
    public void onClick(View v)
    {
        dismiss();
        switch (v.getId())
        {
            case R.id.txtHindi:
                changeLanguage("hi");
                break;
            case R.id.txtEnglish:
                changeLanguage("en");
                break;
            case R.id.txtMarathi:
                changeLanguage("mr");
                break;

        }
    }

    private void changeLanguage(String languageCode)
    {
        //call localization
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _hindi = null;
        _english = null;
        _marathi = null;
    }
}

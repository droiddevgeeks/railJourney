package com.droiddevgeeks.railjourney.fragments;

import android.app.DialogFragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-08.
 */
public class AboutUsPopupDialogFragment extends DialogFragment implements View.OnClickListener
{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.about_us_popup, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        Button close = (Button) rootView.findViewById(R.id.dismiss);
        close.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume()
    {

        Window window = getDialog().getWindow();
        Point size = new Point();
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        window.setLayout((int) (size.x * 0.95), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        super.onResume();
    }


    @Override
    public void onClick(View v)
    {
        dismiss();
    }
}

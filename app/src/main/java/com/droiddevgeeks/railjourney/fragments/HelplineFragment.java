package com.droiddevgeeks.railjourney.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-08.
 */
public class HelplineFragment extends Fragment implements View.OnClickListener
{

    private TextView _helpline1;
    private TextView _helpline2;
    private TextView _helpline3;
    private TextView _helpline4;
    private TextView _helpline5;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.helpline_layout , container,false);
        init(view);
        return  view;
    }

    private void init(View view)
    {
        _helpline1 = (TextView)view.findViewById(R.id.helpline1);
        _helpline2 = (TextView)view.findViewById(R.id.helpline2);
        _helpline3 = (TextView)view.findViewById(R.id.helpline3);
        _helpline4 = (TextView)view.findViewById(R.id.helpline4);
        _helpline5 = (TextView)view.findViewById(R.id.helpline5);

        _helpline1.setOnClickListener(this);
        _helpline2.setOnClickListener(this);
        _helpline3.setOnClickListener(this);
        _helpline4.setOnClickListener(this);
        _helpline5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.helpline1 :
                openPhoneCallDialog(getResources().getString(R.string.helpline1));
                break;
            case R.id.helpline2 :
                openPhoneCallDialog(getResources().getString(R.string.helpline2));
                break;
            case R.id.helpline3 :
                openPhoneCallDialog(getResources().getString(R.string.helpline3));
                break;
            case R.id.helpline4 :
                openPhoneCallDialog(getResources().getString(R.string.helpline4));
                break;
            case R.id.helpline5 :
                openPhoneCallDialog(getResources().getString(R.string.helpline5));
                break;
        }
    }

    private void openPhoneCallDialog(String number)
    {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+number));
        startActivity(intent);
    }
}

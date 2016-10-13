package com.droiddevgeeks.railjourney.pnr;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-11.
 */

public class PNRCheckFragment extends Fragment implements View.OnClickListener
{
    private TextView _checkPNR;
    private EditText _enterPNR;
    private ImageView _clearPNR;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View pnrView = inflater.inflate(R.layout.pnr_check_layout, container, false);
        return pnrView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _enterPNR = (EditText) view.findViewById(R.id.edtPRNEnterBox);
        _checkPNR = (TextView) view.findViewById(R.id.txtCheckPNR);
        _clearPNR = (ImageView) view.findViewById(R.id.imgClearPNR);


        _enterPNR.addTextChangedListener(_textWatcher);
        _checkPNR.setOnClickListener(this);
        _clearPNR.setOnClickListener(this);
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
                _clearPNR.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void afterTextChanged(Editable s)
        {
            if (_enterPNR.getText().toString().length() == 0)
            {
                _clearPNR.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgClearPNR:
                clearPNR();
                break;
            case R.id.txtCheckPNR:
                checkPNR();
                break;
        }

    }

    private void clearPNR()
    {
        _enterPNR.getText().clear();
        _clearPNR.setVisibility(View.INVISIBLE);
    }

    private void checkPNR()
    {
        String pnr = _enterPNR.getText().toString();
        int count = pnr.length();
        if (count < 10)
        {
            Toast.makeText(getContext(), "Enter correct 10 PNR", Toast.LENGTH_LONG).show();
        }

        getFragmentManager().beginTransaction().replace(R.id.container , new PNRResultFragment()).addToBackStack(null).commit();

    }
}

package com.droiddevgeeks.railjourney.pnr;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.MainActivity;
import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.canceltrain.CancelTrainResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.CancelledTrainVO;
import com.droiddevgeeks.railjourney.models.PnrDataVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;
import com.droiddevgeeks.railjourney.utils.Utilities;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.List;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by Vampire on 2016-10-11.
 */

public class PNRCheckFragment extends Fragment implements View.OnClickListener
{
    private TextView _checkPNR;
    public EditText _enterPNR;
    private ImageView _clearPNR;
    private TextView _pageTitle;
    private AdView mAdView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.pnr_check_layout, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        _pageTitle = (TextView)getActivity().findViewById(R.id.appName);
        _pageTitle.setText("PNR");
        _enterPNR = (EditText) view.findViewById(R.id.edtPRNEnterBox);
        _checkPNR = (TextView) view.findViewById(R.id.txtCheckPNR);
        _clearPNR = (ImageView) view.findViewById(R.id.imgClearPNR);


        _enterPNR.addTextChangedListener(_textWatcher);
        _checkPNR.setOnClickListener(this);
        _clearPNR.setOnClickListener(this);

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
            case R.id.imgClearPNR:
                clearPNR();
                break;
            case R.id.txtCheckPNR:
              //  ((MainActivity)getActivity()).setPnrCheck();
                checkPNR(v);
                break;
        }

    }

    private void clearPNR()
    {
        _enterPNR.getText().clear();
        _clearPNR.setVisibility(View.INVISIBLE);
    }

    private void checkPNR(View view)
    {
        String pnr = _enterPNR.getText().toString();
        int count = pnr.length();
        if (count < 10)
        {
            Toast.makeText(getContext(), "Enter correct 10 PNR", Toast.LENGTH_LONG).show();
        }
        else
        {
            PNRResultFragment pnrResultFragment = new PNRResultFragment();
            pnrResultFragment.sendPNRNumber(pnr);
            getFragmentManager().beginTransaction().replace(R.id.container , pnrResultFragment).addToBackStack(null).commit();
            Utilities.hideSoftKeyboard(getContext(),view);

        }

    }



}

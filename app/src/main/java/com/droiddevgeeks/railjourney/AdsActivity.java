package com.droiddevgeeks.railjourney;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by Vampire on 2017-01-15.
 */

public class AdsActivity extends Activity
{
    InterstitialAd mInterstitialAd;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_full_screen));
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("4d0a3b4bca93c000")
                .addTestDevice("bb23ad1a6aab7f0")
                .build();

        mInterstitialAd.loadAd(adRequest);
        // Load ads into Interstitial Ads
        mInterstitialAd.setAdListener(new AdListener()
        {
            public void onAdLoaded()
            {
                showInterstitial();
            }

            @Override
            public void onAdClosed()
            {
                super.onAdClosed();
                closeAds();
            }
        });


    }

    private void closeAds()
    {
        finish();
    }

    private void showInterstitial()
    {
        if (mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
    }


}

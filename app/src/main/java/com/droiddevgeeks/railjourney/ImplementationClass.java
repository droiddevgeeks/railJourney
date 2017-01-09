package com.droiddevgeeks.railjourney;

import android.util.Log;

/**
 * Created by Vampire on 10/6/2016.
 */
public class ImplementationClass implements ItemClickedListener
{
    @Override
    public void onItemClicked(int position)
    {
        Log.v("ImplementationClass", position+"");
    }
}

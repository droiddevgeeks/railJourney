package com.droiddevgeeks.railjourney;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;

/**
 * Created by kunal on 03-10-2016.
 */
public class LanguageFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_selection_layout, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout rlLanguage = (RelativeLayout) view.findViewById(R.id.rlLanguages);
        Animation slide = new AlphaAnimation(0.0f, 1.0f);
        slide.setDuration(1000);
        rlLanguage.startAnimation(slide);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

package com.droiddevgeeks.railjourney;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.RelativeLayout;

public class LanguageActivity extends Activity implements View.OnClickListener
{
    private Button _btnEnglish;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.language_selection_layout);
        init();
    }

    private void init()
    {
        RelativeLayout rlLanguage = (RelativeLayout) findViewById(R.id.rlLanguages);
        _btnEnglish = (Button) findViewById(R.id.btnEnglish);
        Button btnHindi = (Button) findViewById(R.id.btnHindi);
        Button btnMarathi = (Button) findViewById(R.id.btnMarathi);

        _btnEnglish.setOnClickListener(this);
        btnHindi.setOnClickListener(this);
        btnMarathi.setOnClickListener(this);
        Animation slide = new AlphaAnimation(0.0f, 1.0f);
        slide.setDuration(1000);
        rlLanguage.startAnimation(slide);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnEnglish:
                showHomeScreen();
                break;
            case R.id.btnHindi:
                _btnEnglish.performClick();
                break;
            case R.id.btnMarathi:
                _btnEnglish.performClick();
                break;

        }
    }

    private void showHomeScreen()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _btnEnglish = null;
    }
}

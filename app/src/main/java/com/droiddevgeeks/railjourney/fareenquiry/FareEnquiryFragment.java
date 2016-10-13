package com.droiddevgeeks.railjourney.fareenquiry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

/**
 * Created by Vampire on 2016-10-14.
 */

public class FareEnquiryFragment extends Fragment implements View.OnClickListener
{

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        View fareView = inflater.inflate(R.layout.fare_enquiry_page_layout , container , false);
        return fareView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        Button fareButton  = (Button)view.findViewById(R.id.txtGetFare);
        fareButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        getFragmentManager().beginTransaction().replace(R.id.container , new FareResultFragment()).addToBackStack(null).commit();
    }
}

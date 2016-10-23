package com.droiddevgeeks.railjourney.trainroute;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.autosuggest.AutoSuggestResponse;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.models.AutoCompleteVO;
import com.droiddevgeeks.railjourney.models.TrainRouteVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import java.util.List;

/**
 * Created by Vampire on 2016-10-15.
 */

public class TrainRouteFragment extends Fragment implements View.OnClickListener, IDownloadListener, AdapterView.OnItemClickListener
{
    private Button _trainRoute;
    private AutoCompleteTextView _txtTrainNameNumber;
    private List<AutoCompleteVO> _autoCompleteList;
    private ArrayAdapter<String> _autoCompleteAdapter;
    private boolean stopTextWatcher = false;

    private List<TrainRouteVO> _trainRouteList;
    String trainNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        super.onCreateView( inflater, container, savedInstanceState );

        View trainRoute = inflater.inflate( R.layout.train_route_layout, container, false );
        return trainRoute;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated( view, savedInstanceState );
        _trainRoute = (Button) view.findViewById( R.id.btnTrainRoute );
        _trainRoute.setOnClickListener( this );

        _txtTrainNameNumber = (AutoCompleteTextView) view.findViewById( R.id.edtPRNEnterBox );
        _txtTrainNameNumber.setThreshold( 3 );
        _txtTrainNameNumber.setTextColor( Color.BLACK );
        _txtTrainNameNumber.addTextChangedListener( txtChecker );

        _txtTrainNameNumber.setOnItemClickListener( this );


    }

    TextWatcher txtChecker = new TextWatcher()
    {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
        {
            if ( _txtTrainNameNumber.getText().length() >= 3 )
            {
                if(stopTextWatcher)
                {

                }
                else
                {
                    callAutoSuggestAPI( _txtTrainNameNumber.getText().toString() );
                }

            }

        }

        @Override
        public void afterTextChanged(Editable editable)
        {

        }
    };


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {
        trainNumber = _autoCompleteList.get( i ).getCode();
    }

    private void callTrainRouteAPI(String url)
    {

        DownloadParseResponse downloadParseResponse = new TrainRouteResponse( this );
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync( url, downloadParseResponse );
        downloadJSONAsync.execute();
    }


    private void callAutoSuggestAPI(String keyword)
    {

        String url = APIUrls.BASE_PREFIX_URL + APIUrls.AUTOSUGGESTLIST + keyword + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new AutoSuggestResponse( this , getContext() );
        DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync( url, downloadParseResponse );
        downloadJSONAsync.execute();
    }


    @Override
    public void onClick(View v)
    {
        switch ( v.getId() )
        {
            case R.id.btnTrainRoute:
                if ( checkInputValidation() )
                {
                    String trainRouteURL = APIUrls.BASE_PREFIX_URL + APIUrls.TRAIN_ROUTE + trainNumber + APIUrls.BASE_SUFFIX_URL;
                    callTrainRouteAPI( trainRouteURL );
                }
                break;

        }
    }

    private boolean checkInputValidation()
    {
        if ( _txtTrainNameNumber.getText().toString().length() >= 5 )
        {
            return true;
        }
        else
        {
            Toast.makeText( getContext(), "Please enter train number or name", Toast.LENGTH_LONG ).show();
        }
        return false;
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        if ( downloadParseResponse.getType() == 200 )
        {
            AutoSuggestResponse autoSuggestResponse;
            if ( downloadParseResponse instanceof AutoSuggestResponse )
            {
                autoSuggestResponse = (AutoSuggestResponse) downloadParseResponse;
                _autoCompleteList = autoSuggestResponse.getAutoSuggestList();
                if ( _autoCompleteList != null )
                {
                    String[] traincode = new String[_autoCompleteList.size()];
                    String[] train = new String[_autoCompleteList.size()];
                    for ( int i = 0; i < _autoCompleteList.size(); i++ )
                    {
                        train[i] = _autoCompleteList.get( i ).getName();
                    }
                    _autoCompleteAdapter = new ArrayAdapter<String>( getContext(), android.R.layout.select_dialog_item, train );
                    _txtTrainNameNumber.setAdapter( _autoCompleteAdapter );
                    stopTextWatcher = true;
                }
            }
        }
        else if ( downloadParseResponse.getType() == 600 )
        {
            TrainRouteResponse trainRouteResponse;
            if ( downloadParseResponse instanceof TrainRouteResponse )
            {
                trainRouteResponse = (TrainRouteResponse) downloadParseResponse;
                _trainRouteList = trainRouteResponse.getTrainRouteList();
                if ( _trainRouteList != null )
                {
                    TrainRouteResultFragment trainRouteResultFragment = new TrainRouteResultFragment();
                    trainRouteResultFragment.setTrainRouteListData( _trainRouteList );
                    getFragmentManager().beginTransaction().replace( R.id.container, trainRouteResultFragment).addToBackStack( null ).commit();

                }

            }
        }


    }

    @Override
    public void onDownloadFailed()
    {

    }

}

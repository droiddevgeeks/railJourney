package com.droiddevgeeks.railjourney.canceltrain;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.download.DownloadJSONAsync;
import com.droiddevgeeks.railjourney.interfaces.DownloadParseResponse;
import com.droiddevgeeks.railjourney.interfaces.IDownloadListener;
import com.droiddevgeeks.railjourney.memory.JsonStorage;
import com.droiddevgeeks.railjourney.models.CancelledTrainVO;
import com.droiddevgeeks.railjourney.utils.APIUrls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Vampire on 2016-10-13.
 */

public class CancelTrainFragment extends Fragment implements IDownloadListener
{

    private ListView _cancelTrainListView;
    private CancelTrainAdapter _adapter;
    private List<CancelledTrainVO> _list;
    private ProgressDialog waitingProgress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView( inflater, container, savedInstanceState );
        View cancelTrainView = inflater.inflate( R.layout.cancel_train_layout, container, false );
        return cancelTrainView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated( view, savedInstanceState );
        _cancelTrainListView = (ListView) view.findViewById( R.id.listViewCancelTrain );
        showProgressDialog( view );
        callApiForData();
    }

    private void showProgressDialog(View view)
    {
        waitingProgress = new ProgressDialog( view.getContext() );
        waitingProgress.setCancelable( false );
        waitingProgress.setMessage( "Fetching data ..." );
        waitingProgress.setProgressStyle( ProgressDialog.STYLE_SPINNER );
        waitingProgress.setProgress( 0 );
        waitingProgress.setMax( 100 );
        waitingProgress.show();
    }

    private void callApiForData()
    {
        String url = APIUrls.BASE_PREFIX_URL + "cancelled/date/" + getTodayDate() + APIUrls.BASE_SUFFIX_URL;
        DownloadParseResponse downloadParseResponse = new CancelTrainResponse( this , getContext() );
        String jsonString = JsonStorage.getJsonFileData( getContext() , (new SimpleDateFormat( "dd-MM-yyyy" ).format( new Date(  ))).toString());
        if( jsonString != null )
        {
            try
            {
                downloadParseResponse.parseJson( new JSONObject( jsonString ), downloadParseResponse );
            }
            catch ( JSONException e )
            {
                e.printStackTrace();
            }
        }
        else
        {
            DownloadJSONAsync downloadJSONAsync = new DownloadJSONAsync( url, downloadParseResponse );
            downloadJSONAsync.execute();
        }
    }

    private String getTodayDate()
    {
        DateFormat dateFormat = new SimpleDateFormat( "dd-MM-yyyy" );
        Date todaysDate = new Date();
        String today = dateFormat.format( todaysDate );
        return today;
    }

    private void setCancelTrainAdapter()
    {
        _adapter = new CancelTrainAdapter( getContext(), _list );
        _cancelTrainListView.setAdapter( _adapter );
    }

    @Override
    public void onDownloadSuccess(DownloadParseResponse downloadParseResponse)
    {
        CancelTrainResponse cancelTrainResponse;
        if ( downloadParseResponse instanceof CancelTrainResponse )
        {
            cancelTrainResponse = (CancelTrainResponse) downloadParseResponse;
            _list = cancelTrainResponse.getCancelTrainStatusVOs();
            if ( _list != null )
            {
                waitingProgress.dismiss();
                setCancelTrainAdapter();
            }
            else
            {
                Toast.makeText( getContext(), "Please check interner Connection", Toast.LENGTH_SHORT ).show();
            }
        }

    }

    @Override
    public void onDownloadFailed()
    {
        Toast.makeText( getContext(), "Please check interner Connection", Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        _list = null;
        _adapter = null;
        waitingProgress = null;
        _cancelTrainListView = null;
    }
}

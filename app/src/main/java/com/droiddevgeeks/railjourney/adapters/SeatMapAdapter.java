package com.droiddevgeeks.railjourney.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.SeatMapVO;

import java.util.List;

/**
 * Created by Vampire on 2016-10-08.
 */
public class SeatMapAdapter extends BaseAdapter
{

    private Context _context;
    private List<SeatMapVO> _list;
    SeatMapViewHolder _holder ;
    public SeatMapAdapter(Context context , List<SeatMapVO> list)
    {
        _context = context;
        _list = list;
    }
    @Override
    public int getCount()
    {
        return _list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return _list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

        if ( convertView == null )
        {
            convertView = LayoutInflater.from( parent.getContext() ).inflate( R.layout.seat_map_row, parent, false );
            _holder = new SeatMapViewHolder(convertView);
            convertView.setTag(_holder);
        }
        else
        {
          _holder = (SeatMapViewHolder)convertView.getTag();
        }


        _holder.seatRow.setText( _list.get( position ).getTrainType() );
        return  convertView;
    }


    private class SeatMapViewHolder
    {
        TextView seatRow;
        SeatMapViewHolder(View itemView)
        {
            seatRow = (TextView) itemView.findViewById(R.id.txtSeatRowItem);
        }
    }
}

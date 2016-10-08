package com.droiddevgeeks.railjourney.adapters;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.ItemClickedListener;
import com.droiddevgeeks.railjourney.R;
import com.droiddevgeeks.railjourney.models.SectionVO;
import com.droiddevgeeks.railjourney.utils.Utilities;

import java.util.ArrayList;

/**
 * Created by kunal.sale on 10/4/2016.
 */
public class HomeScreenAdapter extends ArrayAdapter<SectionVO>
{
    private ArrayList<SectionVO> _sectionVOArrayLists;
    private int width;
    private int height;
    private ItemClickedListener itemClickedListener;
    public HomeScreenAdapter( Context context, int resource )
    {
        super(context, resource);
    }

    public HomeScreenAdapter( Context context, int resource, int textViewResourceId)
    {
        super(context, resource, textViewResourceId);
    }

    public HomeScreenAdapter( Context context, int resource, ArrayList<SectionVO> sectionVOs, int width, int height, ItemClickedListener itemClickedListener)
    {
        super(context, resource, sectionVOs);
        _sectionVOArrayLists = sectionVOs;
        this.width = width;
        this.height = height;
        this.itemClickedListener = itemClickedListener;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent )
    {
        LayoutInflater inflater = (LayoutInflater) parent.getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        GridViewHolder gridViewHolder;

        if( convertView == null)
        {
            convertView = inflater.inflate(R.layout.grid_child_layout, parent, false);
            gridViewHolder = new GridViewHolder();
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( width, height);
            convertView.setLayoutParams( new GridView.LayoutParams(params));
            gridViewHolder.txtSectionName = (TextView)convertView.findViewById( R.id.txtSectionName);
            gridViewHolder.imgGridSection = (ImageView)convertView.findViewById( R.id.imgGridSection);
            itemClickedListener.onItemClicked(position);

            convertView.setTag(gridViewHolder);
        }
        else
        {
            gridViewHolder = (GridViewHolder)convertView.getTag();
        }

        gridViewHolder.imgGridSection.setImageDrawable( parent.getContext().getResources().getDrawable(_sectionVOArrayLists.get(position).getResId()));
        gridViewHolder.txtSectionName.setText( _sectionVOArrayLists.get(position).getSectionName());
        return convertView;
    }

    @Override
    public int getCount()
    {
        return _sectionVOArrayLists.size();
    }

    class GridViewHolder
    {
        private TextView txtSectionName;
        private ImageView imgGridSection;
    }
}

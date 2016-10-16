package com.droiddevgeeks.railjourney.navigation_drawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droiddevgeeks.railjourney.R;

import java.util.List;

/**
 * Created by kishan.maurya on 04-10-2016.
 */
public class NavigationDrawerListAdapter extends BaseAdapter
{
    private Context _context;
    private List<NavigationDrawerVO> _list;


    public NavigationDrawerListAdapter( Context context, List<NavigationDrawerVO> list)
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
    public Object getItem(int i)
    {
        return null;
    }


    @Override
    public long getItemId(int i)
    {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup)
    {
        NavigationDrawerViewHolder holder;
        if ( view == null )
        {
            view = LayoutInflater.from( viewGroup.getContext() ).inflate( R.layout.navigation_drawer_list_row, viewGroup, false );
            holder = new NavigationDrawerViewHolder( view );
            view.setTag( holder );
        }
        else
        {
            holder = (NavigationDrawerViewHolder) view.getTag();
        }

        holder.rowTitle.setText( _list.get( i ).getRowtitle() );
        holder.rowIcon.setImageResource( _list.get( i ).getRowImage() );

        return  view;
    }


    private class NavigationDrawerViewHolder
    {
        TextView rowTitle;
        ImageView rowIcon;

        public NavigationDrawerViewHolder(View item)
        {
            rowTitle = (TextView) item.findViewById( R.id.txtListRowTitle );
            rowIcon = (ImageView) item.findViewById( R.id.imgListRowIcon );
        }
    }
}

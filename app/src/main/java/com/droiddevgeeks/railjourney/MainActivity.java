package com.droiddevgeeks.railjourney;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.droiddevgeeks.railjourney.fragments.HomeScreenFragment;
import com.droiddevgeeks.railjourney.navigation_drawer.NavigationDrawerListAdapter;
import com.droiddevgeeks.railjourney.navigation_drawer.NavigationDrawerVO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private ListView _listViewNavigationDrawer;
    private DrawerLayout _drawerLayout;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initNavigationDrawerVO();
        getSupportFragmentManager().beginTransaction().add( R.id.container, new HomeScreenFragment()).commit();
    }
    private void initNavigationDrawerVO()
    {
        _drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        _listViewNavigationDrawer = (ListView)findViewById(R.id.navigationDrawerListView);

        ArrayList<NavigationDrawerVO> listNavigationVos = new ArrayList<>(  );

        listNavigationVos.add( new NavigationDrawerVO(R.drawable.helpline , "HelpLine Number") );
        listNavigationVos.add( new NavigationDrawerVO(R.drawable.reminder , "Set Reminder") );
        listNavigationVos.add( new NavigationDrawerVO(R.drawable.seatmap , "Seat Map") );
        listNavigationVos.add( new NavigationDrawerVO(R.drawable.setting , "Settings") );
        listNavigationVos.add( new NavigationDrawerVO(R.drawable.about , "About Us") );
        listNavigationVos.add( new NavigationDrawerVO(R.drawable.exit , "Exit") );

        NavigationDrawerListAdapter navigationAdapter = new NavigationDrawerListAdapter( this, listNavigationVos);
        _listViewNavigationDrawer.setAdapter(navigationAdapter);
    }
}

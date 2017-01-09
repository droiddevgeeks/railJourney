package com.droiddevgeeks.railjourney;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.droiddevgeeks.railjourney.fragments.AboutUsPopupDialogFragment;
import com.droiddevgeeks.railjourney.fragments.HelplineFragment;
import com.droiddevgeeks.railjourney.fragments.HomeScreenFragment;
import com.droiddevgeeks.railjourney.fragments.ReminderPopUpDialogFragment;
import com.droiddevgeeks.railjourney.navigation_drawer.NavigationDrawerListAdapter;
import com.droiddevgeeks.railjourney.models.NavigationDrawerVO;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener
{
    private ListView _listViewNavigationDrawer;
    private DrawerLayout _drawerLayout;
    private ImageView _hamburgerIcon;
    private ImageView _imgSetReminder;
    private ActionBarDrawerToggle _drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new HomeScreenFragment()).commit();
        }
        else
        {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
        }
        init();
        initNavigationDrawerVO();

    }

    private void init()
    {
        _hamburgerIcon = (ImageView) findViewById(R.id.imgBackIcon);
        _imgSetReminder = (ImageView) findViewById(R.id.imgSetReminder);

        _imgSetReminder.setOnClickListener(this);
        _hamburgerIcon.setOnClickListener(this);
    }

    private void initNavigationDrawerVO()
    {
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _listViewNavigationDrawer = (ListView) findViewById(R.id.navigationDrawerListView);
        _drawerToggle = new ActionBarDrawerToggle(this, _drawerLayout, R.string.open, R.string.close)
        {
            @Override
            public void onDrawerOpened(View drawerView)
            {
                _hamburgerIcon.setImageResource(R.drawable.back1);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView)
            {
                super.onDrawerClosed(drawerView);
                _hamburgerIcon.setImageResource(R.drawable.menu);
            }
        };

        _drawerLayout.setDrawerListener(_drawerToggle);

        ArrayList<NavigationDrawerVO> listNavigationVos = new ArrayList<>();

        listNavigationVos.add(new NavigationDrawerVO(R.drawable.helpline, "HelpLine Number"));
        listNavigationVos.add(new NavigationDrawerVO(R.drawable.reminder, "Set Reminder"));
        listNavigationVos.add(new NavigationDrawerVO(R.drawable.about, "About Us"));
        listNavigationVos.add(new NavigationDrawerVO(R.drawable.close, "Exit"));

        NavigationDrawerListAdapter navigationAdapter = new NavigationDrawerListAdapter(this, listNavigationVos);
        _listViewNavigationDrawer.setAdapter(navigationAdapter);
        _listViewNavigationDrawer.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.imgSetReminder:
                if (_drawerLayout.isDrawerOpen(_listViewNavigationDrawer))
                {
                    closeNavigationDrawer();
                }
                openAlarmFragment();
                break;
            case R.id.imgBackIcon:
                if (_drawerLayout.isDrawerOpen(_listViewNavigationDrawer))
                {
                    closeNavigationDrawer();
                }
                else
                {
                    openNavigationDrawer();
                }
                break;
        }

    }

    public void openNavigationDrawer()
    {
        _hamburgerIcon.setImageResource(R.drawable.back1);
        _drawerLayout.openDrawer(_listViewNavigationDrawer);
    }

    public void closeNavigationDrawer()
    {

        _hamburgerIcon.setImageResource(R.drawable.menu);
        _drawerLayout.closeDrawer(_listViewNavigationDrawer);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        closeNavigationDrawer();
        switch (position)
        {
            case 0:
                openHelplineFragment();
                break;
            case 1:
                openAlarmFragment();
                break;
            case 2:
                openAboutUsPopup();
                break;
            case 3 :
                closeApp();
                break;


        }
    }

    private void openAlarmFragment()
    {
        FragmentManager fm = getFragmentManager();
        ReminderPopUpDialogFragment dialogFragment = new ReminderPopUpDialogFragment();
        dialogFragment.show(fm, "Reminder Fragment");
    }

    private void openHelplineFragment()
    {
        setDrawerState(false);
        _hamburgerIcon.setVisibility(View.INVISIBLE);
        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HelplineFragment()).addToBackStack(null).commit();
    }


    private void openAboutUsPopup()
    {

        FragmentManager fm = getFragmentManager();
        AboutUsPopupDialogFragment dialogFragment = new AboutUsPopupDialogFragment();
        dialogFragment.show(fm, "AboutUs Fragment");

    }

    private void closeApp()
    {
        finish();
        System.exit(0);
    }

    public void setDrawerState(boolean isEnabled)
    {
        if (isEnabled)
        {
            _drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
        else
        {
            _drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }


    @Override
    public void onBackPressed()
    {
        setDrawerState(true);
        _hamburgerIcon.setVisibility(View.VISIBLE);

        if (_drawerLayout.isDrawerOpen(_listViewNavigationDrawer))
        {
            _drawerLayout.closeDrawer(_listViewNavigationDrawer);
        }
        else
        {
            super.onBackPressed();
        }


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        _drawerLayout = null;
        _drawerToggle = null;
        _hamburgerIcon = null;
        _imgSetReminder = null;
        _listViewNavigationDrawer = null;

    }
}

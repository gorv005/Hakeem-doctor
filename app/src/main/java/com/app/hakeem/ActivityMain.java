package com.app.hakeem;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.hakeem.adapter.AdapterSideMenu;
import com.app.hakeem.pojo.SideMenuItem;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.lvMenuItem)
    ListView listView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private AdapterSideMenu adapterSideMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN)));
        listView.setAdapter(adapterSideMenu);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
                SideMenuItem sideMenuItem = adapterSideMenu.getItem(pos);

                if (sideMenuItem.getNameResourse() == R.string.login) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_LOGIN);
                    startActivity(intent);
//                    Intent intent = new Intent(ActivityHome.this, ActivityFragmentContainer.class);
//                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_MY_BOOKINGS);
//                    startActivity(intent);
                    //Util.showToast(ActivityHome.this, R.string.under_construction, false);

                }

            }
        });


        if(Util.isNetworkConnectivity(this))
        {
            getAllPosts();
        }


    }

    private void getAllPosts() {




    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}

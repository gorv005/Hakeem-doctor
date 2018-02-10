package com.app.hakeem;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hakeem.adapter.AdapterPosts;
import com.app.hakeem.adapter.AdapterSideMenu;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.ResponsePost;
import com.app.hakeem.pojo.SideMenuItem;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    @BindView(R.id.lvMenuItem)
    ListView listView;
    @BindView(R.id.lvPosts)
    ListView lvPosts;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.rlBottam)
    RelativeLayout rlBottam;
    @BindView(R.id.tvEmail)
    TextView tvEmail;
    @BindView(R.id.ivProfileImage)
    ImageView ivProfileImage;


    private AdapterSideMenu adapterSideMenu;
    private AdapterPosts adapterPosts;
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.awareness);
        setSupportActionBar(toolbar);

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator(R.drawable.icon_menu);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.openDrawer(GravityCompat.START);
            }
        });
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN), ""));
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

                } else if (sideMenuItem.getNameResourse() == R.string.dependent) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_DEPENDENT);
                    startActivity(intent);

                } else if (sideMenuItem.getNameResourse() == R.string.emr_and_tracker) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_PATIENT_EMR_AND_TRACKER);
                    startActivity(intent);

                }
                else if (sideMenuItem.getNameResourse() == R.string.profile) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_DOCTOR_PROFILE);
                    startActivity(intent);
                }

            }
        });


        if (Util.isNetworkConnectivity(this)) {
            getAllPosts();
        }

        rlBottam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreference.getInstance(ActivityMain.this).setBoolean(C.IS_LOGIN,false);
                onResume();
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN)) {

            rlBottam.setVisibility(View.VISIBLE);
            tvEmail.setText(SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getEmail());
            adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN), SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserType()));
        } else {
            rlBottam.setVisibility(View.GONE);
            adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN), ""));
        }
        listView.setAdapter(adapterSideMenu);
    }

    private void getAllPosts() {

        dialog = Util.getProgressDialog(this, R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        String json = "{\"patient_id\" = \"14\" }";

//        String json = gson.toJson("");
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyService volleyService = new VolleyService(this);
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                    ResponsePost responsePost = gson.fromJson(response.toString(), ResponsePost.class);
                    if (responsePost.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        adapterPosts = new AdapterPosts(ActivityMain.this, responsePost.getPosts());
                        lvPosts.setAdapter(adapterPosts);

                    } else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialog.dismiss();

            }
        }, "posts", C.API_POSTS, Util.getHeader(this), obj);


    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
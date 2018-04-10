package com.app.hakeem;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hakeem.adapter.AdapterPosts;
import com.app.hakeem.adapter.AdapterSideMenu;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.AddPost;
import com.app.hakeem.pojo.Post;
import com.app.hakeem.pojo.QueuePerson;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.ResponsePost;
import com.app.hakeem.pojo.SideMenuItem;
import com.app.hakeem.pojo.UploadFileRes;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.MultipartUtility;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.imgEdit)
    ImageView imgEdit;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.llHealthTracker)
    LinearLayout llHealthTracker;
    @BindView(R.id.llMedicalReport)
    LinearLayout llMedicalReport;
    @BindView(R.id.llAwareness)
    LinearLayout llAwareness;
    @BindView(R.id.rlPhrase)
    View rlPhrase;
    @BindView(R.id.ivDeletePhrase)
    ImageView ivDeletePhrase;
    @BindView(R.id.tvSearchPhrase)
    TextView tvSearchPhrase;
    private AdapterSideMenu adapterSideMenu;
    private AdapterPosts adapterPosts;
    private Dialog dialog, dialogShowAddPostPopUp, dialogQueue;
    ImageLoader imageLoader;
    private int GALLERY = 1, CAMERA = 2;
    private Uri fileUri;
    Uri contentURI;
    boolean isImageSelected = false;
    String filePath, imgPostUrl = null;
    private ProgressDialog mProgressDialog;
    ImageView imgPost, imgDelete;
    ArrayList<Post> posts;
    private Response responseQueue;
    private ReceiverNewPatient myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvTitle.setText(R.string.awareness);

        setSupportActionBar(toolbar);
        imageLoader = new ImageLoader(this);
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
        imgEdit.setOnClickListener(mShowPostDialogListner);
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
                    if (SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {
                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_DOCTOR_PATIENT_LIST);
                    } else {
                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_PATIENT_EMR_AND_TRACKER);

                    }
                    startActivity(intent);

                } else if (sideMenuItem.getNameResourse() == R.string.profile) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    if (SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {
                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_DOCTOR_PROFILE);
                    } else {
                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_PATIENT_PROFILE);
                    }
                    startActivity(intent);
                } else if (sideMenuItem.getNameResourse() == R.string.queue) {
                    if(responseQueue==null){
                        return;
                    }
                    Intent intent = new Intent(ActivityMain.this, ActivityChatDoctor.class);

                    if (responseQueue.getQueuePeople().size() > 0) {
                        intent.putExtra(C.USER, responseQueue.getQueuePeople().get(0));
                    } else {
                        QueuePerson queuePerson = new QueuePerson();
                        queuePerson.setPatientId("na");
                        queuePerson.setEmail("na");
                        intent.putExtra(C.USER, queuePerson);



                    }
                    intent.putExtra(C.TOTAL_PERSON_INQUEUE, responseQueue.getQueuePeople().size());
                    startActivity(intent);
                } else if (sideMenuItem.getNameResourse() == R.string.setting) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_SETTING);
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
                showAlertForConfirm(ActivityMain.this, getString(R.string.logout), getString(R.string.logout_sure), getString(R.string.yes), getString(R.string.no), R.drawable.warning, false);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                }
            }
        });
        llAwareness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSearchDialog(ActivityMain.this);
            }
        });
        llHealthTracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (SharedPreference.getInstance(ActivityMain.this).getBoolean(C.IS_LOGIN)) {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    if (SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserType().equals(C.PATIENT)) {

                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_PATIENT_EMR_AND_TRACKER);
                    } else {
                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_DOCTOR_PATIENT_LIST);

                    }
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_LOGIN);
                    startActivity(intent);
                }
            }
        });
        llMedicalReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (SharedPreference.getInstance(ActivityMain.this).getBoolean(C.IS_LOGIN)) {


                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    if (SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserType().equals(C.PATIENT)) {

                        intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_CONSULTATION_TYPE);
                        startActivity(intent);

                    } else {

                        Intent intent1 = new Intent(ActivityMain.this, ActivityChatDoctor.class);
                        if (responseQueue.getQueuePeople().size() > 0) {
                            intent1.putExtra(C.USER, responseQueue.getQueuePeople().get(0));
                        } else {
                            QueuePerson queuePerson = new QueuePerson();
                            queuePerson.setPatientId("na");
                            queuePerson.setEmail("na");
                            intent1.putExtra(C.USER, queuePerson);
                        }
                        intent1.putExtra(C.TOTAL_PERSON_INQUEUE, responseQueue.getQueuePeople().size());
                        startActivity(intent1);

                    }
                } else {
                    Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
                    intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_LOGIN);
                    startActivity(intent);
                }
            }
        });
        rlPhrase.setVisibility(View.GONE);
        ivDeletePhrase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPosts.filter("");
                rlPhrase.setVisibility(View.GONE);
            }
        });
    }

    public void logout() {
        showAlertForToast(ActivityMain.this, getString(R.string.logout), getString(R.string.logout_success), getString(R.string.ok), R.drawable.warning, true);
        SharedPreference.getInstance(ActivityMain.this).setBoolean(C.IS_LOGIN, false);
        SharedPreference.getInstance(ActivityMain.this).setBoolean(C.IS_NOFICATION, false);

        onResume();
    }


    public void showAlertForConfirm(final Activity context, String title, String msg, String btnText1, String btnText2, int img, final boolean finishActivity) {


        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_alert_with_two_button, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(deleteDialogView);


        TextView tvMsg = (TextView) deleteDialogView.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);

        TextView tvTitle = (TextView) deleteDialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        ImageView ivAlertImage = (ImageView) deleteDialogView.findViewById(R.id.ivAlertImage);
        ivAlertImage.setImageResource(img);
        Button btnDone = (Button) deleteDialogView.findViewById(R.id.btnDone);
        btnDone.setText(btnText1);
        Button btnCancel = (Button) deleteDialogView.findViewById(R.id.btnCancel);
        btnCancel.setText(btnText2);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                logout();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });

        dialog.show();


    }

    View.OnClickListener mShowPostDialogListner = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showSendPostDialog(ActivityMain.this);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter(C.NEW_PATIENT);

        myReceiver = new ReceiverNewPatient();
        registerReceiver(myReceiver, filter);

        if (SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN)) {
            if (SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {
                imgEdit.setVisibility(View.VISIBLE);
                imgProfile.setVisibility(View.VISIBLE);
                getQueuePatient();
                imageLoader.DisplayImage(SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserPic(), imgProfile);
                imageLoader.DisplayImage(SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserPic(), ivProfileImage);

            } else {
                imgEdit.setVisibility(View.GONE);
                imgProfile.setVisibility(View.GONE);
                imgProfile.setImageResource(R.drawable.profile);
            }

//            String sender = SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getEmail().replace("@", "");
//            MyXMPP myXMPP = new MyXMPP();
//            myXMPP.create(sender);
        } else {
            imgEdit.setVisibility(View.GONE);
            imgProfile.setVisibility(View.GONE);
            imgProfile.setImageResource(R.drawable.profile);

        }
        if (SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN)) {

            rlBottam.setVisibility(View.VISIBLE);
            tvEmail.setText(SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getEmail());
            adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN), SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserType()));
        } else {
            rlBottam.setVisibility(View.GONE);
            adapterSideMenu = new AdapterSideMenu(this, Util.getSideMenuList(SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN), ""));
        }
        listView.setAdapter(adapterSideMenu);
        saveTokenToServer();


    }


    void saveTokenToServer() {

        if (!SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN))
            return;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("device_id", SharedPreference.getInstance(getApplicationContext()).getString(C.TOKEN));
        hashMap.put("user_id", SharedPreference.getInstance(getApplicationContext()).getUser(C.LOGIN_USER).getUserId());


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyService volleyService = new VolleyService(getApplicationContext());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());

                try {
//                    Gson gson = new Gson();
//                    ConsultationTypeAndList consultationType = gson.fromJson(response.toString(), ConsultationTypeAndList.class);
//                    if (consultationType.getStatusCode().equals(C.STATUS_SUCCESS)) {
//
//                        Util.showAlert(getActivity(),getString(R.string.error),consultationType.getMessage(),getString(R.string.ok),R.drawable.warning);
//                    } else {
//                        Util.showAlert(getActivity(), getString(R.string.error), consultationType.getMessage(), getString(R.string.ok), R.drawable.warning);
//                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());


            }
        }, "consultant", C.API_REGISTER_TOKEN, Util.getHeader(getApplicationContext()), obj);


    }


    private void AddPost(AddPost post) {

        dialog = Util.getProgressDialog(this, R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        Gson gson = new Gson();
        String json = gson.toJson(post);
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
                    Response responsePost = gson.fromJson(response.toString(), Response.class);
                    if (responsePost.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        if (dialogShowAddPostPopUp != null && dialogShowAddPostPopUp.isShowing()) {
                            dialogShowAddPostPopUp.dismiss();
                        }
                        getAllPosts();

                    } else {
                        Util.showAlert(ActivityMain.this, getString(R.string.error), responsePost.getMessage(), getString(R.string.ok), R.drawable.warning);

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialog.dismiss();
                if (dialogShowAddPostPopUp != null && dialogShowAddPostPopUp.isShowing()) {
                    dialogShowAddPostPopUp.dismiss();
                }
            }
        }, "posts", C.API_ADD_POSTS, Util.getHeader(this), obj);


    }

    public void likePost(int pos) {
        if (SharedPreference.getInstance(ActivityMain.this).getBoolean(C.IS_LOGIN)) {

            Post post = adapterPosts.getPost(pos);
            likeUnlikePost(post, pos);
        } else {
            Intent intent = new Intent(ActivityMain.this, ActivityContainer.class);
            intent.putExtra(C.FRAGMENT_ACTION, C.FRAGMENT_LOGIN);
            startActivity(intent);
        }
    }

    void likeUnlikePost(final Post post, final int pos) {

        dialog = Util.getProgressDialog(this, R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserId());
        hashMap.put("post_id", "" + post.getPostId());
        if (post.getIsLiked() == 1) {
            hashMap.put("is_liked", "" + 0);
        } else {
            hashMap.put("is_liked", "" + 1);
        }
        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
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
                        if (posts.get(pos).getIsLiked() == 1) {
                            posts.get(pos).setIsLiked(0);
                            posts.get(pos).setTotalLikes(posts.get(pos).getTotalLikes() - 1);

                        } else {
                            posts.get(pos).setIsLiked(1);
                            posts.get(pos).setTotalLikes(posts.get(pos).getTotalLikes() + 1);

                        }
                        adapterPosts.notifyDataSetChanged();
                    } else {
                        Util.showAlert(ActivityMain.this, getString(R.string.error), responsePost.getMessage(), getString(R.string.ok), R.drawable.warning);

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
        }, "posts", C.API_POSTS_LIKE, Util.getHeader(this), obj);


    }

    private void getAllPosts() {

        dialog = Util.getProgressDialog(this, R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        if (SharedPreference.getInstance(ActivityMain.this).getBoolean(C.IS_LOGIN)) {
            hashMap.put("user_id", SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserId());
        } else {
            hashMap.put("user_id", "");

        }
        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
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
                        posts = responsePost.getPosts();
                        adapterPosts = new AdapterPosts(ActivityMain.this, posts);
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

    public void showSearchDialog(final Activity context) {

        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_search, null);
        final Dialog dialogShowAddPostPopUp = new Dialog(context);
        dialogShowAddPostPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogShowAddPostPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogShowAddPostPopUp.setContentView(deleteDialogView);


        final EditText etSearch = (EditText) deleteDialogView.findViewById(R.id.etSearch);
        final ImageView ivSearch = (ImageView) deleteDialogView.findViewById(R.id.ivSearch);
        final TextView tvobgyne = (TextView) deleteDialogView.findViewById(R.id.tvobgyne);
        final TextView tvPediatric = (TextView) deleteDialogView.findViewById(R.id.tvPediatric);
        final TextView tvAbodminal = (TextView) deleteDialogView.findViewById(R.id.tvAbodminal);
        final TextView tvPsycological = (TextView) deleteDialogView.findViewById(R.id.tvPsycological);
        final TextView tvFamilyAndCommunity = (TextView) deleteDialogView.findViewById(R.id.tvFamilyAndCommunity);


        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterPosts.filter(etSearch.getText().toString());
                dialogShowAddPostPopUp.dismiss();
                rlPhrase.setVisibility(View.VISIBLE);
                tvSearchPhrase.setText(getString(R.string.search_phrase) + ":" + etSearch.getText().toString());
            }
        });
        tvobgyne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText(getString(R.string.obgyne));
                tvobgyne.setTextColor(ContextCompat.getColor(context, R.color.blue));
                tvPediatric.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvAbodminal.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPsycological.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvFamilyAndCommunity.setTextColor(ContextCompat.getColor(context, R.color.white));

            }
        });
        tvPediatric.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText(getString(R.string.pediatric));
                tvobgyne.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPediatric.setTextColor(ContextCompat.getColor(context, R.color.blue));
                tvAbodminal.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPsycological.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvFamilyAndCommunity.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        });
        tvAbodminal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText(getString(R.string.abodminal));
                tvobgyne.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPediatric.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvAbodminal.setTextColor(ContextCompat.getColor(context, R.color.blue));
                tvPsycological.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvFamilyAndCommunity.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        });
        tvPsycological.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText(getString(R.string.psycological));
                tvobgyne.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPediatric.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvAbodminal.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPsycological.setTextColor(ContextCompat.getColor(context, R.color.blue));
                tvFamilyAndCommunity.setTextColor(ContextCompat.getColor(context, R.color.white));
            }
        });
        tvFamilyAndCommunity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etSearch.setText(getString(R.string.family_and_community));
                tvobgyne.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPediatric.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvAbodminal.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvPsycological.setTextColor(ContextCompat.getColor(context, R.color.white));
                tvFamilyAndCommunity.setTextColor(ContextCompat.getColor(context, R.color.blue));
            }
        });

        dialogShowAddPostPopUp.show();


    }

    public void showSendPostDialog(final Activity context) {

        isImageSelected = false;
        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_post, null);
        dialogShowAddPostPopUp = new Dialog(context);
        dialogShowAddPostPopUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogShowAddPostPopUp.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogShowAddPostPopUp.setContentView(deleteDialogView);


        final EditText etPost = (EditText) deleteDialogView.findViewById(R.id.etPost);

        final TextView tvPostTextLength = (TextView) deleteDialogView.findViewById(R.id.tvPostTextLength);
        imgPost = (ImageView) deleteDialogView.findViewById(R.id.img_photo);
        imgDelete = (ImageView) deleteDialogView.findViewById(R.id.btn_close);

        ImageView ivAttachDoc = (ImageView) deleteDialogView.findViewById(R.id.ivAttachDoc);
        ImageView ivCamera = (ImageView) deleteDialogView.findViewById(R.id.ivCamera);
        ImageView ivSendTweet = (ImageView) deleteDialogView.findViewById(R.id.ivSendTweet);
        etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() >= 500) {
                    Util.showAlertForToast(ActivityMain.this, getString(R.string.error), getString(R.string.enter_250_char), getString(R.string.ok), R.drawable.warning, false);
                    return;
                }
                if (s.toString().length() > 250) {
                    tvPostTextLength.setTextColor(Color.RED);
                } else {
                    tvPostTextLength.setTextColor(Color.WHITE);
                }
                int l = 250 - s.toString().length();
                tvPostTextLength.setText("" + l);

            }
        });
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCameraPermissionGranted()) {
                    takePhotoFromCamera();
                } else {
                    requestPermissionForCamera();

                }
            }
        });

        ivAttachDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });
        imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isImageSelected = false;
                imgPost.setVisibility(View.GONE);
                imgDelete.setVisibility(View.GONE);
            }
        });
        ivSendTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                AddPost addPost = new AddPost();
                addPost.setUserId(SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getUserId());
                //  addPost.setAwarenessId(SharedPreference.getInstance(ActivityMain.this).getUser(C.LOGIN_USER).getSpecialist());
                addPost.setAwarenessId("1");
                addPost.setTags("morning,evening");

                if (isImageSelected) {
                    addPost.setType(C.PHOTO);
                    addPost.setContent("");

                    new UploadFileFroURL(ActivityMain.this, addPost).execute(filePath);
                } else if (etPost.getText().toString().trim().length() > 0) {
                    addPost.setType(C.TEXT);
                    addPost.setContent(etPost.getText().toString().trim());
                    addPost.setUrl("");

                    AddPost(addPost);
                } else {
                    Util.showAlert(ActivityMain.this, getString(R.string.error), getString(R.string.post_should_not_be_empty), getString(R.string.ok), R.drawable.warning);

                }


            }
        });


        dialogShowAddPostPopUp.show();


    }

    public boolean isCameraPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_GRANTED) {

                return true;
            } else {


                // ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation

            return true;
        }


    }

    private void requestPermissionForCamera() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(ActivityMain.this, android.Manifest.permission.CAMERA)) {
            //     ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Utils.PERMISSION_REQUEST_CODE);
            Util.showAlert(ActivityMain.this, getString(R.string.alert), "Please allow camera permission in App Settings for additional functionality.", getString(R.string.ok), R.drawable.warning);

            //  Toast.makeText(getActivity(),"GPS permission allows us to access location data. Please allow in App Settings for additional functionality.",Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, 2);
        }
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                contentURI = data.getData();
                try {
                    isImageSelected = true;
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), contentURI);
                    //   String path = saveImage(bitmap);
                    bitmap = Util.scaleDown(bitmap, 500, true);
                    imgPost.setVisibility(View.VISIBLE);
                    imgDelete.setVisibility(View.VISIBLE);
                    imgPost.setImageBitmap(bitmap);
                    filePath = Util.getPath(contentURI, this);
                 /*   String profileImage= Utils.getBase64Image(bitmap);
                    if(C.isloggedIn) {
                        profile.setProfilePic(profileImage);
                        profile.setProfilePicname(Utils.getCurrentTimeStamp()+".jpg");
                    }
                    else {
                        profileDetail.setPicture(profileImage);
                        profileDetail.setPicturename(Utils.getCurrentTimeStamp()+".jpg");
                    }*/
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        } else if (requestCode == CAMERA) {
            try {
                BitmapFactory.Options options = new BitmapFactory.Options();
                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 8;
                Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                        options);
                bitmap = rotateImageIfRequired(bitmap, fileUri.getPath());
                isImageSelected = true;
                imgPost.setVisibility(View.VISIBLE);
                imgDelete.setVisibility(View.VISIBLE);
                imgPost.setImageBitmap(bitmap);
                contentURI = fileUri;
                filePath = fileUri.getPath();
               /* String profileImage = Utils.getBase64Image(bitmap);
                if (C.isloggedIn) {
                    profile.setProfilePic(profileImage);
                    profile.setProfilePicname(Utils.getCurrentTimeStamp() + ".jpg");
                } else {
                    profileDetail.setPicture(profileImage);
                    profileDetail.setPicturename(Utils.getCurrentTimeStamp() + ".jpg");
                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            //  fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            File file = createImageFile();
            boolean isDirectoryCreated = file.getParentFile().mkdirs();
            Log.e("DEBUG", "openCamera: isDirectoryCreated: " + isDirectoryCreated);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri tempFileUri = FileProvider.getUriForFile(this,
                        "com.app.hakeem", // As defined in Manifest
                        file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            } else {
                Uri tempFileUri = Uri.fromFile(file);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, tempFileUri);
            }
            // intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            startActivityForResult(intent, CAMERA);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private File createImageFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new
                Date());
        File file = new File(C.IMAGE_PATH, "IMG_" + timeStamp +
                ".jpg");
        fileUri = Uri.fromFile(file);
        return file;
    }

    public static Bitmap rotateImageIfRequired(Bitmap img, String selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }

    public static Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }

    public class UploadFileFroURL extends AsyncTask<String, Integer, String> {

        private String upLoadServerUri = "http://www.dataheadstudio.com/test/api/upload";
        private File sourceFile;
        private int serverResonseCode;
        private Activity activity;

        AddPost addPost;

        public UploadFileFroURL(Activity activity, AddPost post) {
            // TODO Auto-generated constructor stub
            this.activity = activity;
            addPost = post;
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(activity);
            mProgressDialog.setTitle("Upload");
            mProgressDialog.setMessage("Uploading, Please Wait!");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected String doInBackground(String... content) {

//               String response = uploadFile(content[0]);
            String response = upload(content[0]);
            return response;
        }

        String upload(String sourceFileUri) {
            sourceFile = new File(sourceFileUri);
            String requestURL = "http://www.dataheadstudio.com/test/api/upload";

            try {
                MultipartUtility multipart = new MultipartUtility(new MultipartUtility.ProgressListener() {
                    @Override
                    public void transferred(int num) {
                        publishProgress(num);

                    }
                }, requestURL, "profileUserAuthKey", "uploadprofileimage", ActivityMain.this);


//                multipart.addFormField(C.ACTION, "uploadprofileimage");
//                multipart.addFormField("profileUserAuthKey", SharedPreference.getInstance(getActivity()).getString(C.AUTH_KEY_PROFILE));
                String fileName = renameFile(MimeTypeMap.getFileExtensionFromUrl(sourceFileUri));
                multipart.addFilePart("photo", sourceFile, fileName);
//                multipart.addFilePart("document", sourceFile, fileName);
                List<String> response = multipart.finish();

                System.out.println("SERVER REPLIED:");

                for (String line : response) {
                    System.out.println("DEBUG==" + line);
                }

                return response.get(0);
            } catch (IOException ex) {
                System.err.println(ex);
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {

            mProgressDialog.setProgress(progress[0]);
        }


        protected void onPostExecute(String result) {
            mProgressDialog.dismiss();
            Log.e("Response :", result);
//            Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
            Gson gson = new Gson(); // Or use new GsonBuilder().create();
            UploadFileRes fileRes = gson.fromJson(result, UploadFileRes.class);
            if (fileRes.getStatusCode().equals(C.STATUS_SUCCESS)) {
                addPost.setUrl(fileRes.getUrls().getPhoto());
                AddPost(addPost);
            }
        }

        private String renameFile(String fileName) {
            Random random = new Random();
            int num = 1000 + random.nextInt(9999);
            String newFileName = "LR" + System.currentTimeMillis() + num + "." + fileName;
            return newFileName;
        }


    }

    public void showAlertForToast(final Activity context, String title, String msg, String btnText, int img, final boolean finishActivity) {


        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_alert, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(deleteDialogView);


        TextView tvMsg = (TextView) deleteDialogView.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);

        TextView tvTitle = (TextView) deleteDialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        ImageView ivAlertImage = (ImageView) deleteDialogView.findViewById(R.id.ivAlertImage);
        ivAlertImage.setImageResource(img);
        Button btnDone = (Button) deleteDialogView.findViewById(R.id.btnDone);
        btnDone.setText(btnText);


        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                if (finishActivity) {
                    getAllPosts();
                }

            }
        });


        dialog.show();


    }


    void getQueuePatient() {


        dialogQueue = Util.getProgressDialog(this, R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(this).getUser(C.LOGIN_USER).getUserId() + "");


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
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
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    responseQueue = gson.fromJson(response.toString(), Response.class);
                    if (responseQueue.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        adapterSideMenu.getItem(0).setVal(responseQueue.getQueuePeople().size() + "");
                        adapterSideMenu.notifyDataSetChanged();

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialogQueue.dismiss();

            }
        }, "login", C.API_ALL_QUEUE_PATIENT, Util.getHeader(this), obj);


    }


    public class ReceiverNewPatient extends BroadcastReceiver {
        public ReceiverNewPatient() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(C.NEW_PATIENT))

            {
                getQueuePatient();

            }
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(myReceiver);
    }


}
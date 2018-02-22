package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterComments;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.CommentList;
import com.app.hakeem.pojo.DoctorPatientList;
import com.app.hakeem.pojo.HTBloodSugerReportList;
import com.app.hakeem.pojo.Post;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComments extends Fragment {

    @BindView(R.id.ivDoctor)
    ImageView ivDoctor;
    @BindView(R.id.ivMsgImg)
    ImageView ivMsg;
    @BindView(R.id.ivSpeciality)
    ImageView ivSpeciality;
    @BindView(R.id.tvDoctorName)
    TextView tvDoctorName;
    @BindView(R.id.tvMsg)
    TextView tvMsg;
    @BindView(R.id.tvLike)
    TextView tvLike;
    @BindView(R.id.tvComment)
    TextView tvComment;
    @BindView(R.id.lvComments)
    ListView lvComments;
    Post post;
    ImageLoader imageLoader;
    private Dialog progressDialog;
    AdapterComments adapterComments;
    public FragmentComments() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=new ImageLoader(getActivity());
        Bundle bundle = getArguments();
        if (bundle != null) {
            post = (Post) bundle.getSerializable(C.POST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_comments, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        tvDoctorName.setText(post.getPostBy());
        tvLike.setText(post.getTotalLikes()+"");
        tvComment.setText(post.getTotal_comments()+"");
        if (C.PHOTO.equals(post.getType())) {
            tvMsg.setVisibility(View.GONE);
            ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(), ivMsg);

        } else if (C.VIDEO.equals(post.getType())) {
            tvMsg.setVisibility(View.GONE);
            ivMsg.setVisibility(View.VISIBLE);
            imageLoader.DisplayImage(post.getUrl(), ivMsg);
        } else if (C.TEXT.equals(post.getType())) {
           ivMsg.setVisibility(View.GONE);
            tvMsg.setVisibility(View.VISIBLE);
           tvMsg.setText(post.getContent());
        }
        imageLoader.DisplayImage(post.getIconUrl(), ivSpeciality);
        imageLoader.DisplayImage(post.getUserPic(), ivDoctor);
        getComments();
    }
    private void getComments() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("post_id",""+post.getPostId());


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyService volleyService = new VolleyService(getActivity());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                progressDialog.dismiss();
                CommentList responseServer = gson.fromJson(response.toString(), CommentList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getComments()!=null && responseServer.getComments().size()>0) {
                        adapterComments=new AdapterComments(getActivity(),responseServer.getComments());
                        lvComments.setAdapter(adapterComments);
                    }
                    else {
                        Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                    }

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.error),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.warning,false);

            }
        }, "callback", C.API_GET_COMMENTS, Util.getHeader(getActivity()), obj);


    }


}

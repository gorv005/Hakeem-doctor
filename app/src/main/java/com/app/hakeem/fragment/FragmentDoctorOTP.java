package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterPosts;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.ResponsePost;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorOTP extends Fragment {
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.btnResend)
    TextView btnResend;

    @BindView(R.id.etSMSverificationNumber)
    EditText etSMSverificationNumber;

    @BindView(R.id.btnSend)
    Button btnSend;
    private Dialog dialog;

    public FragmentDoctorOTP() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }



    private void getAllPosts() {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        if (SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)) {
            hashMap.put("user_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
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

        VolleyService volleyService = new VolleyService(getActivity());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                    ResponsePost responsePost = gson.fromJson(response.toString(), ResponsePost.class);
                    if (responsePost.getStatusCode().equals(C.STATUS_SUCCESS)) {


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
        }, "posts", C.API_POSTS, Util.getHeader(getActivity()), obj);


    }

}

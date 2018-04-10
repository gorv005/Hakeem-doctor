package com.app.hakeem.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityChatDoctor;
import com.app.hakeem.ActivityContainer;
import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.QueuePerson;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.User;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.SharedPreference;
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
public class FragmentDoctorProfile extends Fragment {

    @BindView(R.id.btnCaseHistory)
    Button btnCaseHistory;
    @BindView(R.id.btnManageAwarenessQueue)
    Button btnManageAwarenessQueue;
    @BindView(R.id.btnManageQueue)
    Button btnManageQueue;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.tvDocName)
    TextView tvDocName;
    @BindView(R.id.tvDocProf)
    TextView tvDocProf;
    User user;
    ImageLoader imageLoader;
    private Dialog  dialogQueue;
    private Response responseQueue;

    public FragmentDoctorProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageLoader=new ImageLoader(getActivity());
        user= SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(user!=null && user.getUserType().equals(C.DOCTOR)){
            tvDocName.setText(user.getFirstName());
            tvDocProf.setText(user.getSpecialist());

            imageLoader.DisplayImage(user.getUserPic(),imgProfile);
        }

      /*  Drawable drawable = getResources().getDrawable(R.drawable.mng_awareness);
        drawable.setBounds(0, 0, (int)(drawable.getIntrinsicWidth()*0.7),
                (int)(drawable.getIntrinsicHeight()*0.7));
        ScaleDrawable sd = new ScaleDrawable(drawable, 0, 98, 98);
        btnManageAwarenessQueue.setCompoundDrawables(sd.getDrawable(), null, null, null);*/
        btnCaseHistory.setOnClickListener(mBtnCaseHostoryClickListner);
        btnManageAwarenessQueue.setOnClickListener(mbtnManageAwarenessQueueClickListner);
        btnManageQueue.setOnClickListener(mBtnManageQueueClickListner);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.doc_profile);

    }

    View.OnClickListener mBtnCaseHostoryClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_DOCTOR_PATIENT_LIST,null);
        }
    };
    View.OnClickListener mbtnManageAwarenessQueueClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().finish();
        }
    };
    View.OnClickListener mBtnManageQueueClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          getQueuePatient();
        }
    };


    void getQueuePatient() {


        dialogQueue = Util.getProgressDialog(getActivity(), R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId() + "");


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
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    responseQueue = gson.fromJson(response.toString(), Response.class);
                    if (responseQueue.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        if(responseQueue==null){
                            return;
                        }
                        Intent intent = new Intent(getActivity(), ActivityChatDoctor.class);

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
                    }
                    else {
                        Intent intent = new Intent(getActivity(), ActivityChatDoctor.class);


                            QueuePerson queuePerson = new QueuePerson();
                            queuePerson.setPatientId("na");
                            queuePerson.setEmail("na");
                            intent.putExtra(C.USER, queuePerson);




                        intent.putExtra(C.TOTAL_PERSON_INQUEUE, 0);
                        startActivity(intent);
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
        }, "login", C.API_ALL_QUEUE_PATIENT, Util.getHeader(getActivity()), obj);


    }

}

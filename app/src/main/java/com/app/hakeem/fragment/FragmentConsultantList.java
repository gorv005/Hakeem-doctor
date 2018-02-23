package com.app.hakeem.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.hakeem.ActivityChat;
import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterConsultant;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.ConsultationTypeAndList;
import com.app.hakeem.pojo.User;
import com.app.hakeem.util.C;
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
public class FragmentConsultantList extends Fragment {


    @BindView(R.id.lvConsultant)
    ListView lvConsultant;

    @BindView(R.id.tvClinic)
    TextView tvClinic;

    @BindView(R.id.rlImage)
    RelativeLayout rlImage;

    private User user;
    private Dialog dialog;
    private AdapterConsultant adapter;
    private int specialityId;


    public FragmentConsultantList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER);
        specialityId = getArguments().getInt(C.SPECIALITY);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        switch (specialityId) {
            case 1:
                rlImage.setBackgroundResource(R.drawable.family);
                tvClinic.setText(R.string.family_and_community);
                break;
            case 2:
                rlImage.setBackgroundResource(R.drawable.psychiatric);
                tvClinic.setText(R.string.psychological);
                break;
            case 3:
                rlImage.setBackgroundResource(R.drawable.abdominal);
                tvClinic.setText(R.string.abdominal);
                break;
            case 4:
                rlImage.setBackgroundResource(R.drawable.obgyne);
                tvClinic.setText(R.string.obgyne);
                break;
            case 5:
                rlImage.setBackgroundResource(R.drawable.pediatric);
                tvClinic.setText(R.string.pediatrics);
                break;
        }

        lvConsultant.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                if (C.START_CHATTING.equals(SharedPreference.getInstance(getActivity()).getString(C.TITLE))) {
                    if (SharedPreference.getInstance(getActivity()).getString(C.CHAT_DOCTOR_ID).equals(adapter.getItem(position).getDoctorId() + "")) {
                        Intent intent = new Intent(getActivity(), ActivityChat.class);
                        intent.putExtra(C.SENDER, SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getEmail());
                        intent.putExtra(C.RECEIVER, adapter.getItem(position).getEmail());
                        intent.putExtra(C.DOCTOR, adapter.getItem(position));
                        intent.putExtra(C.SPECIALITY, specialityId);
                        getActivity().startActivity(intent);
                    } else {
                        addPatientToQueue(adapter.getItem(position).getDoctorId() + "");
                    }
                } else {
                    addPatientToQueue(adapter.getItem(position).getDoctorId() + "");
                }
            }
        });

        loadDoctorlist();
    }

    private void addPatientToQueue(String doctorID) {
        dialog = Util.getProgressDialog(getActivity(), R.string.loading);
        dialog.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", doctorID + "");
        hashMap.put("patient_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
        hashMap.put("date", Util.getTimeAM());

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
                    ConsultationTypeAndList consultationType = gson.fromJson(response.toString(), ConsultationTypeAndList.class);
                    if (consultationType.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        Util.showAlert(getActivity(), getString(R.string.error), consultationType.getMessage(), getString(R.string.ok), R.drawable.warning);
                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), consultationType.getMessage(), getString(R.string.ok), R.drawable.warning);
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
        }, "consultant", C.API_PATIENT_QUEUE, Util.getHeader(getActivity()), obj);


    }

    private void loadDoctorlist() {

        dialog = Util.getProgressDialog(getActivity(), R.string.loading);
        dialog.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("specialist_id", specialityId + "");

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
                    ConsultationTypeAndList consultationType = gson.fromJson(response.toString(), ConsultationTypeAndList.class);
                    if (consultationType.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        adapter = new AdapterConsultant(getActivity(), consultationType.getOnlineDoctors());
                        lvConsultant.setAdapter(adapter);
                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), consultationType.getMessage(), getString(R.string.ok), R.drawable.warning);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());

            }
        }, "consultant", C.API_CONSULTANT, Util.getHeader(getActivity()), obj);

    }


    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.medical_consultation);

    }
}

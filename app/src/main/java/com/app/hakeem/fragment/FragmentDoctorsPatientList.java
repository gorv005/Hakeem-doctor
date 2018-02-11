package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterDoctorPatientsList;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorPatientList;
import com.app.hakeem.pojo.DoctorsPatient;
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
public class FragmentDoctorsPatientList extends Fragment {

    @BindView(R.id.lvPatient)
    ListView lvPatient;
    private Dialog progressDialog;
    DoctorPatientList doctorPatientList;
    public FragmentDoctorsPatientList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctors_patient_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if (Util.isNetworkConnectivity(getActivity())) {
            getPatientList();
        } else {
        //    Util.showToast(getActivity(), R.string.please_connect_to_the_internet, true);
            Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.please_connect_to_the_internet),getString(R.string.ok),R.drawable.warning,true);

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.choose_patient);

    }

    private void getPatientList() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("doctor_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
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
                DoctorsPatient responseServer = gson.fromJson(response.toString(), DoctorsPatient.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {


                    AdapterDoctorPatientsList adapterPatientList = new AdapterDoctorPatientsList(getActivity(), responseServer.getData());
                    lvPatient.setAdapter(adapterPatientList);

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
        }, "callback", C.API_REGISTER_FETCH_DOCTOR_PATIENTS, Util.getHeader(getActivity()), obj);


    }

}

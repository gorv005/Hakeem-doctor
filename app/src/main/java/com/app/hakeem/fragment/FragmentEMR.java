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
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterEMRDoctorList;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.EMRDoctorsList;
import com.app.hakeem.util.C;
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
public class FragmentEMR extends Fragment {

    @BindView(R.id.tvEmr)
    ListView tvEmr;
    @BindView(R.id.tvNoData)
    TextView tvNoData;
    private Dialog progressDialog;

    String dependentId,patientId;
    public FragmentEMR() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

            dependentId =  bundle.getString(C.DEPENDENT_ID);
            patientId =  bundle.getString(C.PATIENT_ID);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emr, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (Util.isNetworkConnectivity(getActivity())) {
            getDependents();
        } else {
            //    Util.showToast(getActivity(), R.string.please_connect_to_the_internet, true);
            Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.please_connect_to_the_internet),getString(R.string.ok),R.drawable.warning,true);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    private void getDependents() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);
        hashMap.put("dependent_id", dependentId);
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
                EMRDoctorsList responseServer = gson.fromJson(response.toString(), EMRDoctorsList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getData()!=null && responseServer.getData().size()>0) {
                        AdapterEMRDoctorList adapterEMRDoctorList = new AdapterEMRDoctorList(getActivity(), responseServer.getData(),patientId,dependentId);
                        tvEmr.setAdapter(adapterEMRDoctorList);
                        tvNoData.setVisibility(View.GONE);
                    }
                    else {
                        tvNoData.setVisibility(View.VISIBLE);

                    }

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.error),responseServer.getMessage(),getString(R.string.ok),R.drawable.error,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_GET_CHAT_GROUP, Util.getHeader(getActivity()), obj);


    }

}

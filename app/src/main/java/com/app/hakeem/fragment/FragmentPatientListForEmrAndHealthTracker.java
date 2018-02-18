package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterPatientListEMRandTracker;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.Response;
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
public class FragmentPatientListForEmrAndHealthTracker extends Fragment {

    @BindView(R.id.lvDependent)
    ListView lvDependent;
    private Dialog progressDialog;
    private View header;
    private AdapterPatientListEMRandTracker adapterPatientList;

    public FragmentPatientListForEmrAndHealthTracker() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_list_for_emr_and_health_tracker, container, false);
    }
    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.choose_dependent);

    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        lvDependent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Bundle bundle = new Bundle();
                bundle.putString(C.NAME, adapterPatientList.getItem(position).getName());
                bundle.putString(C.DOB, adapterPatientList.getItem(position).getDob());
                bundle.putString(C.GENDER, adapterPatientList.getItem(position).getGender());
                bundle.putString(C.DEPENDENT_ID, adapterPatientList.getItem(position).getChildId());
                bundle.putString(C.PATIENT_ID, adapterPatientList.getItem(position).getChildId());

                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_EMR_AND_TRACKER, bundle);

            }
        });
        if (Util.isNetworkConnectivity(getActivity())) {
            getDependentList();
        } else {
            Util.showToast(getActivity(), R.string.please_connect_to_the_internet, true);
        }
    }


    private void getDependentList() {

        lvDependent.removeHeaderView(header);
        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
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
                Response responseServer = gson.fromJson(response.toString(), Response.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {

                    Child child = new Child();

                    child.setName(responseServer.getPatient().getName());
                    child.setDob(responseServer.getPatient().getDob());
                    child.setGender(responseServer.getPatient().getGender());
                    child.setParantId(responseServer.getPatient().getId()+"");
                    responseServer.getPatient().getChildrens().add(0,child);
                    adapterPatientList = new AdapterPatientListEMRandTracker(getActivity(), responseServer.getPatient().getChildrens());
                    lvDependent.setAdapter(adapterPatientList);

                } else {
                    Util.showToast(getActivity(), responseServer.getMessage(), false);
                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                Util.showToast(getActivity(), R.string.network_error, false);
            }
        }, "callback", C.API_REGISTER_FETCH_PATIENT, Util.getHeader(getActivity()), obj);


    }

}

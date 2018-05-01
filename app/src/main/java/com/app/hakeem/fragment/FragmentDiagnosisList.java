package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.EMRLogList;
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
public class FragmentDiagnosisList extends Fragment {
    @BindView(R.id.tvDiagnosis)
    TextView tvDiagnosis;
    String dependentId,patientId,doctorId;
    private Dialog progressDialog;

    public FragmentDiagnosisList() {
        // Required empty public constructor
    }
    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.diagnosis);

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {

            dependentId =  bundle.getString(C.DEPENDENT_ID);
            patientId =  bundle.getString(C.PATIENT_ID);
            doctorId =  bundle.getString(C.CHAT_DOCTOR_ID);

        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getDiagnosis();

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_diagnosis_list, container, false);
    }
    private void getDiagnosis() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("patient_id", patientId);
        hashMap.put("doctor_id", doctorId);
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
                Log.e("Response", response.toString());

                EMRLogList responseServer = gson.fromJson(response.toString(), EMRLogList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {
                    if(responseServer.getData()!=null && responseServer.getData().getDiagnosis()!=null
                            &&responseServer.getData().getDiagnosis().size()>0) {
                        tvDiagnosis.setText(responseServer.getData().getDiagnosis().get(responseServer.getData().getDiagnosis().size()-1).getDetails());
                    }
                    else {
                        tvDiagnosis.setText(getString(R.string.no_data_to_display));

                    }

                } else {
                    //Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.alert),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                // Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error),getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_GET_PRESCRIPTION, Util.getHeader(getActivity()), obj);


    }

}

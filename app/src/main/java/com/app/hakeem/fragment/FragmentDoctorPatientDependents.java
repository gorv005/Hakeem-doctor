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
import com.app.hakeem.adapter.AdapterDoctorPatientsDependentList;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.DependentList;
import com.app.hakeem.pojo.DoctorPatientList;
import com.app.hakeem.pojo.Patient;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorPatientDependents extends Fragment {

    @BindView(R.id.lvPatient)
    ListView lvPatient;
    private Dialog progressDialog;
    Patient patient;
    List<Child> childLis;
    DoctorPatientList doctorPatientList;
    public FragmentDoctorPatientDependents() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctorPatientList = (DoctorPatientList) bundle.getSerializable(C.PATIENT);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_patient_dependents, container, false);
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

        lvPatient.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putString(C.NAME, childLis.get(position).getName());
                bundle.putString(C.DOB, childLis.get(position).getDob());
                bundle.putString(C.GENDER, childLis.get(position).getGender());
                bundle.putString(C.DEPENDENT_ID, childLis.get(position).getChildId());
                bundle.putString(C.PATIENT_ID, ""+doctorPatientList.getPatientId());

                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_EMR_AND_TRACKER, bundle);
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.choose_dependent);

    }
    private void getDependents() {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", ""+doctorPatientList.getPatientId());
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
                DependentList responseServer = gson.fromJson(response.toString(), DependentList.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {

                    patient=responseServer.getPatient();
                    childLis=patient.getChildrens();
                    Child child=new Child();
                    child.setName(patient.getName());
                    child.setChildId(patient.getId());
                    child.setDob(patient.getDob());
                    child.setGender(patient.getGender());
                    child.setRelation("Main");
                    childLis.add(0,child);
                    AdapterDoctorPatientsDependentList adapterPatientList = new AdapterDoctorPatientsDependentList(getActivity(), childLis);
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
        }, "callback", C.API_REGISTER_FETCH_PATIENT, Util.getHeader(getActivity()), obj);


    }

}

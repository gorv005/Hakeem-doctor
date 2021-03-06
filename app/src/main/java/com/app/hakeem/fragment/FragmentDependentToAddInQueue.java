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
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterPatientList;
import com.app.hakeem.interfaces.DependentDelete;
import com.app.hakeem.interfaces.IResult;
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
public class FragmentDependentToAddInQueue extends Fragment implements DependentDelete {

    @BindView(R.id.lvDependent)
    ListView lvDependent;
    private Dialog progressDialog;

    private View header;
    private AdapterPatientList adapterPatientList;
    private int specilization;


    public FragmentDependentToAddInQueue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        specilization = getArguments().getInt(C.SPECIALITY);
        return inflater.inflate(R.layout.fragment_dependent, container, false);
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

        ActivityContainer.tvTitle.setText(R.string.choose_dependent);

        lvDependent.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Bundle bundle = new Bundle();
                if (position == 0) {
                    bundle.putString(C.DEPENDENT_ID, "");
                }
                else {
                    bundle.putString(C.DEPENDENT_ID, adapterPatientList.getItem(position - 1).getChildId());

                    bundle.putSerializable(C.OBJECT, adapterPatientList.getItem(position - 1));
                }
                bundle.putInt(C.SPECIALITY, specilization);
                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_CONSULTANT, bundle);
            }
        });


        if (Util.isNetworkConnectivity(getActivity())) {
            getDependentList();
        } else {
          //  Util.showToast(getActivity(), R.string.please_connect_to_the_internet, true);
            Util.showAlertForToast(getActivity(),getString(R.string.warning), getString(R.string.please_connect_to_the_internet),getString(R.string.ok),R.drawable.warning,true);

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

                    lvDependent.removeHeaderView(header);
                    header = getActivity().getLayoutInflater().inflate(R.layout.item_dependent, null);
                    header.findViewById(R.id.btnDelete).setVisibility(View.GONE);
                    TextView tvName = (TextView) header.findViewById(R.id.tvName);
                    tvName.setText(getString(R.string.main_profile));
                    lvDependent.addHeaderView(header);
                    adapterPatientList = new AdapterPatientList(FragmentDependentToAddInQueue.this, getActivity(), responseServer.getPatient().getChildrens(),true);
                    lvDependent.setAdapter(adapterPatientList);

                } else {
                    //    Util. showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(), getString(R.string.warning), responseServer.getMessage(), getString(R.string.ok), R.drawable.warning, false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
            //    Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error), getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);

            }
        }, "callback", C.API_REGISTER_FETCH_PATIENT, Util.getHeader(getActivity()), obj);


    }


    @Override
    public void notifyDependentDeleted() {
        getDependentList();
    }
}

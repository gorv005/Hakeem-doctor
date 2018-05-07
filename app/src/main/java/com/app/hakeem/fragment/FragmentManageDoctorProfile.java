package com.app.hakeem.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterDoctorEducationManage;
import com.app.hakeem.adapter.AdapterDoctorExperienceManage;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorProfile;
import com.app.hakeem.pojo.Education;
import com.app.hakeem.pojo.Experience;
import com.app.hakeem.pojo.ExperienceDoc;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.Support;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageDoctorProfile extends Fragment {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etAbountMe)
    EditText etAbountMe;
    @BindView(R.id.lvEducation)
    ListView lvEducation;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.lvExperiance)
    ListView lvExperiance;
    private Dialog dialog;
    AdapterDoctorExperienceManage adapterDoctorExperienceManage;
    AdapterDoctorEducationManage adapterDoctorEducationManage;
    public FragmentManageDoctorProfile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        final List<Education> educations = new ArrayList<>();

        adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),educations);
            lvEducation.setAdapter(adapterDoctorEducationManage);

        final List<ExperienceDoc> experiences = new ArrayList<>();

        adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),experiences);
        lvExperiance.setAdapter(adapterDoctorExperienceManage);

    }


    private void getDocData(final Support support) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();

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
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                    DoctorProfile doctorProfile = gson.fromJson(response.toString(), DoctorProfile.class);
                    if (doctorProfile.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        etUserName.setText(getString(R.string.dr)+""+doctorProfile.getData().getName());
                        tvLocation.setText(doctorProfile.getData().getHomeLocation());
                        etAbountMe.setText(doctorProfile.getData().getAboutMe());
                        for (int i=0;i<doctorProfile.getData().getEducation().size();i++) {
                            adapterDoctorEducationManage.addItem(doctorProfile.getData().getEducation().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvEducation);
                        for (int i=0;i<doctorProfile.getData().getExperience().size();i++) {
                            adapterDoctorExperienceManage.addItem(doctorProfile.getData().getExperience().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvExperiance);
                        Util.showAlertBackPress(getActivity(),getString(R.string.success),doctorProfile.getMessage(),getString(R.string.ok),R.drawable.success,true);

                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), doctorProfile.getMessage(), getString(R.string.ok), R.drawable.error);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                dialog.dismiss();
                Log.e("Response :", error.toString());

            }
        }, "login", C.API_GET_DOC_DATA, Util.getHeader(getActivity()), obj);


    }

}

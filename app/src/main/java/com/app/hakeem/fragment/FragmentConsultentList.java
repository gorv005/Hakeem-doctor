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
import com.app.hakeem.adapter.AdapterConsultationType;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.ConsultationType;
import com.app.hakeem.pojo.User;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentConsultentList extends Fragment {


    @BindView(R.id.lvConsultationType)
    ListView lvConsultationType;

    private User user;
    private Dialog dialog;
    private AdapterConsultationType adapter;

    public FragmentConsultentList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_consultation_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (user != null && user.getUserType().equals(C.PATIENT)) {


            // imageLoader.DisplayImage(user.getUserPic(),imgProfile);
        }


        loadAwarenesslist();
    }

    private void loadAwarenesslist() {

        dialog = Util.getProgressDialog(getActivity(), R.string.loading);
        dialog.show();
        Gson gson = new Gson();
        String strAwareNess = SharedPreference.getInstance(getActivity()).getString(C.AWARENESS_LIST);
        if (strAwareNess != null) {
            ConsultationType consultationType = gson.fromJson(strAwareNess.toString(), ConsultationType.class);
            adapter = new AdapterConsultationType(getActivity(), consultationType.getAwareness());
            lvConsultationType.setAdapter(adapter);
            dialog.dismiss();
        } else {
            VolleyService volleyService = new VolleyService(getActivity());
            volleyService.postDataVolley(new IResult() {
                @Override
                public void notifySuccess(String requestType, JSONObject response) {
                    Log.e("Response :", response.toString());
                    dialog.dismiss();

                    try {
                        Gson gson = new Gson();
                        ConsultationType consultationType = gson.fromJson(response.toString(), ConsultationType.class);
                        if (consultationType.getStatusCode().equals(C.STATUS_SUCCESS)) {
                            SharedPreference.getInstance(getActivity()).setString(C.AWARENESS_LIST, response.toString());
                            adapter = new AdapterConsultationType(getActivity(), consultationType.getAwareness());
                            lvConsultationType.setAdapter(adapter);
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
            }, "awareness", C.API_AWARENESS, Util.getHeader(getActivity()), new JSONObject());

        }


    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.patient_profile);

    }
}

package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.User;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPatientProfile extends Fragment {

    @BindView(R.id.btnElecMediRecords)
    Button btnElecMediRecords;
    @BindView(R.id.btnMedicalCons)
    Button btnMedicalCons;
    @BindView(R.id.btnManageDependents)
    Button btnManageDependents;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;
    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDocProf)
    TextView tvDocProf;
    User user;
    ImageLoader imageLoader;
    public FragmentPatientProfile() {
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
        return inflater.inflate(R.layout.fragment_patient_profile, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if(user!=null && user.getUserType().equals(C.PATIENT)){
            tvName.setText(user.getFirstName());

           // imageLoader.DisplayImage(user.getUserPic(),imgProfile);
        }
           btnMedicalCons.setOnClickListener(mbtnMedicalConsQueueClickListner);
        btnElecMediRecords.setOnClickListener(mbtnElecMediRecordsClickListner);
        btnManageDependents.setOnClickListener(mbtnManageDependentsClickListner);

    }

    @Override
    public void onResume() {
        super.onResume();
        ActivityContainer.tvTitle.setText(R.string.patient_profile);

    }

    View.OnClickListener mbtnMedicalConsQueueClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_SELECT_PATIENT_TO_QUEUE,null);
        }
    };
    View.OnClickListener mbtnManageDependentsClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_DEPENDENT,null);
        }
    };
    View.OnClickListener mbtnElecMediRecordsClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_PATIENT_EMR_AND_TRACKER,null);
        }
    };
}

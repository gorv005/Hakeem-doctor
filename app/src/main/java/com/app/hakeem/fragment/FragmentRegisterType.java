package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.util.C;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRegisterType extends Fragment implements View.OnClickListener {


    @BindView(R.id.btnDoctor)
    Button btnDoctor;
    @BindView(R.id.btnPatient)
    Button btnPatient;

    public FragmentRegisterType() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register_type, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        btnDoctor.setOnClickListener(this);
        btnPatient.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnDoctor:
                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_DOCTOR_REGISTRATION_STEP3,null);
                break;
            case R.id.btnPatient:
                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_PATIENT_REGISTRATION_STEP1,null);
                break;
        }
    }
}

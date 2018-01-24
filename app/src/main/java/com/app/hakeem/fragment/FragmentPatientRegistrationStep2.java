package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.app.hakeem.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPatientRegistrationStep2 extends Fragment {


    public FragmentPatientRegistrationStep2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_registration_step2, container, false);
    }

}

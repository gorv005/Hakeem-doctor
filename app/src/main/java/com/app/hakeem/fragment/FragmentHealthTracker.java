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
public class FragmentHealthTracker extends Fragment {

    @BindView(R.id.btnFeverReport)
    Button btnFeverReport;
    @BindView(R.id.btnWeightReport)
    Button btnWeightReport;
    @BindView(R.id.btnBloodPressure)
    Button btnBloodPressure;
    @BindView(R.id.btnBloodSugar)
    Button btnBloodSugar;
    Bundle bundle;
    public FragmentHealthTracker() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_healthtracker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
         bundle = getArguments();

        btnFeverReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityContainer)getActivity()).callFragment(C.FRAGMENT_TRACKER_FEVER_REPORT,bundle);
            }
        });

        btnWeightReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityContainer)getActivity()).callFragment(C.FRAGMENT_TRACKER_WEIGHT_REPORT,bundle);
            }
        });
        btnBloodPressure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityContainer)getActivity()).callFragment(C.FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT,bundle);
            }
        });
        btnBloodSugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ActivityContainer)getActivity()).callFragment(C.FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT,bundle);
            }
        });
    }
}

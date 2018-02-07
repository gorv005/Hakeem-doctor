package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.util.C;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentEmrAndHealthTracker extends Fragment {

    @BindView(R.id.tvName)
    TextView tvName;
    @BindView(R.id.tvDOB)
    TextView tvDOB;
    @BindView(R.id.tvWeight)
    TextView tvWeight;
    @BindView(R.id.tvHeight)
    TextView tvHeight;
    @BindView(R.id.tvChronic)
    TextView tvChronic;
    @BindView(R.id.tvGender)
    TextView tvGender;
    @BindView(R.id.rbEMR)
    RadioButton rbEmr;
    @BindView(R.id.rbHeathTracker)
    RadioButton rbHeathTracker;
    private Fragment fragment;
    private int fragmentAction;
    private String dependentId;

    public FragmentEmrAndHealthTracker() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_emr_and_health_tracker, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        rbEmr.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbHeathTracker.setChecked(false);
                rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.blue));
                rbHeathTracker.setBackgroundResource(R.drawable.button_deselect_blue);

                rbEmr.setBackgroundResource(R.drawable.button_select_blue);
                rbEmr.setTextColor(getActivity().getResources().getColor(R.color.white));
                Bundle bundle = getArguments();
                fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_EMR);
                fragmnetLoader(fragmentAction, bundle);

            }
        });

        rbHeathTracker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbEmr.setChecked(false);
                rbEmr.setBackgroundResource(R.drawable.button_deselect_blue);
                rbEmr.setTextColor(getActivity().getResources().getColor(R.color.blue));

                rbHeathTracker.setBackgroundResource(R.drawable.button_select_blue);
                rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.white));
                Bundle bundle = getArguments();
                fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_TRACKER);
                fragmnetLoader(fragmentAction, bundle);
                fragmnetLoader(fragmentAction, bundle);

            }
        });


        rbHeathTracker.setChecked(false);
        rbHeathTracker.setTextColor(getActivity().getResources().getColor(R.color.blue));
        rbHeathTracker.setBackgroundResource(R.drawable.button_deselect_blue);

        rbEmr.setBackgroundResource(R.drawable.button_select_blue);
        rbEmr.setTextColor(getActivity().getResources().getColor(R.color.white));

        Bundle bundle = getArguments();
        fragmentAction = getArguments().getInt(C.FRAGMENT_ACTION, C.FRAGMENT_EMR);
        fragmnetLoader(fragmentAction, bundle);

        dependentId = bundle.getString(C.DEPENDENT_ID);
        tvName.setText(bundle.getString(C.NAME));
        tvGender.setText(bundle.getString(C.GENDER));
        tvDOB.setText(bundle.getString(C.DOB));

    }


    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType) {

            case C.FRAGMENT_EMR:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentEMR();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER_WEIGHT_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER_FEVER_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER_BLOOD_PRESSURE_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;
            case C.FRAGMENT_TRACKER_BLOOD_SUGAR_REPORT:

//                tvTitle.setText(R.string.emr);
                fragment = new FragmentHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);
                break;

        }
        fragment.setArguments(bundle);
        fragmentTransaction.commitNow();
        getChildFragmentManager().executePendingTransactions();


    }


}
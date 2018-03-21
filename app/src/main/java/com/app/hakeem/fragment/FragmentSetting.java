package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.app.hakeem.R;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends Fragment {


    @BindView(R.id.switchNotification)
    Switch switchNotification;

    public FragmentSetting() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);

        boolean isNotification = SharedPreference.getInstance(getActivity()).getBoolean(C.IS_NOFICATION);
        switchNotification.setChecked(isNotification);

        switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,isChecked);
            }
        });
    }
}

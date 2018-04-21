package com.app.hakeem.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSetting extends Fragment {


    @BindView(R.id.switchNotification)
    Switch switchNotification;
    @BindView(R.id.etLanguage)
    EditText etLanguage;
    @BindView(R.id.spinnerLanguage)
    Spinner spinnerLanguage;
    boolean isFirstTime=true;
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
        etLanguage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerLanguage.performClick();
            }
        });

        spinnerLanguage.setOnItemSelectedListener(mspinnerLanguageSelectListner);

        String[] language = new String[]{
           /* "ALL",
            "PRE",
            "POST",
            "SLEEP"*/
                getActivity().getString(R.string.english),
                getActivity().getString(R.string.arabic)


        };

        final List<String> specialityList = new ArrayList<>(Arrays.asList(language));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,specialityList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return true;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.BLACK);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLanguage.setAdapter(spinnerArrayAdapter);
        String lan=SharedPreference.getInstance(getActivity()).getString(C.LANGUAGE);
        if (lan!=null && lan.equals(C.ARABIC)){
          spinnerLanguage.setSelection(1);
        }
        else {
            spinnerLanguage.setSelection(0);

        }
    }


    AdapterView.OnItemSelectedListener mspinnerLanguageSelectListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!isFirstTime) {
                    if (position == 0) {
                        etLanguage.setText(getString(R.string.english));
                        SharedPreference.getInstance(getActivity()).setString(C.LANGUAGE, C.English);
                        //  Util.setAppLocale(C.English,getActivity());
                        openMainActivity();

                    } else if (position == 1) {
                        etLanguage.setText(getString(R.string.arabic));
                        SharedPreference.getInstance(getActivity()).setString(C.LANGUAGE, C.ARABIC);
                        //  Util.setAppLocale(C.ARABIC,getActivity());
                        openMainActivity();
                    }
                }
                else {
                    isFirstTime=false;
                }

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    private void openMainActivity() {
        Intent intent = new Intent(getActivity(), ActivityContainer.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }
}

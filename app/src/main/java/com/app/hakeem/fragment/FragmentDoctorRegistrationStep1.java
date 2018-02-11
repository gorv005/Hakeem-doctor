package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorRegistrationStep1 extends Fragment {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.rbMale)
    RadioButton rbMale;
    @BindView(R.id.rbFemale)
    RadioButton rbFemale;
    @BindView(R.id.btnContinue)
    Button btnContinue;

    public FragmentDoctorRegistrationStep1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_registration_step1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllValid()) {
                    DoctorRegistration doctorRegistration=new DoctorRegistration();
                    doctorRegistration.setFirstName(etUserName.getText().toString());
                    doctorRegistration.setLastName("");
                    doctorRegistration.setGender(rbMale.isChecked() ? "M" : "F");
                    doctorRegistration.setMobileNumber(etMobile.getText().toString());
                    doctorRegistration.setEmail(etEmail.getText().toString());
                    doctorRegistration.setPassword(etPassword.getText().toString());
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(C.USER, doctorRegistration);
                    ((ActivityContainer) (getActivity())).fragmnetLoader(C.FRAGMENT_DOCTOR_REGISTRATION_STEP2, bundle);

                }
            }
        });

        rbMale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbFemale.setChecked(false);
                rbFemale.setTextColor(getActivity().getResources().getColor(R.color.blue));
                rbFemale.setBackgroundResource(R.drawable.button_deselect_blue);

                rbMale.setBackgroundResource(R.drawable.button_select_blue);
                rbMale.setTextColor(getActivity().getResources().getColor(R.color.white));

            }
        });

        rbFemale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rbMale.setChecked(false);
                rbMale.setBackgroundResource(R.drawable.button_deselect_blue);
                rbMale.setTextColor(getActivity().getResources().getColor(R.color.blue));

                rbFemale.setBackgroundResource(R.drawable.button_select_blue);
                rbFemale.setTextColor(getActivity().getResources().getColor(R.color.white));
            }
        });

    }


    public boolean isAllValid() {

        if (etUserName.getText().toString().length() == 0) {
          //  etUserName.setError(getActivity().getResources().getString(R.string.name_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.name_required),getString(R.string.ok),R.drawable.warning);
            etUserName.requestFocus();
            return false;
        } else if (etUserName.getText().toString().trim().length() < 3) {
          //  etUserName.setError(getActivity().getResources().getString(R.string.name_should_be_more_then_3_character));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.name_should_be_more_then_3_character),getString(R.string.ok),R.drawable.warning);

            etUserName.requestFocus();
            return false;
        } else if (etUserName.getText().toString().trim().startsWith(".")) {
           // etUserName.setError(getActivity().getResources().getString(R.string.name_could_not_starts_with_dot));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.name_could_not_starts_with_dot),getString(R.string.ok),R.drawable.warning);

            etUserName.requestFocus();
            return false;
        } else if (etMobile.getText().toString().length() == 0) {
          //  etMobile.setError(getActivity().getResources().getString(R.string.mobile_no_is_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.mobile_no_is_required),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        } else if (etMobile.getText().length() < 10) {

         //   etMobile.setError(getActivity().getResources().getString(R.string.please_enter_valid_mobile_number));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_enter_valid_mobile_number),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        } else if (etMobile.getText().toString().startsWith("0")) {
         //   etMobile.setError(getActivity().getResources().getString(R.string.number_strts_with_zero));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.number_strts_with_zero),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        } else if (etEmail.getText().toString().length() == 0) {
          //  etEmail.setError(getActivity().getResources().getString(R.string.email_is_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.email_is_required),getString(R.string.ok),R.drawable.warning);

            etEmail.requestFocus();
            return false;
        } else if (!Util.isValidMail(etEmail.getText().toString())) {
         //   etEmail.setError(getActivity().getResources().getString(R.string.please_enter_valid_email));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_enter_valid_email),getString(R.string.ok),R.drawable.warning);

            etEmail.requestFocus();
            return false;
        } else if (etPassword.getText().toString().length() == 0) {//||etConfirmPassword.getText().toString().length() == 0
         //   etPassword.setError(getActivity().getResources().getString(R.string.password_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.password_required),getString(R.string.ok),R.drawable.warning);

            etPassword.requestFocus();
            return false;
        } else if (!Util.isValidPassword(etPassword.getText().toString())) {
          //  etPassword.setError(getActivity().getResources().getString(R.string.password_should_be_8_12_characters_with_at_least_1_nummeric));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.password_should_be_8_12_characters_with_at_least_1_nummeric),getString(R.string.ok),R.drawable.warning);

            etPassword.requestFocus();
            return false;
        }


        return true;
    }

}

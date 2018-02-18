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
import com.app.hakeem.pojo.RequestPatientRegistration;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPatientRegistrationStep1 extends Fragment {

    @BindView(R.id.etName)
    EditText etName;
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

    public FragmentPatientRegistrationStep1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_registration_step1, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this,view);

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllValid()) {

                    RequestPatientRegistration requestPatientRegistration = new RequestPatientRegistration();
                    requestPatientRegistration.setFirstName(etName.getText().toString());
                    requestPatientRegistration.setGender(rbMale.isChecked() ? "M" : "F");
                    requestPatientRegistration.setMobileNumber(etMobile.getText().toString());
                    requestPatientRegistration.setEmail(etEmail.getText().toString());
                    requestPatientRegistration.setPassword(etPassword.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(C.USER, requestPatientRegistration);
                    ((ActivityContainer) (getActivity())).fragmnetLoader(C.FRAGMENT_PATIENT_REGISTRATION_STEP2, bundle);

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

//        if(BuildConfig.DEBUG)
//        {
//            etName.setText("Aditya");
//            etPassword.setText("Admin@123");
//            etMobile.setText("967348934789");
//            etEmail.setText("x@yopmail.com");
//
//
//        }

    }



    public boolean isAllValid() {

        if (etName.getText().toString().length() == 0) {
          //  etName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.first_name_required),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().length() < 3) {
         //   etName.setError(getActivity().getResources().getString(R.string.first_name_should_be_more_then_3_character));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.first_name_should_be_more_then_3_character),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().startsWith(".")) {
         //   etName.setError(getActivity().getResources().getString(R.string.name_could_not_starts_with_dot));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.name_could_not_starts_with_dot),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (etMobile.getText().toString().length() == 0) {
            //etMobile.setError(getActivity().getResources().getString(R.string.mobile_no_is_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.mobile_no_is_required),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        } else if (etMobile.getText().length() < 8) {

          //  etMobile.setError(getActivity().getResources().getString(R.string.please_enter_valid_mobile_number));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_enter_valid_mobile_number),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        } /*else if (etMobile.getText().toString().startsWith("0")) {
          //  etMobile.setError(getActivity().getResources().getString(R.string.number_strts_with_zero));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.number_strts_with_zero),getString(R.string.ok),R.drawable.warning);

            etMobile.requestFocus();
            return false;
        }*/ else if (etEmail.getText().toString().length() == 0) {
         //   etEmail.setError(getActivity().getResources().getString(R.string.email_is_required));
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
         //   etPassword.setError(getActivity().getResources().getString(R.string.password_should_be_8_12_characters_with_at_least_1_nummeric));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.password_should_be_8_12_characters_with_at_least_1_nummeric),getString(R.string.ok),R.drawable.warning);

            etPassword.requestFocus();
            return false;
        }


        return true;
    }
}

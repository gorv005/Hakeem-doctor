package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.util.C;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDocterRegistrationStep2 extends Fragment {

    @BindView(R.id.etNationalaty)
    EditText etNationalaty;
    @BindView(R.id.etResidance)
    EditText etResidance;
    @BindView(R.id.etWorkPlace)
    EditText etWorkPlace;
    @BindView(R.id.etHomeLocation)
    EditText etHomeLocation;
    @BindView(R.id.etIdCard)
    EditText etIdCard;
    @BindView(R.id.btnContinue)
    Button btnContinue;
    DoctorRegistration doctorRegistration;
    public FragmentDocterRegistrationStep2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_docter_registration_step2, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        doctorRegistration = (DoctorRegistration) bundle.getSerializable(C.USER);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this,view);
        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllValid()) {

                    doctorRegistration.setNationality(etNationalaty.getText().toString());
                    doctorRegistration.setResidence(etResidance.getText().toString());
                    doctorRegistration.setWorkplace(etWorkPlace.getText().toString());
                    doctorRegistration.setHomeLocation(etHomeLocation.getText().toString());
                    doctorRegistration.setPassport(etIdCard.getText().toString());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(C.USER, doctorRegistration);
                    ((ActivityContainer) (getActivity())).fragmnetLoader(C.FRAGMENT_DOCTOR_REGISTRATION_STEP3, bundle);

                }
            }
        });
    }


    public boolean isAllValid() {

        if (etNationalaty.getText().toString().length() == 0) {
            etNationalaty.setError(getActivity().getResources().getString(R.string.nationility_required));
            etNationalaty.requestFocus();
            return false;
        }  else if (etResidance.getText().toString().length() == 0) {
            etResidance.setError(getActivity().getResources().getString(R.string.residance_required));
            etResidance.requestFocus();
            return false;
        }
        else if (etWorkPlace.getText().toString().length() == 0) {
            etWorkPlace.setError(getActivity().getResources().getString(R.string.workPlace_required));
            etWorkPlace.requestFocus();
            return false;
        }
        else if (etHomeLocation.getText().toString().length() == 0) {
            etHomeLocation.setError(getActivity().getResources().getString(R.string.homeLocation_required));
            etHomeLocation.requestFocus();
            return false;
        }
        else if (etIdCard.getText().toString().length() == 0) {
            etIdCard.setError(getActivity().getResources().getString(R.string.idcard_required));
            etIdCard.requestFocus();
            return false;
        }
        return true;
    }
}

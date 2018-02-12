package com.app.hakeem;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.app.hakeem.fragment.FragmentDependent;
import com.app.hakeem.fragment.FragmentDocterRegistrationStep2;
import com.app.hakeem.fragment.FragmentDoctorPatientDependents;
import com.app.hakeem.fragment.FragmentDoctorProfile;
import com.app.hakeem.fragment.FragmentDoctorRegistrationStep1;
import com.app.hakeem.fragment.FragmentDoctorRegistrationStep3;
import com.app.hakeem.fragment.FragmentDoctorRegistrationStep4;
import com.app.hakeem.fragment.FragmentDoctorsPatientList;
import com.app.hakeem.fragment.FragmentEmrAndHealthTracker;
import com.app.hakeem.fragment.FragmentLogin;
import com.app.hakeem.fragment.FragmentPatientListForEmrAndHealthTracker;
import com.app.hakeem.fragment.FragmentPatientRegistrationStep1;
import com.app.hakeem.fragment.FragmentPatientRegistrationStep2;
import com.app.hakeem.fragment.FragmentRegisterType;
import com.app.hakeem.fragment.FragmentSplash;
import com.app.hakeem.util.C;

public class ActivityContainer extends AppCompatActivity {

    private Fragment fragment;
    public static TextView tvTitle;
    private Bundle bundle;
    private int fragmentAction;
    private Button btnAddDependent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        final Drawable upArrow = getResources().getDrawable(R.drawable.back);
        upArrow.setColorFilter(getResources().getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(upArrow);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnAddDependent = (Button) findViewById(R.id.btnAddDependents);
        btnAddDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fragment instanceof FragmentDependent) {
                    ((FragmentDependent) fragment).openPopUpToAddChild();
                }

            }
        });
        bundle = getIntent().getBundleExtra(C.BUNDLE);
        fragmentAction = getIntent().getIntExtra(C.FRAGMENT_ACTION, C.FRAGMENT_SPLASH);
        fragmnetLoader(fragmentAction, bundle);

    }

    public void fragmnetLoader(int fragmentType, Bundle bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        switch (fragmentType) {
            case C.FRAGMENT_SPLASH:
                getSupportActionBar().hide();
                fragment = new FragmentSplash();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_SPLASH);
                break;
            case C.FRAGMENT_LOGIN:
                getSupportActionBar().hide();
                fragment = new FragmentLogin();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_LOGIN);
                break;

            case C.FRAGMENT_REGISTRATION_TYPE:
                getSupportActionBar().hide();
                fragment = new FragmentRegisterType();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_TYPE);
                break;
            case C.FRAGMENT_DOCTOR_REGISTRATION_STEP1:
                getSupportActionBar().hide();
                fragment = new FragmentDoctorRegistrationStep1();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_TYPE);
                break;
            case C.FRAGMENT_DOCTOR_REGISTRATION_STEP2:
                getSupportActionBar().hide();
                fragment = new FragmentDocterRegistrationStep2();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_TYPE);
                break;

            case C.FRAGMENT_PATIENT_REGISTRATION_STEP1:
                getSupportActionBar().hide();
                fragment = new FragmentPatientRegistrationStep1();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_P_1);
                break;

            case C.FRAGMENT_PATIENT_REGISTRATION_STEP2:
                getSupportActionBar().hide();
                fragment = new FragmentPatientRegistrationStep2();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_P_2);
                break;
            case C.FRAGMENT_DOCTOR_REGISTRATION_STEP3:
                getSupportActionBar().hide();
                fragment = new FragmentDoctorRegistrationStep3();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_TYPE_R_3);
                break;
            case C.FRAGMENT_DOCTOR_REGISTRATION_STEP4:
                getSupportActionBar().hide();
                fragment = new FragmentDoctorRegistrationStep4();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_REGISTER_TYPE_R_4);
                break;
            case C.FRAGMENT_DEPENDENT:
                getSupportActionBar().show();
                tvTitle.setText(R.string.dependent);
                btnAddDependent.setVisibility(View.VISIBLE);
                fragment = new FragmentDependent();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_DEPENDENT);
                break;
            case C.FRAGMENT_PATIENT_EMR_AND_TRACKER:
                getSupportActionBar().show();
                tvTitle.setText(R.string.patient);
                btnAddDependent.setVisibility(View.GONE);
                fragment = new FragmentPatientListForEmrAndHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_EMR_AND_TRACKER);

                break;
            case C.FRAGMENT_EMR_AND_TRACKER:


                getSupportActionBar().show();
                tvTitle.setText(R.string.emr);
                btnAddDependent.setVisibility(View.GONE);
                fragment = new FragmentEmrAndHealthTracker();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_PATIENT_EMR_AND_TRACKER);

                break;
            case C.FRAGMENT_DOCTOR_PROFILE:


                getSupportActionBar().show();
                btnAddDependent.setVisibility(View.GONE);
                fragment = new FragmentDoctorProfile();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_DOCTOR_PROFILE);

                break;
            case C.FRAGMENT_DOCTOR_PATIENT_LIST:


                getSupportActionBar().show();
                btnAddDependent.setVisibility(View.GONE);
                fragment = new FragmentDoctorsPatientList();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_DOCTOR_PATIENT_LIST);

                break;
            case C.FRAGMENT_DOCTOR_PATIENT_DEPENDENT_LIST:
                getSupportActionBar().show();
                btnAddDependent.setVisibility(View.GONE);
                fragment = new FragmentDoctorPatientDependents();
                fragmentTransaction.replace(R.id.container, fragment);
                fragmentTransaction.addToBackStack(C.TAG_FRAGMENT_DOCTOR_PATIENT_DEPENDENT_LIST);

                break;
        }
        fragment.setArguments(bundle);
        fragmentTransaction.commit();
        getSupportFragmentManager().executePendingTransactions();


    }


    @Override
    public void onBackPressed() {


        getSupportFragmentManager().executePendingTransactions();
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {

//            Log.e("CountPop", getSupportFragmentManager().getBackStackEntryCount() + "");

            int fragmentCount = getSupportFragmentManager().getBackStackEntryCount();
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(fragmentCount - 2);
            String fragmentTag = backEntry.getName();

            getSupportFragmentManager().popBackStack();

            getSupportFragmentManager().executePendingTransactions();
            if (fragmentTag != null) {
                switch (fragmentTag) {
//                    case C.TAG_FRAGMENT_BOOK_A_FLIGHT:
//                        tvTitle.setText(C.TAG_FRAGMENT_BOOK_A_FLIGHT);
////                        tvSubTitle.setVisibility(View.GONE);
////                        tvTitle2.setVisibility(View.GONE);
//                        break;
//                    case C.TAG_FRAGMENT_SEARCH_FLIGHT:
//                        tvTitle.setText(C.TAG_FRAGMENT_SEARCH_FLIGHT);
//                        break;
//                    case C.TAG_FRAGMENT_OURDESTINATIONS:
//                        tvTitle.setText(getResources().getString(R.string.our_destinations));
//                        break;
//                    case C.TAG_FRAGMENT_MY_ACCOUNT:
//                        tvTitle.setText(getResources().getString(R.string.my_account));
//                        break;
                }
            }


        } else {
            finish();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return super.onOptionsItemSelected(item);

    }
}

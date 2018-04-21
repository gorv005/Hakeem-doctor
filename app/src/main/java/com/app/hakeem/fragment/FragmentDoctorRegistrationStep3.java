package com.app.hakeem.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterDoctorExperience;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.pojo.Experience;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDoctorRegistrationStep3 extends Fragment {

    @BindView(R.id.etSpeciality)
    EditText etSpeciality;

    @BindView(R.id.spinnerSpeciality)
    Spinner spinnerSpeciality;

    @BindView(R.id.etCurrentGrade)
    EditText etCurrentGrade;

    @BindView(R.id.etSubSpeciality)
    EditText etSubSpeciality;

    @BindView(R.id.spinnerCurrentGrade)
    Spinner spinnerCurrentGrade;

    @BindView(R.id.etClassification)
    EditText etClassification;

    @BindView(R.id.btnAddExperiance)
    Button btnAddExperiance;

    @BindView(R.id.btnContinue)
    Button btnContinue;

    @BindView(R.id.lvExperiance)
    ListView lvExperience;

    EditText etHospitalName;

    EditText etWorkingSince;

    EditText etResignedSince;
    int pos;
    Button btnConfirmExperiance;
    Calendar myCalendar = Calendar.getInstance(Locale.US);
    boolean isWorkingSince=false;
    AdapterDoctorExperience adapterDoctorExperience=null;

    java.util.Date startDate,endDate;

    String[] speciality = new String[]{
            "Speciality",
            "Family and Community",
            "Psychological",
            "Adbominal",
            "Obgyne",
            "Pediatrics"
    };
    String[] currentGrade = new String[]{
            "Current Grade",
            "Specialist",
            "Consultant"

    };

    DoctorRegistration doctorRegistration;
    boolean isExpAdd=false;
    public FragmentDoctorRegistrationStep3() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            doctorRegistration = (DoctorRegistration) bundle.getSerializable(C.USER);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_registration_step3, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        etSpeciality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerSpeciality.performClick();
            }
        });
        etCurrentGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerCurrentGrade.performClick();
            }
        });
        btnAddExperiance.setOnClickListener(mbtnAddExpClickListener);
        btnContinue.setOnClickListener(mBtnContinueClickListner);
        spinnerSpeciality.setOnItemSelectedListener(mSpecialitySelectListner);
        spinnerCurrentGrade.setOnItemSelectedListener(mCurrentGradeSelectListner);

        final List<String> specialityList = new ArrayList<>(Arrays.asList(speciality));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,specialityList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
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
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpeciality.setAdapter(spinnerArrayAdapter);


        final List<String> currentGradeList = new ArrayList<>(Arrays.asList(currentGrade));
        final ArrayAdapter<String> spinnerArrayAdapterCurrentGrade = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,currentGradeList){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
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
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerArrayAdapterCurrentGrade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCurrentGrade.setAdapter(spinnerArrayAdapterCurrentGrade);
        lvExperience.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                pos=position;
                showAlertForConfirm(getActivity(),getString(R.string.delete),getString(R.string.delete_sure),getString(R.string.yes),getString(R.string.no),R.drawable.warning,false);

                return false;
            }
        });
    }
    public  void showAlertForConfirm(final Activity context, String title, String msg, String btnText1, String btnText2, int img, final boolean finishActivity) {


        final LayoutInflater factory = LayoutInflater.from(context);
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_alert_with_two_button, null);
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //   dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(deleteDialogView);



        TextView tvMsg = (TextView) deleteDialogView.findViewById(R.id.tvMsg);
        tvMsg.setText(msg);

        TextView tvTitle = (TextView) deleteDialogView.findViewById(R.id.tvTitle);
        tvTitle.setText(title);
        ImageView ivAlertImage = (ImageView) deleteDialogView.findViewById(R.id.ivAlertImage);
        ivAlertImage.setImageResource(img);
        Button btnDone = (Button) deleteDialogView.findViewById(R.id.btnDone);
        btnDone.setText(btnText1);
        Button btnCancel = (Button) deleteDialogView.findViewById(R.id.btnCancel);
        btnCancel.setText(btnText2);

        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
                adapterDoctorExperience.getAllItem().remove(pos);
                adapterDoctorExperience.notifyDataSetChanged();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();


            }
        });

        dialog.show();


    }

    View.OnClickListener mBtnContinueClickListner=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(isAllValid()){
                for(int i=0;i<speciality.length;i++) {
                    if(etSpeciality.getText().toString().equals(speciality[i])) {
                        doctorRegistration.setSpecialist(""+i);
                    }
                }
                doctorRegistration.setCurrentGrade(etCurrentGrade.getText().toString());
                doctorRegistration.setSubSpecialist(etSubSpeciality.getText().toString());
                doctorRegistration.setClassification(etClassification.getText().toString());
                if(adapterDoctorExperience!=null) {
                    doctorRegistration.setExperience(adapterDoctorExperience.getAllItem());
                }

                Bundle bundle = new Bundle();
                bundle.putSerializable(C.USER, doctorRegistration);
                ((ActivityContainer) (getActivity())).fragmnetLoader(C.FRAGMENT_DOCTOR_REGISTRATION_STEP4, bundle);
            }
        }
    } ;

    AdapterView.OnItemSelectedListener mSpecialitySelectListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            etSpeciality.setText(speciality[position]);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    AdapterView.OnItemSelectedListener mCurrentGradeSelectListner=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            etCurrentGrade.setText(currentGrade[position]);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
    public boolean isAllValid() {
        if (etSpeciality.getText().toString().length() == 0) {
         //   etSpeciality.setError(getActivity().getResources().getString(R.string.speciality_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.speciality_required),getString(R.string.ok),R.drawable.warning);

            etSpeciality.requestFocus();
            return false;
        } else if (etCurrentGrade.getText().toString().length() == 0) {
          //  etCurrentGrade.setError(getActivity().getResources().getString(R.string.currentGrade_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.currentGrade_required),getString(R.string.ok),R.drawable.warning);

            etCurrentGrade.requestFocus();
            return false;
        } else if (etSubSpeciality.getText().toString().length() == 0) {
         //   etSubSpeciality.setError(getActivity().getResources().getString(R.string.subSpeciality_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.subSpeciality_required),getString(R.string.ok),R.drawable.warning);

            etSubSpeciality.requestFocus();
            return false;
        }
        else if (etClassification.getText().toString().length() == 0) {
          //  etClassification.setError(getActivity().getResources().getString(R.string.classification_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.classification_required),getString(R.string.ok),R.drawable.warning);

            etClassification.requestFocus();
            return false;
        }
        else if (adapterDoctorExperience==null || adapterDoctorExperience.getAllItem()==null ||adapterDoctorExperience.getAllItem().size()==0) {
            //Toast.makeText(getActivity(),getString(R.string.require_add_experience),Toast.LENGTH_LONG).show();
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.require_add_experience),getString(R.string.ok),R.drawable.warning);

            return false;
        }

        return true;
    }

    View.OnClickListener mbtnAddExpClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            openPopUpToAddExperience();
        }
    };

    private void openPopUpToAddExperience() {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_experience, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);

        final ArrayList<Experience> experiences = new ArrayList<>();
        if (adapterDoctorExperience == null) {
            adapterDoctorExperience = new AdapterDoctorExperience(getActivity(), experiences);
            lvExperience.setAdapter(adapterDoctorExperience);
        }

        etHospitalName = (EditText) deleteDialogView.findViewById(R.id.etHospitalName);
         etWorkingSince = (EditText) deleteDialogView.findViewById(R.id.etWorkingSince);
         etResignedSince = (EditText) deleteDialogView.findViewById(R.id.etResignedSince);

         btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);



        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfExp()) {
                    Experience experience=new Experience();
                    experience.setHospitalName(etHospitalName.getText().toString());
                    experience.setWorkedSince(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResignedSince(Util.getDateFromString(etResignedSince.getText().toString()));
                   // experiences.add(experience);
                    adapterDoctorExperience.addItem(experience);
                    isExpAdd=true;
                   Util.setListViewHeightBasedOnChildren(lvExperience);
                    dialog.dismiss();
                }
            }
        });

        etWorkingSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=true;
                openCalender();
            }
        });

        etResignedSince.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isWorkingSince=false;
                openCalender();
            }
        });
        dialog.show();


    }
    private void openCalender() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() );
        datePickerDialog.show();


    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = C.DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        if (isWorkingSince) {
            startDate=myCalendar.getTime();
            etWorkingSince.setText(sdf.format(myCalendar.getTime()));
        } else {
            endDate=myCalendar.getTime();

            etResignedSince.setText(sdf.format(myCalendar.getTime()));
        }

    }

    public boolean isAllVaildDetailOfExp() {
        if (etHospitalName.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.hospital_name_required),getString(R.string.ok),R.drawable.warning);

            etHospitalName.requestFocus();
            return false;
        }
       else if (etWorkingSince.getText().toString().length() == 0) {
           // etWorkingSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.working_since_required),getString(R.string.ok),R.drawable.warning);

            etWorkingSince.requestFocus();
            return false;
        }
        else if (etResignedSince.getText().toString().length() == 0) {
          //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.resigned_date_required),getString(R.string.ok),R.drawable.warning);

            etResignedSince.requestFocus();
            return false;
        }
        else if (endDate!=null) {
            //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            if (!Util.isValidToFromDates(startDate,endDate)) {
                //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
                Util.showAlert(getActivity(), getString(R.string.error), getString(R.string.resigned_date_should_be_less_than_joining), getString(R.string.ok), R.drawable.warning);

                etResignedSince.requestFocus();
                return false;
            }
        }

        return true;
    }

}

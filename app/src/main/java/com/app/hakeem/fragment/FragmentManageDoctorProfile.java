package com.app.hakeem.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterDoctorEducationManage;
import com.app.hakeem.adapter.AdapterDoctorExperienceManage;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorProfile;
import com.app.hakeem.pojo.Education;
import com.app.hakeem.pojo.ExperienceDoc;
import com.app.hakeem.util.C;
import com.app.hakeem.util.ImageLoader;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentManageDoctorProfile extends Fragment {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etAbountMe)
    EditText etAbountMe;
    @BindView(R.id.lvEducation)
    ListView lvEducation;
    @BindView(R.id.tvLocation)
    TextView tvLocation;
    @BindView(R.id.lvExperiance)
    ListView lvExperiance;
    @BindView(R.id.ivImageProfile)
    ImageView ivImageProfile;
    @BindView(R.id.ivAddExperience)
    ImageView ivAddExperience;
    @BindView(R.id.ivAddEducation)
    ImageView ivAddEducation;
    private Dialog dialog;
    AdapterDoctorExperienceManage adapterDoctorExperienceManage;
    AdapterDoctorEducationManage adapterDoctorEducationManage;
    ImageLoader imageLoader;
    boolean isExpAdd=false;
    java.util.Date startDate,endDate;
    EditText etHospitalName;

    EditText etWorkingSince;
    EditText etUniversityName;

    EditText etDescription;
    EditText etResignedSince;
    Button btnConfirmExperiance;
    Calendar myCalendar = Calendar.getInstance(Locale.US);
    boolean isWorkingSince=false;
    boolean isEdit=false;
    public FragmentManageDoctorProfile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manage_doctor_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        imageLoader=new ImageLoader(getActivity());
        ivAddEducation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpToAddEducation();
            }
        });

        ivAddExperience.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            openPopUpToAddExperience();
            }
        });
        final List<Education> educations = new ArrayList<>();

        adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),educations,isEdit);
            lvEducation.setAdapter(adapterDoctorEducationManage);

        final List<ExperienceDoc> experiences = new ArrayList<>();

        adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),experiences,isEdit);
        lvExperiance.setAdapter(adapterDoctorExperienceManage);
        disableViews();
        getDocData();

        ActivityContainer.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isEdit) {
                    ActivityContainer.tvEdit.setText(getString(R.string.save));
                    isEdit = true;
                    adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),adapterDoctorExperienceManage.getAllItem(),isEdit);
                    lvExperiance.setAdapter(adapterDoctorExperienceManage);
                    adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),adapterDoctorEducationManage.getAllItem(),isEdit);
                    lvEducation.setAdapter(adapterDoctorEducationManage);
                    enableView();
                }
                else {
                    ActivityContainer.tvEdit.setText(getString(R.string.Edit));
                    isEdit = false;
                    adapterDoctorExperienceManage=new AdapterDoctorExperienceManage(getActivity(),adapterDoctorExperienceManage.getAllItem(),isEdit);
                    lvExperiance.setAdapter(adapterDoctorExperienceManage);
                    adapterDoctorEducationManage=new AdapterDoctorEducationManage(getActivity(),adapterDoctorEducationManage.getAllItem(),isEdit);
                    lvEducation.setAdapter(adapterDoctorEducationManage);
                    disableViews();
                }
            }
        });
    }


    void disableViews(){

        //viewUploadImage.setClickable(false);
        etUserName.setFocusable(false);
        etAbountMe.setFocusable(false);
        tvLocation.setFocusable(false);
        etUserName.setBackgroundResource(0);
        etAbountMe.setBackgroundResource(0);
        tvLocation.setBackgroundResource(0);
        ivAddExperience.setVisibility(View.GONE);
        ivAddEducation.setVisibility(View.GONE);

    }
    public boolean isAllVaildDetailOfEducation() {
        if (etUniversityName.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.University_required),getString(R.string.ok),R.drawable.warning);

            etUniversityName.requestFocus();
            return false;
        }

        else if (etDescription.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.description_required),getString(R.string.ok),R.drawable.warning);

            etDescription.requestFocus();
            return false;
        }
        return true;
    }

    public boolean isAllVaildDetailOfExp() {
        if (etHospitalName.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.hospital_name_required),getString(R.string.ok),R.drawable.warning);

            etHospitalName.requestFocus();
            return false;
        }
        else if (etWorkingSince.getText().toString().length() == 0) {
            // etWorkingSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.working_since_required),getString(R.string.ok),R.drawable.warning);

            etWorkingSince.requestFocus();
            return false;
        }
        else if (etResignedSince.getText().toString().length() == 0) {
            //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.resigned_date_required),getString(R.string.ok),R.drawable.warning);

            etResignedSince.requestFocus();
            return false;
        }
        else if (endDate!=null) {
            //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
            if (!Util.isValidToFromDates(startDate,endDate)) {
                //  etResignedSince.setError(getActivity().getResources().getString(R.string.first_name_required));
                Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.resigned_date_should_be_less_than_joining), getString(R.string.ok), R.drawable.warning);

                etResignedSince.requestFocus();
                return false;
            }
        }
        else if (etDescription.getText().toString().length() == 0) {
            //etHospitalName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.description_required),getString(R.string.ok),R.drawable.warning);

            etDescription.requestFocus();
            return false;
        }
        return true;
    }

    private void openPopUpToAddEducation() {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_education, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etUniversityName = (EditText) deleteDialogView.findViewById(R.id.etUniversityName);

        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);



        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfEducation()) {
                    Education education=new Education();
                    education.setEduId("");

                    education.setUniversityName(etUniversityName.getText().toString());
                    education.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorEducationManage.addItem(education);

                    Util.setListViewHeightBasedOnChildren(lvEducation);
                    dialog.dismiss();
                }
            }
        });


        dialog.show();


    }

    private void openPopUpToAddExperience() {

        startDate=null;
        endDate=null;
        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_experience_manage, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);



        etHospitalName = (EditText) deleteDialogView.findViewById(R.id.etHospitalName);
        etWorkingSince = (EditText) deleteDialogView.findViewById(R.id.etWorkingSince);
        etResignedSince = (EditText) deleteDialogView.findViewById(R.id.etResignedSince);
        etDescription = (EditText) deleteDialogView.findViewById(R.id.etDescription);

        btnConfirmExperiance = (Button) deleteDialogView.findViewById(R.id.btnConfirmAddExperiance);



        btnConfirmExperiance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfExp()) {
                    ExperienceDoc experience=new ExperienceDoc();
                    experience.setExpId("");
                    experience.setHospitalName(etHospitalName.getText().toString());
                    experience.setWorkedSince(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResignedSince(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setJoining_date(Util.getDateFromString(etWorkingSince.getText().toString()));
                    experience.setResigned_date(Util.getDateFromString(etResignedSince.getText().toString()));
                    experience.setDescription(etDescription.getText().toString());

                    // experiences.add(experience);
                    adapterDoctorExperienceManage.addItem(experience);
                    isExpAdd=true;
                    Util.setListViewHeightBasedOnChildren(lvExperiance);
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

    void enableView(){

        //viewUploadImage.setClickable(false);
        etUserName.setFocusable(true);
        etAbountMe.setFocusable(true);
        tvLocation.setFocusable(true);
        etUserName.setBackgroundResource(R.drawable.edittext_deselect_blue);
        etAbountMe.setBackgroundResource(R.drawable.edittext_deselect_blue);
        tvLocation.setBackgroundResource(R.drawable.edittext_deselect_blue);
        ivAddExperience.setVisibility(View.VISIBLE);
        ivAddEducation.setVisibility(View.VISIBLE);

    }
    private void getDocData() {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("doctor_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId() + "");
        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        VolleyService volleyService = new VolleyService(getActivity());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());
                dialog.dismiss();

                try {
                    Gson gson = new Gson();
                    DoctorProfile doctorProfile = gson.fromJson(response.toString(), DoctorProfile.class);
                    if (doctorProfile.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        imageLoader.DisplayImage(doctorProfile.getData().getUpload(),ivImageProfile);
                        etUserName.setText(getString(R.string.dr)+""+doctorProfile.getData().getName());
                        tvLocation.setText(doctorProfile.getData().getHomeLocation());
                        etAbountMe.setText(doctorProfile.getData().getAboutMe());
                        for (int i=0;i<doctorProfile.getData().getEducation().size();i++) {
                            adapterDoctorEducationManage.addItem(doctorProfile.getData().getEducation().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvEducation);
                        for (int i=0;i<doctorProfile.getData().getExperience().size();i++) {
                            doctorProfile.getData().getExperience().get(i).setJoining_date(doctorProfile.getData().getExperience().get(i).getWorkedSince());
                            doctorProfile.getData().getExperience().get(i).setResigned_date(doctorProfile.getData().getExperience().get(i).getResignedSince());
                            adapterDoctorExperienceManage.addItem(doctorProfile.getData().getExperience().get(i));
                        }
                        Util.setListViewHeightBasedOnChildren(lvExperiance);
                      //  Util.showAlertBackPress(getActivity(),getString(R.string.success),doctorProfile.getMessage(),getString(R.string.ok),R.drawable.success,true);

                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), doctorProfile.getMessage(), getString(R.string.ok), R.drawable.error);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                dialog.dismiss();
                Log.e("Response :", error.toString());

            }
        }, "login", C.API_GET_DOC_DATA, Util.getHeader(getActivity()), obj);
    }

}

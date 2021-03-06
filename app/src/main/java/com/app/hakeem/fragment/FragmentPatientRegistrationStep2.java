package com.app.hakeem.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterCityList;
import com.app.hakeem.adapter.AdapterDependent;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.CityList;
import com.app.hakeem.pojo.GeneralPojoKeyValue;
import com.app.hakeem.pojo.LoginCredential;
import com.app.hakeem.pojo.RequestPatientRegistration;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.ResponseLogin;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPatientRegistrationStep2 extends Fragment  {


    @BindView(R.id.tvBirthDay)
    TextView tvBirthDay;

    @BindView(R.id.tvCity)
    TextView tvCity;

    @BindView(R.id.checkBox_terms_and_cond)
    CheckBox checkBox_terms_and_cond;

    @BindView(R.id.tvTerm_and_condition)
    TextView tvTermsAndCondition;

    @BindView(R.id.btnAddDependents)
    Button btnAddDependent;

    @BindView(R.id.btnCompleteRegistration)
    Button btnCompleteRegistration;

    @BindView(R.id.lvDependent)
    ListView lvDependent;


    RequestPatientRegistration requestPatientRegistration;

    AdapterDependent adapterDependent;
    Calendar myCalendar = Calendar.getInstance();

    private AdapterCityList adapter;
    private boolean isDependent = false;
    private EditText etName;
    private TextView tvRelationship;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private TextView tvDOB;
    private Button btnDone;
    private Spinner spinnerRelationship;
    private Dialog progressDialog;
    private GeneralPojoKeyValue generalPojoKeyValue;
    private Dialog dialog;
    String dob,city;

    public FragmentPatientRegistrationStep2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            requestPatientRegistration = (RequestPatientRegistration) bundle.getSerializable(C.USER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_patient_registration_step2, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        tvBirthDay.setText(dob);
        tvCity.setText(city);
        btnCompleteRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAllValid()) {
                  //  Util.setAppLocale(C.English,getActivity());

                    requestPatientRegistration.setDob(Util.getDateFromString(tvBirthDay.getText().toString()));
                    requestPatientRegistration.setCountryCode(tvCity.getText().toString());
                    if(adapterDependent!=null) {
                        requestPatientRegistration.setChildren(adapterDependent.getAllItem());
                    }
                    else {
                        adapterDependent=new AdapterDependent(getActivity(),new ArrayList<Child>());
                        requestPatientRegistration.setChildren(adapterDependent.getAllItem());

                    }
                    requestPatientRegistration.setUserGroup(C.USER_PATIENT);
                  //  Util.setAppLocale(C.ARABIC,getActivity());

                    doPatientRegister(requestPatientRegistration);
                }
            }
        });
        btnAddDependent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopUpToAddChild();
            }
        });
        tvTermsAndCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dob=tvBirthDay.getText().toString();
                city=tvCity.getText().toString();
                ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_TERMS_AND_CONDITION,null);
            }
        });
        tvBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDependent = false;
                openCalender();
            }
        });
        tvCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCitySelector();
            }
        });
        try {
            if (adapterDependent != null) {
                lvDependent.setAdapter(adapterDependent);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

/*
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.tvBirthDay:
                isDependent = false;
                openCalender();
                break;


            case R.id.tvCity:
                openCitySelector();
                break;
            case R.id.btnAddDependents:
                openPopUpToAddChild();
                break;
            case R.id.btnCompleteRegistration:
                if (isAllValid()) {
                    requestPatientRegistration.setDob(Util.getDateFromString(tvBirthDay.getText().toString()));
                    requestPatientRegistration.setCountryCode(tvCity.getText().toString());
                    requestPatientRegistration.setChildren(adapterDependent.getAllItem());
                    requestPatientRegistration.setUserGroup(C.USER_PATIENT);
                    doPatientRegister(requestPatientRegistration);
                }
                break;
        }

    }
*/

    private void openCitySelector() {


        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.pop_up_city_list);
        dialog.setTitle(R.string.select_city);

        if (adapter == null) {
            String city = Util.loadCityJson(getActivity());

            Gson gson = new Gson();

            CityList cityList = gson.fromJson(city, CityList.class);
            adapter = new AdapterCityList(getActivity(), cityList.getCities());
        }
        ListView lvCities = (ListView) dialog.findViewById(R.id.List);
        lvCities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tvCity.setText(adapter.getItem(position).getName());
                dialog.dismiss();
            }
        });
        lvCities.setAdapter(adapter);
        dialog.show();


    }

    private void openCalender() {
        //Util.setAppLocale(C.English,getActivity());
        Calendar myCalendar = Calendar.getInstance(Locale.US);
        myCalendar.setTimeInMillis(System.currentTimeMillis());

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        if (isDependent) {
//            myCalendar.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR) - 17);
            datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
        }
        else {
         //   myCalendar.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR) - 18);

            datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());
            //datePickerDialog.getDatePicker().setMaxDate(myCalendar.getTimeInMillis());

        }

        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
         //   Util.setAppLocale(C.ARABIC,getActivity());

            updateLabel(myCalendar);
        }

    };

    private void updateLabel(Calendar myCalendar) {

        String myFormat = C.DATE_FORMAT;
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        if (isDependent) {

            tvDOB.setText(sdf.format(myCalendar.getTime()));
        } else {
            tvBirthDay.setText(sdf.format(myCalendar.getTime()));
        }

    }

    private void doPatientRegister(final RequestPatientRegistration requestPatientRegistration) {

        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        final Gson gson = new Gson();
        String json = gson.toJson(requestPatientRegistration);
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
                progressDialog.dismiss();

                Response responseServer = gson.fromJson(response.toString(), Response.class);


                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {

                  //  Util.showToast(getActivity(), responseServer.getMessage(), false);

                    Handler handler =new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            doLogin(requestPatientRegistration.getEmail(),requestPatientRegistration.getPassword());

                        }
                    },2000);

                } else {
                   // Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),getString(R.string.error),responseServer.getMessage(),getString(R.string.ok),R.drawable.error,false);

                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
              //  Util.showToast(getActivity(), R.string.network_error, false);
                Util.showAlertForToast(getActivity(),getString(R.string.error), getString(R.string.network_error),getString(R.string.ok),R.drawable.error,false);


            }
        }, "callback", C.API_REGISTER_PATIENT, Util.getHeader(getActivity()), obj);


    }

    private void openPopUpToAddChild() {


        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_dependent, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);

        ArrayList<Child> children = new ArrayList<>();
        if (adapterDependent == null) {
            adapterDependent = new AdapterDependent(getActivity(), children);

        }
        lvDependent.setAdapter(adapterDependent);

        etName = (EditText) deleteDialogView.findViewById(R.id.etName);
        tvRelationship = (TextView) deleteDialogView.findViewById(R.id.tvRelationship);
        rbMale = (RadioButton) deleteDialogView.findViewById(R.id.rbMale);
        rbFemale = (RadioButton) deleteDialogView.findViewById(R.id.rbFemale);
        tvDOB = (TextView) deleteDialogView.findViewById(R.id.tvBirthDay);
        btnDone = (Button) deleteDialogView.findViewById(R.id.btnDone);
        spinnerRelationship = (Spinner) deleteDialogView.findViewById(R.id.spinnerRelationship);
        ArrayAdapter<GeneralPojoKeyValue> spinnerAcadQualsArrayAdapter = new ArrayAdapter<GeneralPojoKeyValue>(
                getActivity(), R.layout.spinner_item_new, Util.getRelationList()) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spinnerAcadQualsArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRelationship.setAdapter(spinnerAcadQualsArrayAdapter);
        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 generalPojoKeyValue =Util.getRelationList().get(position);
                tvRelationship.setText(generalPojoKeyValue.getValue());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        tvRelationship.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerRelationship.performClick();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isAllVaildDetailOfDependent()) {

                    Child child = new Child();
                    child.setName(etName.getText().toString());
                    child.setDob(Util.getDateFromString(tvDOB.getText().toString()));
                    child.setGender(rbMale.isChecked() ? "M" : "F");
                    child.setRelationPojo(generalPojoKeyValue);
                    adapterDependent.addItem(child);
                    Util.setListViewHeightBasedOnChildren(lvDependent);
                    dialog.dismiss();
                }
            }
        });

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDependent = true;
                openCalender();
            }
        });

        dialog.show();


    }

    public boolean isAllValid() {
        if (tvBirthDay.getText().toString().length() == 0) {
        //    tvDOB.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.please_enter_date_of_birth),getString(R.string.ok),R.drawable.warning);

            tvBirthDay.requestFocus();
            return false;
        } else if (tvCity.getText().toString().length() == 0) {
          //  tvCity.setError(getActivity().getResources().getString(R.string.please_enter_city));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.please_enter_city),getString(R.string.ok),R.drawable.warning);

            tvCity.requestFocus();
            return false;
        } /*else if (adapterDependent.getAllItem().size() == 0) {
          //  Util.showToast(getActivity(), R.string.please_add_atleast_one_depandent, false);
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_add_atleast_one_depandent),getString(R.string.ok),R.drawable.warning);

            return false;
        }*/
        if(!checkBox_terms_and_cond.isChecked()){
            Util.showAlert(getActivity(), getString(R.string.warning), getString(R.string.please_select_terms_and_condition), getString(R.string.ok), R.drawable.warning);
            return false;
        }
        return true;
    }

    public boolean isAllVaildDetailOfDependent() {
        if (etName.getText().toString().length() == 0) {
         //   etName.setError(getActivity().getResources().getString(R.string.first_name_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.first_name_required),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().length() < 3) {
          //  etName.setError(getActivity().getResources().getString(R.string.first_name_should_be_more_then_3_character));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.first_name_should_be_more_then_3_character),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().startsWith(".")) {
          //  etName.setError(getActivity().getResources().getString(R.string.name_could_not_starts_with_dot));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.name_could_not_starts_with_dot),getString(R.string.ok),R.drawable.warning);

            etName.requestFocus();
            return false;
        } else if (tvRelationship.getText().toString().length() == 0) {
         //   tvRelationship.setError(getActivity().getResources().getString(R.string.please_enter_relationship));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.please_enter_relationship),getString(R.string.ok),R.drawable.warning);

            tvRelationship.requestFocus();
            return false;
        } else if (tvDOB.getText().toString().length() == 0) {
          //  tvDOB.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.please_enter_date_of_birth),getString(R.string.ok),R.drawable.warning);

            tvDOB.requestFocus();
            return false;
        }
        else if (generalPojoKeyValue==null ||  generalPojoKeyValue.getKey().equals("0")) {
            //  tvDOB.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.please_select_relation),getString(R.string.ok),R.drawable.warning);

            tvDOB.requestFocus();
            return false;
        }

        return true;
    }




    private void doLogin(String email, String password) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        LoginCredential loginCredential = new LoginCredential();
        loginCredential.setEmail(email);
        loginCredential.setPassword(password);
        Gson gson = new Gson();
        String json = gson.toJson(loginCredential);
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
                    ResponseLogin responseLogin = gson.fromJson(response.toString(), ResponseLogin.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                      /*  SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                        SharedPreference.getInstance(getActivity()).setString(C.AUTH_TOKEN, responseLogin.getUser().getToken());
                        SharedPreference.getInstance(getActivity()).setUser(C.LOGIN_USER,responseLogin.getUser());
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,true);
                        Intent intent = new Intent(getActivity(), ActivityMain.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        getActivity().startActivity(intent);*/

                        Bundle bundle=new Bundle();
                        bundle.putSerializable(C.DETAILS,requestPatientRegistration);
                        bundle.putBoolean(C.IS_DOC,false);
                        bundle.putString(C.USER_ID,responseLogin.getUser().getUserId());

                        ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_OTP,bundle);
                    } else {

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, false);
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,false);
                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());

            }
        }, "login", C.API_LOGIN, Util.getHeader(getActivity()), obj);


    }



}

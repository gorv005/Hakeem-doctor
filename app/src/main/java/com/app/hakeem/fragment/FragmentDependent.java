package com.app.hakeem.fragment;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterPatientList;
import com.app.hakeem.interfaces.DependentDelete;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.GeneralPojoKeyValue;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDependent extends Fragment implements DependentDelete {

    @BindView(R.id.lvDependent)
    ListView lvDependent;
    private Dialog progressDialog;
    private EditText etName;
    private TextView tvRelationship;
    private RadioButton rbMale;
    private RadioButton rbFemale;
    private TextView tvDOB;
    private Button btnDone;
    private Spinner spinnerRelationship;
    private Child child;
    private View header;


    public FragmentDependent() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dependent, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);


        if (Util.isNetworkConnectivity(getActivity())) {
            getDependentList();
        } else {
            Util.showToast(getActivity(), R.string.please_connect_to_the_internet, true);
        }

    }

    private void getDependentList() {

        lvDependent.removeHeaderView(header);
        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("user_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
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
                progressDialog.dismiss();
                Response responseServer = gson.fromJson(response.toString(), Response.class);
                if (responseServer.getStatusCode().equals(C.STATUS_SUCCESS)) {

                    lvDependent.removeHeaderView(header);
                    header = getActivity().getLayoutInflater().inflate(R.layout.item_dependent, null);
                    header.findViewById(R.id.btnDelete).setVisibility(View.GONE);
                    TextView tvName = (TextView) header.findViewById(R.id.tvName);
                    tvName.setText(getString(R.string.main_profile));
                    lvDependent.addHeaderView(header);
                    AdapterPatientList adapterPatientList = new AdapterPatientList(FragmentDependent.this,getActivity(), responseServer.getPatient().getChildrens(),false);
                    lvDependent.setAdapter(adapterPatientList);

                } else {
                //    Util. showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),responseServer.getMessage(),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }
            }

            @Override
            public void notifyError(String requestType, String error) {
                Log.e("Response", error.toString());
                progressDialog.dismiss();
                Util.showToast(getActivity(), R.string.network_error, false);
            }
        }, "callback", C.API_REGISTER_FETCH_PATIENT, Util.getHeader(getActivity()), obj);


    }

    public void openPopUpToAddChild() {


        final LayoutInflater factory = LayoutInflater.from(getActivity());
        final View deleteDialogView = factory.inflate(
                R.layout.dialog_add_dependent, null);
        final AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setView(deleteDialogView);

        child = new Child();

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
        spinnerRelationship.setAdapter(spinnerAcadQualsArrayAdapter);
        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                child.setRelationPojo(Util.getRelationList().get(position));
                tvRelationship.setText(Util.getRelationList().get(position).getValue());

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

                    child.setName(etName.getText().toString());
                    child.setDob(Util.getDateFromString(tvDOB.getText().toString()));
                    child.setGender(rbMale.isChecked() ? "M" : "F");
                    child.setParantId(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());

                    dialog.dismiss();
                    addDependent(child);
                }
            }
        });

        tvDOB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalender();
            }
        });

        dialog.show();


    }

    private void openCalender() {
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.setTimeInMillis(System.currentTimeMillis());

        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));

        myCalendar.set(Calendar.YEAR, myCalendar.get(Calendar.YEAR) - 17);
        datePickerDialog.getDatePicker().setMinDate(myCalendar.getTimeInMillis());

        datePickerDialog.show();
    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            Calendar myCalendar = Calendar.getInstance();

            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(myCalendar);
        }

    };

    private void updateLabel(Calendar myCalendar) {

        String myFormat = "dd/MMM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        tvDOB.setText(sdf.format(myCalendar.getTime()));
    }


    public boolean isAllVaildDetailOfDependent() {
        if (etName.getText().toString().length() == 0) {
            etName.setError(getActivity().getResources().getString(R.string.first_name_required));
            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().length() < 3) {
            etName.setError(getActivity().getResources().getString(R.string.first_name_should_be_more_then_3_character));
            etName.requestFocus();
            return false;
        } else if (etName.getText().toString().trim().startsWith(".")) {
            etName.setError(getActivity().getResources().getString(R.string.name_could_not_starts_with_dot));
            etName.requestFocus();
            return false;
        } else if (tvRelationship.getText().toString().length() == 0) {
            tvRelationship.setError(getActivity().getResources().getString(R.string.please_enter_relationship));
            tvRelationship.requestFocus();
            return false;
        } else if (tvDOB.getText().toString().length() == 0) {
            tvDOB.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            tvDOB.requestFocus();
            return false;
        }


        return true;
    }

    private void addDependent(Child child) {


        progressDialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        progressDialog.show();

        final Gson gson = new Gson();
        String json = gson.toJson(child);
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

                   // Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),responseServer.getMessage(),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                    getDependentList();

                } else {
                  //  Util.showToast(getActivity(), responseServer.getMessage(), false);
                    Util.showAlertForToast(getActivity(),responseServer.getMessage(),responseServer.getMessage(),getString(R.string.ok),R.drawable.warning,false);

                }

            }

            @Override
            public void notifyError(String requestType, String error) {
                progressDialog.dismiss();
                Util.showToast(getActivity(), R.string.network_error, false);

            }
        }, "callback", C.API_ADD_DPENDENT, Util.getHeader(getActivity()), obj);


    }


    @Override
    public void notifyDependentDeleted() {
        getDependentList();
    }
}

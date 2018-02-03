package com.app.hakeem.fragment;


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
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.adapter.AdapterCityList;
import com.app.hakeem.adapter.AdapterDependent;
import com.app.hakeem.pojo.Child;
import com.app.hakeem.pojo.CityList;
import com.app.hakeem.pojo.RequestPatientRegistration;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentPatientRegistrationStep2 extends Fragment implements View.OnClickListener {


    @BindView(R.id.tvBirthDay)
    TextView tvBirthDay;

    @BindView(R.id.tvCity)
    TextView tvCity;

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


    public FragmentPatientRegistrationStep2() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        requestPatientRegistration = (RequestPatientRegistration) bundle.getSerializable(C.USER);
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

        btnCompleteRegistration.setOnClickListener(this);
        btnAddDependent.setOnClickListener(this);
        tvTermsAndCondition.setOnClickListener(this);
        tvBirthDay.setOnClickListener(this);
        tvCity.setOnClickListener(this);
    }

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
                    requestPatientRegistration.setCity(tvCity.getText().toString());
                    requestPatientRegistration.setChildren(adapterDependent.getAllItem());
                    doPatientRegister(requestPatientRegistration);
                }
                break;
        }

    }

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
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());
        c.set(Calendar.YEAR,c.get(Calendar.YEAR)-18);


        if (isDependent)
            datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        else {

            datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
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
            updateLabel();
        }

    };

    private void updateLabel() {

        String myFormat = "dd/MMM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
        if (isDependent) {

            tvDOB.setText(sdf.format(myCalendar.getTime()));
        } else {
            tvBirthDay.setText(sdf.format(myCalendar.getTime()));
        }

    }

    private void doPatientRegister(RequestPatientRegistration requestPatientRegistration) {


        //Todo API Hit

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
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
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
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRelationship.setAdapter(spinnerArrayAdapter);
        spinnerRelationship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvRelationship.setText(Util.getRelationList().get(position));
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
                    child.setDob(tvDOB.getText().toString());
                    child.setGender(rbMale.isChecked() ? "M" : "F");
                    child.setRelationship(tvRelationship.getText().toString());
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
        if (tvDOB.getText().toString().length() == 0) {
            tvDOB.setError(getActivity().getResources().getString(R.string.please_enter_date_of_birth));
            tvDOB.requestFocus();
            return false;
        } else if (tvCity.getText().toString().length() == 0) {
            tvCity.setError(getActivity().getResources().getString(R.string.please_enter_city));
            tvCity.requestFocus();
            return false;
        } else if (adapterDependent.getAllItem().size() == 0) {
            Util.showToast(getActivity(), R.string.please_add_atleast_one_depandent, false);
            return false;
        }


        return true;
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

    void openRelationshipPopUp() {

    }
}

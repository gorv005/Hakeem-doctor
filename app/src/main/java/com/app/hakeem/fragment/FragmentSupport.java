package com.app.hakeem.fragment;


import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.pojo.LoginCredential;
import com.app.hakeem.pojo.RequestPatientRegistration;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.ResponseLogin;
import com.app.hakeem.pojo.Support;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSupport extends Fragment {

    @BindView(R.id.etEmail)
    EditText etEmail;
    @BindView(R.id.etMobile)
    EditText etMobile;
    @BindView(R.id.etProblemType)
    EditText etProblemType;
    @BindView(R.id.spinnerProblemType)
    Spinner spinnerProblemType;
    @BindView(R.id.etProblemTitle)
    EditText etProblemTitle;
    @BindView(R.id.etProbDesc)
    EditText etProbDesc;
    @BindView(R.id.btnSend)
    Button btnSend;
    private Dialog dialog;
    String[] problemType ;
    public FragmentSupport() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_support, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        etMobile.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (etMobile.getText().length() == 0)
                    etMobile.setText(C.NUMBER_FORMAT);
                return false;
            }
        });
        etMobile.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus && etMobile.getText().toString().equals(C.NUMBER_FORMAT))
                    etMobile.setText("");
            }
        });
        etMobile.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            /*    if (s != null && s.length() == 5 && (s.charAt(4) < '7')) {
                    etMobile.setText("");
                    etMobile.setError("Number should start with 9,8 and 7");
                }*/
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().startsWith(C.NUMBER_FORMAT)) {
                    etMobile.setText(C.NUMBER_FORMAT);
                    Selection.setSelection(etMobile.getText(), etMobile
                            .getText().length());

                }
            }
        });
        etProblemType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerProblemType.performClick();
            }
        });
        problemType = new String[]{getString(R.string.problem_type), getString(R.string.Technical_issue),
                getString(R.string.Payments_issue),getString(R.string.Other)};
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isAllValid()){
                    Support support=new Support();
                    if(SharedPreference.getInstance(getActivity()).getBoolean(C.IS_LOGIN)){
                        support.setUserId(SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId());
                    }
                    else {
                        support.setUserId("");
                    }
                    support.setEmail(etEmail.getText().toString());
                    String strMobile[]=etMobile.getText().toString().split("-");
                    support.setPhone(strMobile[0]+strMobile[1]);
                    support.setProblemType(etProblemType.getText().toString());
                    support.setProblemDescription(etProbDesc.getText().toString());
                    support.setProblemTitle(etProblemTitle.getText().toString());
                    postSupport(support);
                }
            }
        });

        spinnerProblemType.setOnItemSelectedListener(mProblemTypeClickLister);
        final List<String> problemTypeList = new ArrayList<>(Arrays.asList(problemType));
        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(
                getActivity(),R.layout.spinner_item_new,problemTypeList){
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
        spinnerProblemType.setAdapter(spinnerArrayAdapter);
    }
    AdapterView.OnItemSelectedListener mProblemTypeClickLister=new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            etProblemType.setText(problemType[position]);

        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };


    private void postSupport(final Support support) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();

        Gson gson = new Gson();
        String json = gson.toJson(support);
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
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        Util.showAlertBackPress(getActivity(),getString(R.string.success),responseLogin.getMessage(),getString(R.string.ok),R.drawable.success,true);

                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), responseLogin.getMessage(), getString(R.string.ok), R.drawable.error);
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
        }, "login", C.API_SUPPORT, Util.getHeader(getActivity()), obj);


    }


    private boolean isAllValid() {


       if (etEmail.getText().toString().length() == 0) {
            //   etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.email_is_required),getString(R.string.ok),R.drawable.warning);

           etEmail.requestFocus();
            return false;
        }
        else if (!Util.isValidMail(etEmail.getText().toString())) {
            //    etUserName.setError(getActivity().getResources().getString(R.string.enter_valid_mail));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.enter_valid_mail),getString(R.string.ok),R.drawable.warning);

            etEmail.requestFocus();
            return false;
        }
       else if (etEmail.getText().toString().length() == 0) {
            //   etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.enter_password),getString(R.string.ok),R.drawable.warning);

            etEmail.requestFocus();
            return false;
        }
       else if (etMobile.getText().toString().length() == 4||etMobile.getText().toString().length() == 0) {
           //  etMobile.setError(getActivity().getResources().getString(R.string.mobile_no_is_required));
           Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.mobile_no_is_required),getString(R.string.ok),R.drawable.warning);

           etMobile.requestFocus();
           return false;
       }
       else if (etMobile.getText().length() < 13) {

           //   etMobile.setError(getActivity().getResources().getString(R.string.please_enter_valid_mobile_number));
           Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.mobile_no_is_required),getString(R.string.ok),R.drawable.warning);

           etMobile.requestFocus();
           return false;
       } else if (!etMobile.getText().toString().split("-")[1].startsWith("5")) {
           //   etMobile.setError(getActivity().getResources().getString(R.string.number_strts_with_zero));
           Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.mobile_no_is_required),getString(R.string.ok),R.drawable.warning);

           etMobile.requestFocus();
           return false;
       }
       else if (etProblemType.getText().toString().equals(problemType[0])) {
            //   etSpeciality.setError(getActivity().getResources().getString(R.string.speciality_required));
            Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.Problem_Type_required),getString(R.string.ok),R.drawable.warning);

            etProblemType.requestFocus();
            return false;
        }
       else if (etProblemTitle.getText().toString().length() == 0) {
           //   etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
           Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.Problem_Title_required),getString(R.string.ok),R.drawable.warning);

           etProblemTitle.requestFocus();
           return false;
       }
       else if (etProbDesc.getText().toString().length() == 0) {
           //   etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
           Util.showAlert(getActivity(),getString(R.string.warning),getString(R.string.Problem_Description_required),getString(R.string.ok),R.drawable.warning);

           etProbDesc.requestFocus();
           return false;
       }
        return true;
    }

}

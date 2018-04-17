package com.app.hakeem.fragment;


import android.app.Dialog;
import android.content.Intent;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorRegistration;
import com.app.hakeem.pojo.LoginCredential;
import com.app.hakeem.pojo.RequestPatientRegistration;
import com.app.hakeem.pojo.Response;
import com.app.hakeem.pojo.ResponseLogin;
import com.app.hakeem.pojo.ResponsePost;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOTP extends Fragment {
    @BindView(R.id.etMobileNumber)
    EditText etMobileNumber;

    @BindView(R.id.btnResend)
    TextView btnResend;

    @BindView(R.id.etSMSverificationNumber)
    EditText etSMSverificationNumber;

    @BindView(R.id.btnSend)
    Button btnSend;
    private Dialog dialog;
    DoctorRegistration doctorRegistration;
    RequestPatientRegistration requestPatientRegistration;
    private Dialog dialogQueue;
    boolean isDoc=false;
    String userId;
    public FragmentOTP() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle!=null) {
            isDoc=bundle.getBoolean(C.IS_DOC);
           if(isDoc) {
               doctorRegistration = (DoctorRegistration) bundle.getSerializable(C.DETAILS);
               userId=bundle.getString(C.USER_ID);
           }
           else {
               requestPatientRegistration = (RequestPatientRegistration) bundle.getSerializable(C.DETAILS);
               userId=bundle.getString(C.USER_ID);

           }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_doctor_otp, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        etMobileNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (etMobileNumber.getText().length() == 0)
                        etMobileNumber.setText(C.NUMBER_FORMAT);
                    return false;
                }
            });
        etMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (!hasFocus && etMobileNumber.getText().toString().equals(C.NUMBER_FORMAT))
                        etMobileNumber.setText("");
                }
            });
        etMobileNumber.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            /*    if (s != null && s.length() == 5 && (s.charAt(4) < '7')) {
                    etMobileNumber.setText("");
                    etMobileNumber.setError("Number should start with 9,8 and 7");
                }*/
            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (!s.toString().startsWith(C.NUMBER_FORMAT)) {
                    etMobileNumber.setText(C.NUMBER_FORMAT);
                    Selection.setSelection(etMobileNumber.getText(), etMobileNumber
                            .getText().length());

                }
            }
        });
        etMobileNumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
              /*  if (!hasFocus && etMobileNumber.getText().toString().length() != 14)
                    etMobileNumber.setError("Phone number must be 10 digits");*/

            }
        });
        if(isDoc) {
            String m=Util.getArabicMobile(doctorRegistration.getMobileNumber());

            etMobileNumber.setText(m);
    //        etMobileNumber.setText(m);
        }
        else {
            String m=Util.getArabicMobile(requestPatientRegistration.getMobileNumber());
            etMobileNumber.setText(m);
    //        etMobileNumber.setText(m);
        }
        if(etMobileNumber.getText().toString()!=null && etMobileNumber.getText().toString().length()>11){
            otpRequest(etMobileNumber.getText().toString(),userId);
        }

            btnSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(etMobileNumber.getText().toString()!=null && etMobileNumber.getText().toString().length()>11) {

                        if (etSMSverificationNumber.getText().toString().length() > 0) {
                           otpVerify(etMobileNumber.getText().toString().replaceAll("-",""),etSMSverificationNumber.getText().toString());
                        } else {
                           // etSMSverificationNumber.setError(getString(R.string.otp_required));
                            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.otp_required),getString(R.string.ok),R.drawable.warning);
                            etSMSverificationNumber.requestFocus();
                        }
                    }
                    else {
                       // etMobileNumber.setError(getString(R.string.please_enter_valid_mobile_number));
                        Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_enter_valid_mobile_number),getString(R.string.ok),R.drawable.warning);
                        etMobileNumber.requestFocus();
                    }
                }
            });

        btnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etMobileNumber.getText().toString()!=null && etMobileNumber.getText().toString().length()>11){
                    otpRequest(etMobileNumber.getText().toString(),userId);

                }
                else {
                   // etMobileNumber.setError(getString(R.string.please_enter_valid_mobile_number));
                    Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.please_enter_valid_mobile_number),getString(R.string.ok),R.drawable.warning);
                    etMobileNumber.requestFocus();
                }
            }
        });



    }


    private void otpVerify(String mobile,String otp) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile_no", mobile);
        hashMap.put("otp", otp);

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
                    ResponsePost responsePost = gson.fromJson(response.toString(), ResponsePost.class);
                    if (responsePost.getStatusCode().equals(C.STATUS_SUCCESS)) {
                        if (isDoc) {
                            doLogin(doctorRegistration.getEmail(), doctorRegistration.getPassword());
                        } else {
                            doLogin(requestPatientRegistration.getEmail(), requestPatientRegistration.getPassword());
                        }

                    } else {
                        Util.showAlert(getActivity(), getString(R.string.alert), responsePost.getMessage(), getString(R.string.ok), R.drawable.warning);

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialog.dismiss();

            }
        }, "verify", C.API_VERIFY_OTP, Util.getHeader(getActivity()), obj);


    }

    private void otpRequest(String mobile,String userId) {

        dialog = Util.getProgressDialog(getActivity(), R.string.please_wait);
        dialog.setCancelable(false);
        dialog.show();
        HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("mobile_no", mobile);
        hashMap.put("user_id", userId);

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
                    ResponsePost responsePost = gson.fromJson(response.toString(), ResponsePost.class);
                    if (responsePost.getStatusCode().equals(C.STATUS_SUCCESS)) {
                       /* if(isDoc) {
                            doLogin(doctorRegistration.getEmail(), doctorRegistration.getPassword());
                        }
                        else {
                            doLogin(requestPatientRegistration.getEmail(),requestPatientRegistration.getPassword());
                        }*/

                    } /*else {
                        Util.showAlert(getActivity(), getString(R.string.alert), responsePost.getMessage(), getString(R.string.ok), R.drawable.warning);

                    }*/

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialog.dismiss();

            }
        }, "otp", C.API_GET_OTP, Util.getHeader(getActivity()), obj);


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

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                        SharedPreference.getInstance(getActivity()).setString(C.AUTH_TOKEN, responseLogin.getUser().getToken());
                        SharedPreference.getInstance(getActivity()).setUser(C.LOGIN_USER, responseLogin.getUser());
                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION,true);
                        if(isDoc) {
                            goOnline();
                        }
                        else {
                            Intent intent = new Intent(getActivity(), ActivityMain.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            getActivity().startActivity(intent);
                        }

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
                dialog.dismiss();

                Log.e("Response :", error.toString());

            }
        }, "login", C.API_LOGIN, Util.getHeader(getActivity()), obj);


    }
    private void goOnline() {


        dialogQueue = Util.getProgressDialog(getActivity(), R.string.loading);
        dialogQueue.show();

        HashMap<String, String> hashMap = new HashMap<>();

        hashMap.put("doctor_id", SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserId() + "");
        hashMap.put("status_id", "1");


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
                dialogQueue.dismiss();

                try {
                    Gson gson = new Gson();
                    Response responseLogin = gson.fromJson(response.toString(), Response.class);
                    if (responseLogin.getStatusCode().equals(C.STATUS_SUCCESS)) {

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_DOCOTR_ONLINE, true);
                        openMainActivity();
                    } else {

                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());
                dialogQueue.dismiss();

            }
        }, "online_doctor", C.API_GO_ONLINE, Util.getHeader(getActivity()), obj);


    }
    private void openMainActivity() {
        Intent intent = new Intent(getActivity(), ActivityMain.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        getActivity().startActivity(intent);
    }
}

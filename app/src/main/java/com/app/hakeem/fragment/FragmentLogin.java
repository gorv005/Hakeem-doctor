package com.app.hakeem.fragment;


import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.hakeem.ActivityContainer;
import com.app.hakeem.ActivityMain;
import com.app.hakeem.BuildConfig;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.DoctorRegistration;
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

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentLogin extends Fragment implements View.OnClickListener {

    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etPassword)
    EditText etPassword;
    @BindView(R.id.btnLogin)
    Button btnLogin;
    @BindView(R.id.btnCreateAccount)
    Button btnCreateAccount;
    @BindView(R.id.btnNeedHelp)
    Button btnNeedHelp;
    private Dialog dialog;
    private Dialog dialogQueue;


    public FragmentLogin() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        if (BuildConfig.DEBUG) {
            //  etUserName.setText("x@yopmail.com");
            // etPassword.setText("Admin@123");
            /*etUserName.setText("shagun@gmail.com");
            etPassword.setText("Admin@123");*/
        }

        btnLogin.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnNeedHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:

                SharedPreference.getInstance(getActivity()).setBoolean(C.IS_DOCOTR_ONLINE, false);
                if (isAllValid()) {
                    doLogin(etUserName.getText().toString(), etPassword.getText().toString());
                }

                break;
            case R.id.btnNeedHelp:
                break;
            case R.id.btnCreateAccount:
                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_REGISTRATION_TYPE, null);
                break;

        }
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
                        if(responseLogin.getUser().getIsMobileVerify()==0){
                            if (responseLogin.getUser().getUserType().equals(C.DOCTOR)) {
                                Bundle bundle=new Bundle();
                                DoctorRegistration  doctorRegistration=new DoctorRegistration();
                                doctorRegistration.setEmail(responseLogin.getUser().getEmail());
                                doctorRegistration.setPassword(etPassword.getText().toString());
                                doctorRegistration.setMobileNumber(responseLogin.getUser().getMobileNumber());

                                bundle.putSerializable(C.DETAILS,doctorRegistration);
                                bundle.putSerializable(C.USER_ID,responseLogin.getUser().getUserId());

                                bundle.putBoolean(C.IS_DOC,true);
                                ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_OTP,bundle);
                            }
                            else {
                                Bundle bundle=new Bundle();
                                RequestPatientRegistration requestPatientRegistration=new RequestPatientRegistration();
                                requestPatientRegistration.setEmail(responseLogin.getUser().getEmail());
                                requestPatientRegistration.setPassword(etPassword.getText().toString());
                                requestPatientRegistration.setMobileNumber(responseLogin.getUser().getMobileNumber());
                                bundle.putSerializable(C.USER_ID,responseLogin.getUser().getUserId());
                                bundle.putSerializable(C.DETAILS,requestPatientRegistration);
                                bundle.putBoolean(C.IS_DOC,false);
                                ((ActivityContainer)getActivity()).fragmnetLoader(C.FRAGMENT_OTP,bundle);
                            }

                        }
                        else {
                            SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                            SharedPreference.getInstance(getActivity()).setString(C.AUTH_TOKEN, responseLogin.getUser().getToken());
                            SharedPreference.getInstance(getActivity()).setUser(C.LOGIN_USER, responseLogin.getUser());
                            //   Util.showToast(getActivity(),responseLogin.getMessage(),true);
                            SharedPreference.getInstance(getActivity()).setBoolean(C.IS_NOFICATION, true);
                            if (SharedPreference.getInstance(getActivity()).getUser(C.LOGIN_USER).getUserType().equals(C.DOCTOR)) {

                                goOnline();
                            } else {
                                openMainActivity();
                            }
                        }
                    } else {
                        Util.showAlert(getActivity(), getString(R.string.error), responseLogin.getMessage(), getString(R.string.ok), R.drawable.warning);
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
                        Util.showToast(getActivity(), R.string.network_error, true);
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

    private boolean isAllValid() {


        if (!Util.isValidMail(etUserName.getText().toString())) {
        //    etUserName.setError(getActivity().getResources().getString(R.string.enter_valid_mail));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.enter_valid_mail),getString(R.string.ok),R.drawable.warning);

            etUserName.requestFocus();
            return false;
        } else if (etPassword.getText().toString().length() == 0) {
         //   etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
            Util.showAlert(getActivity(),getString(R.string.error),getString(R.string.enter_password),getString(R.string.ok),R.drawable.warning);

            etPassword.requestFocus();
            return false;
        }

        return true;
    }
}

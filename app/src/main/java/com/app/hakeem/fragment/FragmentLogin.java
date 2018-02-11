package com.app.hakeem.fragment;


import android.app.Dialog;
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
import com.app.hakeem.BuildConfig;
import com.app.hakeem.R;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.pojo.LoginCredential;
import com.app.hakeem.pojo.ResponseLogin;
import com.app.hakeem.util.C;
import com.app.hakeem.util.SharedPreference;
import com.app.hakeem.util.Util;
import com.app.hakeem.webservices.VolleyService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

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

        if(BuildConfig.DEBUG)
        {
            etUserName.setText("qwerty@yopmail.com");
            etPassword.setText("Admin@123");
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

                if (isAllValid()) {
                    doLogin(etUserName.getText().toString(), etPassword.getText().toString());
                }

                break;
            case R.id.btnNeedHelp:
                break;
            case R.id.btnCreateAccount:
                ((ActivityContainer) getActivity()).fragmnetLoader(C.FRAGMENT_REGISTRATION_TYPE,null);
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

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, true);
                        SharedPreference.getInstance(getActivity()).setString(C.AUTH_TOKEN, responseLogin.getUser().getToken());
                        SharedPreference.getInstance(getActivity()).setUser(C.LOGIN_USER,responseLogin.getUser());
                        Util.showToast(getActivity(),responseLogin.getMessage(),true);

                    } else {

                        SharedPreference.getInstance(getActivity()).setBoolean(C.IS_LOGIN, false);
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

    private boolean isAllValid() {


        if (!Util.isValidMail(etUserName.getText().toString())) {
            etUserName.setError(getActivity().getResources().getString(R.string.enter_valid_mail));
            return false;
        } else if (etPassword.getText().toString().length() == 0) {
            etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
            return false;
        }

        return true;
    }
}

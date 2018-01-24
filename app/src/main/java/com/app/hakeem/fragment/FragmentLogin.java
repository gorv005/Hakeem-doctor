package com.app.hakeem.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.app.hakeem.R;
import com.app.hakeem.util.Util;

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


        btnLogin.setOnClickListener(this);
        btnCreateAccount.setOnClickListener(this);
        btnNeedHelp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnLogin:

                if(isAllValid())
                {
//                    doLogin();
                }

                break;
            case R.id.btnNeedHelp:
                break;
            case R.id.btnCreateAccount:
                break;

        }
    }

    private boolean isAllValid() {


        if(!Util.isValidMail(etUserName.getText().toString()))
        {
            etUserName.setError(getActivity().getResources().getString(R.string.enter_valid_mail));
            return false;
        }
        else if(etPassword.getText().toString().length()==0)
        {
            etPassword.setError(getActivity().getResources().getString(R.string.enter_password));
            return false;
        }

        return true;
    }
}

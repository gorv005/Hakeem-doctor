package com.app.hakeem.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.hakeem.R;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;


public class FragmentSplash extends Fragment {
    Handler handler = new Handler();

    public FragmentSplash() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkConnectivity(getActivity())) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

//                    ((ActivityFragmentContainer)getActivity()).fragmnetLoader(C.FRAGMENT_MAIN,null);
                    getActivity().finish();
                    Intent intent = new Intent(getActivity(), ActivityMain.class);
                    startActivity(intent);

                }
            }, C.SPLASH_LOADER_TIME);
        } else {
            Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_LONG).show();
        }

    }
}

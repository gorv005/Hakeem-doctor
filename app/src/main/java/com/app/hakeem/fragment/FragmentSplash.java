package com.app.hakeem.fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.app.hakeem.ActivityMain;
import com.app.hakeem.R;
import com.app.hakeem.util.C;
import com.app.hakeem.util.Util;


public class FragmentSplash extends Fragment {
    Handler handler = new Handler();

    public FragmentSplash() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.isNetworkConnectivity(getActivity())) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (isStoragePermissionGranted()) {
                        getActivity().finish();
                        Intent intent = new Intent(getActivity(), ActivityMain.class);
                        startActivity(intent);
                    } else {
                        requestPermissionForStorage();
                    }
                }
            }, C.SPLASH_LOADER_TIME);
        } else {
            Toast.makeText(getActivity(), "Please connect to internet", Toast.LENGTH_LONG).show();
        }
    }


    private void requestPermissionForStorage() {

        ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    }


    public  boolean isStoragePermissionGranted() {
        try {

            if (Build.VERSION.SDK_INT >= 23) {
                if (getActivity().checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {

                    return true;
                } else {


                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    return false;
                }
            } else { //permission is automatically granted on sdk<23 upon installation

                return true;
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}

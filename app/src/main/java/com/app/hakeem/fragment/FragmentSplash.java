package com.app.hakeem.fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.app.hakeem.R;
import com.app.hakeem.utils.C;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSplash extends Fragment {


    public FragmentSplash() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_splash, container, false);

    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

            }
        }, C.SPLASH_DELAY);
    }



    public static void showPopUP( Activity activity) {
        try {
            LayoutInflater factory = LayoutInflater.from(activity);
            final View deleteDialogView = factory.inflate(
                    R.layout.pop_up_lang, null);
            final AlertDialog deleteDialog = new AlertDialog.Builder(activity).create();
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            deleteDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            deleteDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            deleteDialog.setView(deleteDialogView);

            deleteDialogView.findViewById(R.id.btnEnglish).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    deleteDialog.dismiss();
                }
            });
            deleteDialogView.findViewById(R.id.btnArabic).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    deleteDialog.dismiss();
                }
            });
            deleteDialog.show();
            deleteDialog.setCancelable(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.app.hakeem.util;

/**
 * Created by aditya.singh on 12/5/2017.
 */


import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.webservices.VolleyService;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    //    http://35.160.135.2/newsportal/
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(C.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);


        SharedPreference.getInstance(getApplicationContext()).setString(C.TOKEN, token);
        saveTokenToServer();
    }


    void saveTokenToServer() {

        if (!SharedPreference.getInstance(this).getBoolean(C.IS_LOGIN))
            return;

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("device_id", SharedPreference.getInstance(getApplicationContext()).getString(C.TOKEN));
        hashMap.put("user_id", SharedPreference.getInstance(getApplicationContext()).getUser(C.LOGIN_USER).getUserId());


        final Gson gson = new Gson();
        String json = gson.toJson(hashMap);
        JSONObject obj = null;
        try {
            obj = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        VolleyService volleyService = new VolleyService(getApplicationContext());
        volleyService.postDataVolley(new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Log.e("Response :", response.toString());

                try {
//                    Gson gson = new Gson();
//                    ConsultationTypeAndList consultationType = gson.fromJson(response.toString(), ConsultationTypeAndList.class);
//                    if (consultationType.getStatusCode().equals(C.STATUS_SUCCESS)) {
//
//                        Util.showAlert(getActivity(),getString(R.string.error),consultationType.getMessage(),getString(R.string.ok),R.drawable.warning);
//                    } else {
//                        Util.showAlert(getActivity(), getString(R.string.error), consultationType.getMessage(), getString(R.string.ok), R.drawable.warning);
//                    }

                } catch (Exception e) {

                    e.printStackTrace();
                }

            }

            @Override
            public void notifyError(String requestType, String error) {

                Log.e("Response :", error.toString());


            }
        }, "consultant", C.API_REGISTER_TOKEN, Util.getHeader(getApplicationContext()), obj);


    }

    private void storeRegIdInPref(String token) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(C.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("regId", token);
        editor.commit();
    }
}

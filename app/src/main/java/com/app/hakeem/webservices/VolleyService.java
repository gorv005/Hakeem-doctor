package com.app.hakeem.webservices;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.app.hakeem.interfaces.IResult;
import com.app.hakeem.util.C;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created by aditya.singh on 4/22/2017.
 */

public class VolleyService {

    IResult mResultCallback = null;
    Context context;

    public VolleyService(Context context) {
        this.context = context;
    }


    public void postDataVolley(IResult resultCallback, final String requestType, String url, final Map<String, String> headers, JSONObject sendObj) {
        mResultCallback = resultCallback;
        try {

            JsonObjectRequest jsonObj = new JsonObjectRequest(url, sendObj, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (mResultCallback != null)
                        mResultCallback.notifySuccess(requestType, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    if (mResultCallback != null)
                        mResultCallback.notifyError(requestType, error);
                }
            }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    return headers;
                }
            };


            RetryPolicy policy = new DefaultRetryPolicy(C.API_TIME_OUT, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            jsonObj.setRetryPolicy(policy);
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            requestQueue.add(jsonObj);


        } catch (Exception e) {
            if (mResultCallback != null)
                mResultCallback.notifyError(requestType, new VolleyError(e));
        }
    }

}
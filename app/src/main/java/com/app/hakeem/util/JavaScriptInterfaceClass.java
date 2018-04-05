package com.app.hakeem.util;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.webkit.JavascriptInterface;


public class JavaScriptInterfaceClass {

    public static final String TAG = "Javascriptlog";
    public static View mView;
    public static Activity activity;

    public JavaScriptInterfaceClass(Activity mActivity, View view) {
        mView = view;
        activity = mActivity;
    }

    /**
     * This passes our data out to the JS
     */

    @JavascriptInterface
    public Context getActivity() {
        return activity;
    }

    @JavascriptInterface
    public void setActivity(Activity app) {
        this.activity = app;
    }

    @JavascriptInterface
    public void finish() {
        activity.finish();
    }


    @JavascriptInterface
    public void success() {

        Util.showToast(activity, "Success Payment", true);
//        ((ActivityPayment) activity).showAlert();
    }

    @JavascriptInterface
    public void failure() {

        Util.showToast(activity, "Cancel Payment", true);
//        ((ActivityPayment) activity).showAlert();
    }


}


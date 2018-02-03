package com.app.hakeem.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.hakeem.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Ady on 9/2/2017.
 */

public class Util {


    public static boolean networkConnectivity(Activity activity) {
        ConnectivityManager cm = (ConnectivityManager) activity
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }
    public static boolean isValidMail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidMobile(String phone) {
        return (android.util.Patterns.PHONE.matcher(phone).matches() && (phone.length() >= 10 && phone.length() < 15));
    }

    public static int getAge(String year, String month, String day) {
        Calendar calDOB = Calendar.getInstance();
        calDOB.set(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
        Calendar calNow = Calendar.getInstance();
        calNow.setTime(new java.util.Date());
        int ageYr = (calNow.get(Calendar.YEAR) - calDOB.get(Calendar.YEAR));
        int ageMo = (calNow.get(Calendar.MONTH) - calDOB.get(Calendar.MONTH));
        if (ageMo < 0) {
            ageYr--;
        }
        return ageYr;
    }

    public static boolean isValidPassword(String password) {

        Pattern pattern;
        Matcher matcher;

        String PASSWORD_PATTERN = "((?=.*[A-Za-z])(?=.*[0-9]).{8,12})";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
    public static void hideKeyBoard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (activity.getCurrentFocus() != null)
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

    }
    public static String getProperDate(Date date) {
        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String strDate = newDateFormat.format(date);
        return strDate;
    }

}

package com.app.hakeem.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.app.hakeem.pojo.User;
import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by aditya.singh on 3/1/2017.
 */
public class SharedPreference implements Serializable {

    private Context context;
    private static volatile SharedPreference savePreferenceAndData;

    public static SharedPreference getInstance(Context context) {
        if (savePreferenceAndData == null) {
            synchronized (SharedPreference.class) {
                if (savePreferenceAndData == null)
                    savePreferenceAndData = new SharedPreference(context);
            }
        }
        return savePreferenceAndData;
    }

    public SharedPreference(Context context) {

        if (savePreferenceAndData != null) {
            throw new RuntimeException("Only one can instance create.");
        }
        this.context = context;

    }

    public void setString(String key, String data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, data).apply();
    }

    public String getString(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(key, null);
    }

    public void setIsProfileModified(String key, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }

    public boolean getIsProfileModified(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public void setIsReportDeleted(String key, boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }

    public boolean getIsReportDeleted(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }

    public void setBoolean(String key, boolean data) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, data).apply();
    }

    public boolean getBoolean(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(key, false);
    }


    public void setInt(String key, int value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getInt(key, 0);
    }

    public void clearData() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
    }


    public User getUser(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String data = prefs.getString(key, null);
        Gson gson = new Gson();
        return gson.fromJson(data, User.class);

    }

    public void setUser(String key, User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, json).apply();

    }

}


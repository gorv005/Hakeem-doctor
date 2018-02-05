package com.app.hakeem.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Ady on 9/2/2017.
 */
public class SharedPreference {

    private Context context;
    private static SharedPreference savePreferenceAndData;

    public static SharedPreference getInstance(Context context)
    {
        if(savePreferenceAndData==null)
        {
            savePreferenceAndData = new SharedPreference(context);
        }
        return savePreferenceAndData;
    }

    public SharedPreference(Context context)
    {
        this.context = context;

    }
    public void setString(String key, String data)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putString(key, data).apply();
    }
    public String getString(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getString(key, null);
    }

    public void setIsProfileModified(String key,boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }
    public boolean getIsProfileModified(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(key, false);
    }
    public void setIsReportDeleted(String key,boolean status) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, status).apply();
    }
    public boolean getIsReportDeleted(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(key, false);
    }
    public void setBoolean(String key, boolean data)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putBoolean(key, data).apply();
    }

    public boolean getBoolean(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getBoolean(key, false);
    }



    public void saveInt(String key, int value)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().putInt(key, value).apply();
    }

    public int getInt(String key)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return  prefs.getInt(key, 0);
    }
    public void clearData()
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.edit().clear().commit();
    }
}

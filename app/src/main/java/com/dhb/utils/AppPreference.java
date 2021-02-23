package com.dhb.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;


import com.dhb.R;
import com.dhb.models.VersionInfo;
import com.google.gson.Gson;

public class AppPreference {
    private static AppPreference appPreferences;
    private Gson gson;

    private Context applicationContext;

    private SharedPreferences sharedPreferences;

    private AppPreference(Context applicationContext) {

        this.applicationContext = applicationContext;
        Logger.debug("applicationContext: " + applicationContext);
        Resources res = this.applicationContext.getResources();
        gson=new Gson();
        // Logger.debug("res: "+res);
        String preferencesName = res.getString(R.string.app_name);
        sharedPreferences = this.applicationContext.getSharedPreferences(preferencesName, Context.MODE_PRIVATE);
    }

    public static AppPreference getAppPreferences(Context applicationContext) {

        if (appPreferences != null) {
            return appPreferences;
        }
        appPreferences = new AppPreference(applicationContext);
        return appPreferences;
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public String getString(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public void putLong(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.commit();
    }

    public long getLong(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public int getInt(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putFloat(String key, float value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key, value);
        editor.commit();
    }

    public float getFloat(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }


    public void clearPreferences() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void putVersionInfo(String key, VersionInfo versionInfo){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String json = gson.toJson(versionInfo);
        editor.putString(key, json);
        editor.commit();
    }

    public VersionInfo getVersionInfo(String key){
        String json = sharedPreferences.getString(key,"");
        VersionInfo versionInfo = gson.fromJson(json,VersionInfo.class);
        return versionInfo;
    }

}

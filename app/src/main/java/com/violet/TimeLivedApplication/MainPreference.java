package com.violet.TimeLivedApplication;

import android.content.Context;
import android.content.SharedPreferences;

public class MainPreference {
    private static final String TAG = MainPreference.class.getSimpleName();
    public static final String PREFERENCE = TAG;

    public static final String PREF_KEY_YEAR = "year";
    public static final String PREF_KEY_MONTH = "month";
    public static final String PREF_KEY_DAY = "day";
    public static final String PREF_KEY_GENDER = "gender";
    public static final String PREF_KEY_NATION = "nation";

    public static final int PREF_VALUE_DEFAULT_INT = -1;

    private static MainPreference sPreference;
    private SharedPreferences mSharedPreferences;

    public static MainPreference getInstance() {
        return sPreference;
    }

    public static void createInstance(Context context) {
        if (sPreference == null) {
            synchronized (MainPreference.class) {
                if (sPreference == null) {
                    sPreference = new MainPreference(context);
                }
            }
        }
    }

    private MainPreference(Context context) {
        if (null  != context) {
            mSharedPreferences = context.getSharedPreferences(PREFERENCE, Context.MODE_PRIVATE);
        }
    }

    public int getIntValue(String strKey) {
        return getIntValue(strKey, PREF_VALUE_DEFAULT_INT);
    }

    public int getIntValue(String strKey, int defaultVal) {
        return mSharedPreferences.getInt(strKey, defaultVal);
    }

    public void setIntValue( String strKey, int val) {
        if(null == strKey || 0 == strKey.length()) {
            return;
        }

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(null != editor){
            editor.putInt(strKey, val);
            editor.apply();
        }
    }
}

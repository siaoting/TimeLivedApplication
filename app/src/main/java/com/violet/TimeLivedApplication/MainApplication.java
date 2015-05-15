package com.violet.TimeLivedApplication;

import android.app.Application;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        MainPreference.createInstance(this);
    }
}

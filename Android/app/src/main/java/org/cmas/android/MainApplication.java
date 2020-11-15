package org.cmas.android;

import androidx.multidex.MultiDexApplication;

public class MainApplication extends MultiDexApplication {

    private static MainApplication context;

    public static MainApplication getAppContext() {
        return MainApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = this;
    }
}

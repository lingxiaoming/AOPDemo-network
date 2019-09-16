package com.zyy.aopdemo;

import android.app.Application;

import com.zyy.library.NetworkManager;

public class BaseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        NetworkManager.getDefault().init(this);
    }
}

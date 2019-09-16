package com.zyy.library;

import android.app.Application;
import android.content.IntentFilter;

public class NetworkManager {

    private static volatile NetworkManager instance;

    private Application application;

    private NetStateReceiver netStateReceiver;

    private NetChangeObserver listener;

    private NetworkManager(){
        netStateReceiver = new NetStateReceiver();
    }

    public void setListener(NetChangeObserver listener){
        this.listener = listener;
        netStateReceiver.setListener(this.listener);
    }

    public static NetworkManager getDefault(){
        if(instance == null){
            synchronized (NetworkManager.class){
                if(instance == null){
                    instance = new NetworkManager();
                }
            }
        }
        return instance;
    }

    public Application getApplication(){
        if(application == null){
            throw new RuntimeException("application is null");
        }
        return application;
    }


    public void init(Application application){
        this.application = application;

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ANDROID_NET_CHANGE_ACTION);
        application.registerReceiver(netStateReceiver, intentFilter);
    }

    public void register(Object object) {
        netStateReceiver.register(object);
    }
}

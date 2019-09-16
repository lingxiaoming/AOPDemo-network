package com.zyy.aopdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.zyy.library.NetType;
import com.zyy.library.Network;
import com.zyy.library.NetworkManager;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        NetworkManager.getDefault().setListener(this);
        NetworkManager.getDefault().register(this);
    }


    @Network(netType = NetType.WIFI)//wifi的时候触发
    public void network(NetType netType){
        switch (netType){
            case AUTO:
                Log.e("Test", "AUTO");
                break;
            case WIFI:
                Log.e("Test", "WIFI");
                break;
            case CMNET:
            case CMWAP:
                Log.e("Test", netType.name());
                break;
            case NONE:
                Log.e("Test", "NONE");
                break;
        }
    }
//
//
//    @Override
//    public void onConnect() {
//        Toast.makeText(this, "网络连接成功", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onDisConnect() {
//        Toast.makeText(this, "网络连接失败", Toast.LENGTH_SHORT).show();
//    }
}

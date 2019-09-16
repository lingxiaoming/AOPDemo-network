package com.zyy.library;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkUtils {

    public static boolean isNetworkAvailable(){
        ConnectivityManager connMgr = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        if(connMgr == null) return false;

        NetworkInfo[] info = connMgr.getAllNetworkInfo();
        if(info != null){
            for (NetworkInfo networkInfo:info) {
                if(networkInfo.getState() == NetworkInfo.State.CONNECTED){
                    return true;
                }
            }
        }
        return false;
    }


     public static NetType getNetType(){
         ConnectivityManager connMgr = (ConnectivityManager) NetworkManager.getDefault().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

         if(connMgr == null) return NetType.NONE;

         NetworkInfo info = connMgr.getActiveNetworkInfo();

         if(info == null) return NetType.NONE;

         int nType = info.getType();

         if(nType == ConnectivityManager.TYPE_MOBILE){
             if(info.getExtraInfo().toLowerCase().equals("cmnet")){
                return NetType.CMNET;
             }else{
                 return NetType.CMWAP;
             }
         }else if(nType == ConnectivityManager.TYPE_WIFI){
            return NetType.WIFI;
         }

         return NetType.NONE;
     }


}

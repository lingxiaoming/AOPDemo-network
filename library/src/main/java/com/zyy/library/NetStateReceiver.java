package com.zyy.library;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class NetStateReceiver extends BroadcastReceiver {
    private NetType netType;
    private NetChangeObserver listener;
    private Map<Object, List<MethodManager>> networkList;

    NetStateReceiver(){
        netType = NetType.NONE;
        networkList = new HashMap<>();
    }

    public void setListener(NetChangeObserver listener){
        this.listener = listener;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(Constants.ANDROID_NET_CHANGE_ACTION)){
            netType = NetworkUtils.getNetType();
        }

        if(NetworkUtils.isNetworkAvailable()){
            Log.e("Test", "网络连接成功...");
            if(listener != null){
                listener.onConnect();
            }
        }else {
            Log.e("Test", "网络连接失败...");
            if(listener != null){
                listener.onDisConnect();
            }
        }

        post(netType);
    }

    private void post(NetType netType) {
        if(networkList.isEmpty()) return;

        Set<Object> set = networkList.keySet();

        for (Object getter:set) {
            List<MethodManager> methodManagerList = networkList.get(getter);
            if(methodManagerList != null){
                for (MethodManager methodManager:methodManagerList) {
                    //参数匹配
                    if(methodManager.getType().isAssignableFrom(netType.getClass())){
                        switch (methodManager.getNetType()){
                            case AUTO:
                                invoke(methodManager, getter, netType);
                                break;
                            case WIFI:
                                if(netType == NetType.WIFI || netType == NetType.NONE){
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            case CMNET:
                            case CMWAP:
                                if(netType == NetType.CMNET || netType == NetType.CMWAP ||netType == NetType.NONE){
                                    invoke(methodManager, getter, netType);
                                }
                                break;
                            case NONE:

                                break;
                        }
                    }
                }
            }
        }
    }

    private void invoke(MethodManager methodManager, Object getter, NetType netType) {
        try {
            methodManager.getMethod().invoke(getter, netType);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void register(Object object){
        List<MethodManager> methodList = networkList.get(object);

        if(methodList == null){
            methodList = findAnnotation(object);
            networkList.put(object, methodList);
        }
    }

    private List<MethodManager> findAnnotation(Object object) {
        List<MethodManager> methodManagerList = new ArrayList<>();

        Class<?> clazz = object.getClass();
        Method[] methods = clazz.getMethods();

        //订阅方法的收集
        for (Method method:methods) {
            Network network = method.getAnnotation(Network.class);
            if(network == null) continue;

            Class<?>[] parameterTypes = method.getParameterTypes();

            MethodManager manager = new MethodManager(parameterTypes[0], network.netType(), method);
            methodManagerList.add(manager);

        }
        return methodManagerList;
    }
}

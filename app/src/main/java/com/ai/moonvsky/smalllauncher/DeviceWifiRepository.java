package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class DeviceWifiRepository {
    private static final String TAG = "DeviceWifiRepository";
    private MutableLiveData<WifiInfo> wifiInfoMutableLiveData;

    public DeviceWifiRepository(Application application) {
        wifiInfoMutableLiveData = new MutableLiveData<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        application.registerReceiver(new MyNetReceiver(), intentFilter);
    }

    public MutableLiveData<WifiInfo> getWifiInfoMutableLiveData() {
        return wifiInfoMutableLiveData;
    }

    public class MyNetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle extras = intent.getExtras();
            Log.e(TAG, "actioin:" + action);

            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {//这个监听wifi的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, -1);
                Log.e(TAG, "wifiState:" + wifiState);
                WifiInfo wifiInfo = new WifiInfo();
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_DISABLED:
                        Log.e(TAG, "wifiState:WIFI_STATE_DISABLED");
                        wifiInfo.setConnected(false);
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        Log.e(TAG, "wifiState:WIFI_STATE_DISABLING");
                        wifiInfo.setConnected(false);
                        break;
                    case WifiManager.WIFI_STATE_ENABLED:
                        Log.e(TAG, "wifiState:WIFI_STATE_ENABLED");
                        wifiInfo.setConnected(true);
                        if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(action)) {
                            Parcelable parcelableExtra = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                            WifiInfo mWifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
                            String ssid=mWifiInfo.getSsid();
                            wifiInfo.setSsid(ssid);
                        }
                        break;
                    case WifiManager.WIFI_STATE_ENABLING:
                        Log.e(TAG, "wifiState:WIFI_STATE_ENABLING");
                        wifiInfo.setConnected(false);
                        break;
                    case WifiManager.WIFI_STATE_UNKNOWN:
                        Log.e(TAG, "wifiState:WIFI_STATE_UNKNOWN");
                        wifiInfo.setConnected(false);
                        break;
                    //
                }
                wifiInfoMutableLiveData.postValue(wifiInfo);
            }

        }
    }
}

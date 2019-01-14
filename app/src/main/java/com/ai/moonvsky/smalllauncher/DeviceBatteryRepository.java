package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;

public class DeviceBatteryRepository {
    private static final String TAG = "DeviceBatteryRepository";
    private MutableLiveData<BatteryInfo> batteryInfoMutableLiveData;

    public DeviceBatteryRepository(Application application) {
        batteryInfoMutableLiveData = new MutableLiveData<>();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);

        application.registerReceiver(new MyNetReceiver(), intentFilter);
    }

    public MutableLiveData<BatteryInfo> getBatteryInfoMutableLiveData() {
        return batteryInfoMutableLiveData;
    }

    public class MyNetReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            int level= intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int scale=intent.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            BatteryInfo batteryInfo=new BatteryInfo();
            int batteryPercent=(int)(((float)level / scale) * 100);
            batteryInfo.setBatteryPercent(batteryPercent);
            Log.e(TAG, "actioin:" + action+"|level:"+level+"|percent:"+batteryPercent);
            if(batteryPercent!=100){
                batteryInfo.setFull(false);
            }else{
                batteryInfo.setFull(true);
            }
            batteryInfoMutableLiveData.postValue(batteryInfo);

        }
    }
}

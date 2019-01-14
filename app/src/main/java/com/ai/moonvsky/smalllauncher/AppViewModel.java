package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;

import java.util.List;

public class AppViewModel extends AndroidViewModel {
    private MutableLiveData<List<App>> mAllApps;
    private AppRepository appRepository;
    private MutableLiveData<WifiInfo> wifiInfoMutableLiveData;
    private MutableLiveData<BatteryInfo> batteryInfoMutableLiveData;
    private DeviceWifiRepository deviceWifiRepository;
    private DeviceBatteryRepository deviceBatteryRepository;

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        mAllApps = appRepository.getmAllApps();
        deviceWifiRepository=new DeviceWifiRepository(application);
        wifiInfoMutableLiveData=deviceWifiRepository.getWifiInfoMutableLiveData();
        deviceBatteryRepository=new DeviceBatteryRepository(application);
        batteryInfoMutableLiveData=deviceBatteryRepository.getBatteryInfoMutableLiveData();
    }


    public MutableLiveData<List<App>> getmAllApps() {
        return mAllApps;
    }

    public MutableLiveData<WifiInfo> getWifiInfoMutableLiveData() {
        return wifiInfoMutableLiveData;
    }

    public MutableLiveData<BatteryInfo> getBatteryInfoMutableLiveData() {
        return batteryInfoMutableLiveData;
    }

    public void unregisterAppReceiver(){
        appRepository.unregisterAppReceiver();
    }
}

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

    public AppViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
        mAllApps = appRepository.getmAllApps();
    }


    public MutableLiveData<List<App>> getmAllApps() {
        return mAllApps;
    }
    public void unregisterAppReceiver(){
        appRepository.unregisterAppReceiver();
    }
}

package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

class AppRepository {
    private MutableLiveData<List<App>> mAllApps;

    AppRepository(Application context) {
        //get all app data
        mAllApps = new MutableLiveData<>();
        new LoadTask(context).execute();
    }

    MutableLiveData<List<App>> getmAllApps() {
        return mAllApps;
    }

    class LoadTask extends AsyncTask<Void, Integer, ArrayList<App>> {

        private Context mContext;
        PackageManager packageManager;
        private ArrayList<App> appList;

        LoadTask(@NonNull Context context) {
            appList = new ArrayList<>();
            this.mContext = context;
            this.packageManager = context.getPackageManager();
        }


        @Override
        protected ArrayList<App> doInBackground(Void... voids) {
            List<PackageInfo> packagesInfo = packageManager.getInstalledPackages(PackageManager.GET_ACTIVITIES);
            for (PackageInfo appInfo : packagesInfo
                    ) {
                App app = new App();
                app.packageName = appInfo.packageName;
                app.appName = appInfo.applicationInfo.loadLabel(packageManager).toString();
                app.launchIntent = packageManager.getLaunchIntentForPackage(app.packageName);
                try {
                    if (app.launchIntent != null) {
                        app.icon = packageManager.getActivityIcon(app.launchIntent);
                        appList.add(app);
                    }
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }
            mAllApps.postValue(appList);
            return appList;
        }
    }
}

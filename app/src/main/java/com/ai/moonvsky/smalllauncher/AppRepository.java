package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

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

    public class AppInstallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            PackageManager manager = context.getPackageManager();
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                App app = new App();
                app.packageName = packageName;
                app.launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
                try {
                    app.appName = context.getResources().getString(context.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.labelRes);
                    app.icon = context.getPackageManager().getApplicationIcon(packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                mAllApps.getValue().add(app);
//                Toast.makeText(context, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            }
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
                Log.d("Test", "onReceive: " + Intent.ACTION_PACKAGE_REMOVED);
                String packageName = intent.getData().getSchemeSpecificPart();
                Toast.makeText(context, "卸载成功" + packageName, Toast.LENGTH_LONG).show();
                for (App app :
                        mAllApps.getValue()) {
                    if (packageName.equals(app.packageName)) {
                        mAllApps.getValue().remove(app);
                    }
                }
            }
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REPLACED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                Toast.makeText(context, "替换成功" + packageName, Toast.LENGTH_LONG).show();
            }


        }

    }
}

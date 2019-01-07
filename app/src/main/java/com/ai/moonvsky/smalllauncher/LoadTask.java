package com.ai.moonvsky.smalllauncher;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;

import java.util.ArrayList;
import java.util.List;

class LoadTask extends AsyncTask<Void,Integer,ArrayList<App>> {

        ArrayList<App> appList;
        private Context mContext;
        PackageManager packageManager;

        public LoadTask(@NonNull Context context) {
            this.mContext=context;
            this.packageManager=context.getPackageManager();
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
        return appList;
    }

}
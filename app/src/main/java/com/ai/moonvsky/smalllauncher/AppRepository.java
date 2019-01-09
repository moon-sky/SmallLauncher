package com.ai.moonvsky.smalllauncher;

import android.app.Application;
import android.arch.lifecycle.MutableLiveData;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

class AppRepository {
    public static final String TAG = "Test";
    private MutableLiveData<List<App>> mAllApps;
    private AppInstallReceiver receiver;
    private Context mContext;

    AppRepository(Application context) {
        mContext = context;
        //get all app data
        mAllApps = new MutableLiveData<>();
        new LoadTask().execute();
        initAppReceiver(context);
    }

    private void initAppReceiver(Application context) {
        receiver = new AppInstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        context.registerReceiver(receiver, filter);
    }

    MutableLiveData<List<App>> getmAllApps() {
        return mAllApps;
    }

    /**
     * 注销APP相关监听
     */
    public void unregisterAppReceiver() {
        mContext.unregisterReceiver(receiver);
    }

    class LoadTask extends AsyncTask<Void, Integer, ArrayList<App>> {

        private Context mContext;
        PackageManager packageManager;
        private ArrayList<App> appList;

        LoadTask() {
            appList = new ArrayList<>();
            this.packageManager = mContext.getPackageManager();
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
/*        public AppInstallReceiver() {
        }*/

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "onReceive: action:" + intent.getAction());
            PackageManager manager = context.getPackageManager();
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED)) {
                String packageName = intent.getData().getSchemeSpecificPart();
                App app = new App();
                app.packageName = packageName;
                app.launchIntent = context.getPackageManager().getLaunchIntentForPackage(app.packageName);
                try {
                    app.appName = context.getPackageManager().getPackageInfo(packageName, 0).applicationInfo.loadLabel(context.getPackageManager()).toString();
                    app.icon = context.getPackageManager().getApplicationIcon(packageName);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

                mAllApps.getValue().add(app);
                mAllApps.postValue(mAllApps.getValue());
//                Toast.makeText(mContext, "安装成功"+packageName, Toast.LENGTH_LONG).show();
            }
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
                Log.d(TAG, "onReceive: " + Intent.ACTION_PACKAGE_REMOVED);
                String packageName = intent.getData().getSchemeSpecificPart();
                Toast.makeText(context, "卸载成功" + packageName, Toast.LENGTH_LONG).show();
                for (int i = 0; i < mAllApps.getValue().size(); i++) {
                    App app = mAllApps.getValue().get(i);
                    if (packageName.equals(app.packageName)) {
                        mAllApps.getValue().remove(app);
                        mAllApps.postValue(mAllApps.getValue());
                        Log.d(TAG, "onReceive: remove app:" + mAllApps.getValue().size());
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

package com.ai.moonvsky.smalllauncher;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_app);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        new LoadTask(this).execute();


    }

    class LoadTask extends AsyncTask<Void,Integer,ArrayList<App>> {

        ArrayList<App> appList=new ArrayList<>();
        PackageManager packageManager;

        public LoadTask(@NonNull Context context) {
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

        @Override
        protected void onPostExecute(ArrayList<App> apps) {
            MyAdapter myAdapter = new MyAdapter(apps);
            recyclerView.setAdapter(myAdapter);
        }
    }


}

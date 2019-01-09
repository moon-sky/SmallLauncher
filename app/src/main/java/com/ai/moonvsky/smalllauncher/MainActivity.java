package com.ai.moonvsky.smalllauncher;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Test";
    RecyclerView recyclerView;
    private AppViewModel appViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MyAdapter myAdapter = new MyAdapter();
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appViewModel.getmAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable List<App> apps) {
                Log.d(TAG, "onChanged: appsize:" + apps.size());
                myAdapter.setmDataset(apps);
            }

        });
        recyclerView = findViewById(R.id.rv_app);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new SpaceItemDecoration());
        recyclerView.setAdapter(myAdapter);
//        recyclerView.setItemAnimator(new DefaultItemAnimator());

    }

    @Override
    protected void onDestroy() {
        if(appViewModel!=null){
            appViewModel.unregisterAppReceiver();
        }
        super.onDestroy();
    }
}

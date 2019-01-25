package com.ai.moonvsky.smalllauncher;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "Test";
    RecyclerView recyclerView;
    private AppViewModel appViewModel;
    private MenuItem wifiMenuItem;
    private MenuItem batteryMenuItem;
    private TextView ssidTextView;
    private ImageView mWifiIcon;
    private TextView batteryPercent;
    private ImageView mBatteryIcon;
    private Lock lock;
    private Condition uiAvaliable;
    private WifiInfo tempWifiInfo;
    private BatteryInfo tempBatteryInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToobar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToobar);
        lock = new ReentrantLock();
        uiAvaliable = lock.newCondition();


        final MyAdapter myAdapter = new MyAdapter();
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appViewModel.getmAllApps().observe(this, new Observer<List<App>>() {
            @Override
            public void onChanged(@Nullable List<App> apps) {
                assert apps != null;
                Log.d(TAG, "onChanged: appsize:" + apps.size());
                myAdapter.setmDataset(apps);
            }

        });
        appViewModel.getWifiInfoMutableLiveData().observe(this, new Observer<WifiInfo>() {
            @Override
            public void onChanged(@Nullable WifiInfo wifiInfo) {
                tempWifiInfo = wifiInfo;
                assert wifiInfo != null;
                Log.d(TAG, "onChanged: " + wifiInfo.toString());
                if (wifiMenuItem == null) {
                    return;
                }
                updateWifiView(wifiInfo);

            }
        });
        appViewModel.getBatteryInfoMutableLiveData().observe(this, new Observer<BatteryInfo>() {
            @Override
            public void onChanged(@Nullable BatteryInfo batteryInfo) {
                tempBatteryInfo = batteryInfo;
                if (batteryMenuItem == null) {
                    return;
                }
                updateBatteryView(batteryInfo);
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

    private void updateWifiView(@NonNull WifiInfo wifiInfo) {
        if (wifiInfo.isConnected()) {
            mWifiIcon.setImageResource(R.drawable.ic_wifi_black_24dp);

            ssidTextView.setText(wifiInfo.getSsid());
        } else {
            mWifiIcon.setImageResource(R.drawable.ic_signal_wifi_off_black_24dp);
            ssidTextView.setText("");
        }
    }

    private void updateBatteryView(@NonNull BatteryInfo batteryInfo) {
        if (batteryInfo.isFull()) {
            mBatteryIcon.setImageResource(R.drawable.ic_battery_full_black_24dp);
            batteryPercent.setText(getString(R.string.full));
        } else {
            int bp=batteryInfo.getBatteryPercent();
            int resId;
            if(range(bp,80,100)){
                resId=R.drawable.ic_battery_80_black_24dp;
            }else  if(range(bp,60,80)){
                resId=R.drawable.ic_battery_60_black_24dp;
            }else if(range(bp,50,60)){
                resId=R.drawable.ic_battery_50_black_24dp;
            }
            else if(range(bp,30,50)){
                resId=R.drawable.ic_battery_50_black_24dp;
            }
            else if(range(bp,20,30)){
                resId=R.drawable.ic_battery_50_black_24dp;
            }else{
                resId=R.drawable.ic_battery_alert_black_24dp;
            }
            mBatteryIcon.setImageResource(resId);
            Log.d(TAG, "updateBatteryView: "+batteryInfo.toString());
            batteryPercent.setText(batteryInfo.getBatteryPercent()+"");
        }
    }
    private boolean range(int target,int low,int max){
        return target <= max && target >= low;
    }

    @Override
    protected void onDestroy() {
        if (appViewModel != null) {
            appViewModel.unregisterAppReceiver();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        Log.d(TAG, "onCreateOptionsMenu: ");
        wifiMenuItem = menu.findItem(R.id.wifi);
        Log.d(TAG, "onCreateOptionsMenu: " + wifiMenuItem);
        ssidTextView = wifiMenuItem.getActionView().findViewById(R.id.tv_ssid);
        mWifiIcon = wifiMenuItem.getActionView().findViewById(R.id.iv_wifi);
        if (tempWifiInfo != null) {
            updateWifiView(tempWifiInfo);
        }
        batteryMenuItem = menu.findItem(R.id.battery);
        batteryPercent = batteryMenuItem.getActionView().findViewById(R.id.tv_percent);
        mBatteryIcon = batteryMenuItem.getActionView().findViewById(R.id.icon_battery);
        Log.d(TAG, "onCreateOptionsMenu: "+batteryPercent.getText()+"|icon:"+mBatteryIcon);
        updateBatteryView(tempBatteryInfo);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}

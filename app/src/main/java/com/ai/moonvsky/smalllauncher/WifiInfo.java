package com.ai.moonvsky.smalllauncher;

import android.support.annotation.NonNull;

public class WifiInfo {
    private boolean isConnected=false;
    private String ssid;

    boolean isConnected() {
        return isConnected;
    }

    void setConnected(boolean connected) {
        isConnected = connected;
    }

    String getSsid() {
        return ssid;
    }

    void setSsid(String ssid) {
        this.ssid = ssid;
    }

    @NonNull
    @Override
    public String toString() {
        return "WifiInfo{" +
                "isConnected=" + isConnected +
                ", ssid='" + ssid + '\'' +
                '}';
    }
}

package com.ai.moonvsky.smalllauncher;

import android.support.annotation.NonNull;

public class BatteryInfo {
    private boolean isFull;
    private int batteryPercent;

    public boolean isFull() {
        return isFull;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    int getBatteryPercent() {
        return batteryPercent;
    }

    void setBatteryPercent(int batteryPercent) {
        this.batteryPercent = batteryPercent;
    }

    @NonNull
    @Override
    public String toString() {
        return "BatteryInfo{" +
                "isFull=" + isFull +
                ", batteryPercent=" + batteryPercent +
                '}';
    }
}

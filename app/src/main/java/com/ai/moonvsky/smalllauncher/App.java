package com.ai.moonvsky.smalllauncher;

import android.content.Intent;
import android.graphics.drawable.Drawable;

public class App {
    String packageName;
    Intent launchIntent;
    String appName;
    Drawable icon;

    @Override
    public String toString() {
        return "App{" +
                "packageName='" + packageName + '\'' +
                ", launchIntent='" + launchIntent + '\'' +
                ", icon=" + icon +
                '}';
    }

    public App(String packageName, Intent launchIntent, Drawable icon) {

        this.packageName = packageName;
        this.launchIntent = launchIntent;
        this.icon = icon;
    }
    public App(){}

    public String getPackageName() {
        return packageName;
    }

    public Intent getLaunchIntent() {
        return launchIntent;
    }

    public Drawable getIcon() {
        return icon;
    }

    public String getAppName() {
        return appName;
    }
}

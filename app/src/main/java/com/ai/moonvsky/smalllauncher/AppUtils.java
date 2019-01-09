package com.ai.moonvsky.smalllauncher;

import android.content.Intent;
import android.net.Uri;

public class AppUtils {
    /**
     * 获取卸载App的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static Intent getUninstallAppIntent(String packageName) {
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:" + packageName));
        return intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 获取打开App的意图
     *
     * @param packageName 包名
     * @return intent
     */
    public static  Intent getLaunchAppIntent(String packageName) {
        return MyApplication.getContext().getPackageManager().getLaunchIntentForPackage(packageName);
    }

}

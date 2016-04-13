package org.mobile.htloginsdk.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by 郭君华 on 2016/3/2.
 * Email：guojunhua3369@163.com
 */
public final class HTSdk {
    public static SharedPreferences sp;
    public static SharedPreferences.Editor edit;

    public static synchronized void sdkInitialize(Context context ,String appId ,String channel) {
        sp = getSharedPreferences(context);
        HTSdk.sdkInitialize(appId,channel,sp);
    }
    public static synchronized void sdkInitialize(String appId ,String channel,SharedPreferences sp) {
        if (!appId.equals("")&!channel.equals("")){
            edit = sp.edit();
            edit.putString("appId", appId);
            edit.putString("channel",channel);
            edit.apply();
        }
    }
    public static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences("login", context.MODE_PRIVATE);
    }
}

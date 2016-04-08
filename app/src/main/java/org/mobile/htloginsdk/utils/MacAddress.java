package org.mobile.htloginsdk.utils;

import android.content.Context;
import android.provider.Settings;
import android.telephony.TelephonyManager;

/**
 * Created by 郭君华 on 2016/1/6.
 * Email：guojunhua3369@163.com
 */
public class MacAddress {
    private Context context;
    private TelephonyManager telephoneManager;

    public MacAddress(Context context) {
        this.context = context;
    }

    public String getMacAddressAndroid() {
        telephoneManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
        String deviceId = telephoneManager.getDeviceId();
        if (deviceId == null || deviceId.isEmpty()) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            if (deviceId.isEmpty() || deviceId == null) {
                deviceId = "00000000";
            }
        }
        return deviceId;
    }

    /**
     * 获取手机品牌
     */
    public static String getBrand() {
        return android.os.Build.BRAND;
    }
}

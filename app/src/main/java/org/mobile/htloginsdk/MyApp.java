package org.mobile.htloginsdk;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by 郭君华 on 2016/1/18.
 * Email：guojunhua3369@163.com
 */
public class MyApp extends Application {
    public final static String url = "http://c.gamehetu.com/passport/%s?app=%s&data=%s";
    public final static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC1jDlsT3SgLrjYVuxegFJlygU8" + "\r"
            + "hN0CqxHZI2c6Tn9/VmlmSugAtkm9u/VIm7/EjUhreQ6sGr5MyZXF68cH38Say7Oa" + "\r"
            + "b7ws2oHbaE2LfetwAXBC/THVH1l59HacLJiOnHFvBGKjPEQULyx4N5Gj9qAYbWeY" + "\r"
            + "sWFrt5f4g0bd4c+mKQIDAQAB" + "\r";
    public final static String bindUrl = "http://c.gamehetu.com/stat/login?app=%s&type=%s&version=%s&os=android&channel=%s&uid=%s&coo_server=%s&coo_uid=%s&uuid=%s&idfa=%s&mac=&device_type=%s";
    private TextView tv_title;
    private ImageView iv_anim;
    private AnimationDrawable animation;

    public MyApp() {
    }

    public SharedPreferences getSharedPreferences() {
        return super.getSharedPreferences("login", MODE_PRIVATE);
    }
    public AlertDialog getAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_layout, null);
        tv_title = ((TextView) view.findViewById(R.id.alert_tv));
        iv_anim = ((ImageView) view.findViewById(R.id.alert_iv));
        iv_anim.setBackgroundResource(R.drawable.loadanimation);
        animation = ((AnimationDrawable) iv_anim.getBackground());
        builder.setView(view);
        tv_title.setText(this.getString(R.string.loading));
        return builder.create();
    }
}

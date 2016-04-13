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

import org.mobile.htloginsdk.bean.UserLogin;
import org.mobile.htloginsdk.utils.HTSdk;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.http.RequestParams;
import org.xutils.x;

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
    private static volatile MyApp instance;
    private SharedPreferences sp;
    private static String appId;
    public static MyApp getInstance() {
        if (instance == null) {
            synchronized (MyApp.class) {
                if (instance == null) {
                    instance = new MyApp();
                }
            }
        }

        return instance;
    }
    private DbManager.DaoConfig daoConfig;
    public DbManager.DaoConfig getDaoConfig() {
        return daoConfig;
    }
    private DbManager db;
    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        daoConfig = new DbManager.DaoConfig()
                .setDbName("HtSdkLogin_db")//创建数据库的名称
                .setDbVersion(1)//数据库版本号
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                        // TODO: ...
                        // db.addColumn(...);
                        // db.dropTable(...);
                        // ...
                    }
                });//数据库更新操作
        db = x.getDb(daoConfig);
        sp = HTSdk.getSharedPreferences(this);
        if (!sp.getString("appId", "").equals("")) {
            appId = sp.getString("appId", "");
        }
    }

    public static String getAppId() {
        return appId;
    }

    public void saveData(UserLogin userLogin){
        if (userLogin!=null){
            try {
                db.save(userLogin);
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }

    public RequestParams getRequestParams(String url){
        RequestParams params = new RequestParams(url);
        return params;
    }
    public AlertDialog getAlertDialog(Context context){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(context).inflate(R.layout.alert_layout, null);
        tv_title = ((TextView) view.findViewById(R.id.alert_tv));
        iv_anim = ((ImageView) view.findViewById(R.id.alert_iv));
        iv_anim.setBackgroundResource(R.drawable.loadanimation);
        animation = ((AnimationDrawable) iv_anim.getBackground());
        builder.setView(view);
        //tv_title.setText(this.getString(R.string.loading));
        return builder.create();
    }
}

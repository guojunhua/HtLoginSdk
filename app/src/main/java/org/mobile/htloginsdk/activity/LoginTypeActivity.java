package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.bean.UserLogin;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;

/**
 * Created by 郭君华 on 2016/4/8.
 * Email：guojunhua3369@163.com
 */
public class LoginTypeActivity extends Activity {
    private SharedPreferences sp;
    private DbManager db;
    private UserLogin first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        sp = getSharedPreferences("login", MODE_PRIVATE);
        try {
            first = db.selector(UserLogin.class).where("username","=",sp.getString("username","")).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        if (sp.getInt("loginStats",0)!=0){
            if (sp.getInt("loginStats",0)==1||sp.getInt("loginStats",0)==3){
                if (first.getIsBind()==1){
                    startActivity(new Intent(LoginTypeActivity.this,BindLoginActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(LoginTypeActivity.this,AccountLoginActivity.class));
                    finish();
                }
            }else {
                startActivity(new Intent(LoginTypeActivity.this,AccountLoginActivity.class));
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            db.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

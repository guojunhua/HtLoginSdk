package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.bean.UserLogin;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭君华 on 2016/4/8.
 * Email：guojunhua3369@163.com
 */
public class JudgeActivity extends Activity {
    public DbManager db;
    private List<UserLogin> data;
    private SharedPreferences sp;
    private UserLogin first;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        Log.e("---data", "   " + (data.size() - 1));
        if ((data.size() - 1) == 0) {
            startActivity(new Intent(JudgeActivity.this, MainActivity.class));
            finish();
        } else {
            //startActivity(new Intent(JudgeActivity.this, LoginTypeActivity.class));
            //finish();
            if (sp.getInt("loginStats", 0) != 0 && first != null) {
                if (sp.getInt("loginStats", 0) == 1 || sp.getInt("loginStats", 0) == 3) {
                    if (first.getIsBind() == 1) {
                        startActivity(new Intent(JudgeActivity.this, BindLoginActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(JudgeActivity.this, AccountLoginActivity.class));
                        finish();
                    }
                } else {
                    startActivity(new Intent(JudgeActivity.this, AccountLoginActivity.class));
                    finish();
                }
            }
        }

    }

    public void init() {
        try {
            sp = getSharedPreferences("login", MODE_PRIVATE);
            db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
            data = db.selector(UserLogin.class).findAll();
            first = db.selector(UserLogin.class).where("username", "=", sp.getString("username", "")).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
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

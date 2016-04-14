package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        init();
        if (data==null) {
            startActivity(new Intent(JudgeActivity.this, MainActivity.class));
            finish();
        } else {
            startActivity(new Intent(JudgeActivity.this, LoginTypeActivity.class));
            finish();
        }

    }

    public void init() {
        try {
            db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
            data = db.selector(UserLogin.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}

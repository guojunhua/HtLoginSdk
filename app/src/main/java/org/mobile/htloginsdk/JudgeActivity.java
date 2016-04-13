package org.mobile.htloginsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.mobile.htloginsdk.bean.UserLogin;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by 郭君华 on 2016/4/8.
 * Email：guojunhua3369@163.com
 */
public class JudgeActivity extends Activity {
    public DbManager db;
    public List<UserLogin> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        try {
            data = db.selector(UserLogin.class).findAll();
            if (data!=null){
                startActivity(new Intent(JudgeActivity.this,LoginTypeActivity.class));
                finish();
            }else {
                startActivity(new Intent(JudgeActivity.this,MainActivity.class));
                finish();
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
}

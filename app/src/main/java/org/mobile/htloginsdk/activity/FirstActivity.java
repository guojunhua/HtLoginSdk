package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;

import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.utils.HTSdk;
import org.mobile.htloginsdk.utils.HeTuCallback;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.xutils.ex.HttpException;

/**
 * Created by 郭君华 on 2016/4/12.
 * Email：guojunhua3369@163.com
 */
public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HTSdk.sdkInitialize(this, "100000", "asdf");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_first);
    }
    public void onClick(View v){
        startActivity(new Intent(FirstActivity.this,JudgeActivity.class));
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        HtLoginManager.getInstance().registerCallback(new HeTuCallback<LoginBean>() {
            @Override
            public void onSuccess(LoginBean responseInfo) {

            }

            @Override
            public void onFailure(Throwable ex, boolean isOnCallback) {

            }
        });
    }
}

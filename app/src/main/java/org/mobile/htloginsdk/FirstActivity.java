package org.mobile.htloginsdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import org.mobile.htloginsdk.utils.HTSdk;

/**
 * Created by 郭君华 on 2016/4/12.
 * Email：guojunhua3369@163.com
 */
public class FirstActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        HTSdk.sdkInitialize(this,"100000","");
        setContentView(R.layout.activity_first);
    }
    public void onClick(View v){
        startActivity(new Intent(FirstActivity.this,JudgeActivity.class));
    }
}

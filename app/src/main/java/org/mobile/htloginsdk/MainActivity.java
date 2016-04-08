package org.mobile.htloginsdk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.facebook.ProfileTracker;

import org.mobile.htloginsdk.bean.User;
import org.mobile.htloginsdk.utils.Base64Utils;
import org.mobile.htloginsdk.utils.LogInStateListener;
import org.mobile.htloginsdk.utils.LoginManager;
import org.mobile.htloginsdk.utils.MacAddress;

import java.io.Serializable;

public class MainActivity extends Activity implements View.OnClickListener, LogInStateListener {
    private String appId;
    private String apiType;
    private String data;
    private String diviceId;
    private String brand;
    private Button btn_tourist, btn_account;
    public ProfileTracker profileTracker;
    public String name;
    public String id;
    private TextView tv_title;
    private ImageView iv_anim;
    private AnimationDrawable animation;
    private AlertDialog dialog;
    public Button btn_signup;
    public Button face;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        login();
    }

    private void initView() {
        btn_tourist = ((Button) findViewById(R.id.tourist));
        btn_account = ((Button) findViewById(R.id.account));
        btn_signup = ((Button) findViewById(R.id.signup));
        face = ((Button) findViewById(R.id.facebook1));
        btn_signup.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
        btn_account.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.alert_layout, null);
        tv_title = ((TextView) view.findViewById(R.id.alert_tv));
        iv_anim = ((ImageView) view.findViewById(R.id.alert_iv));
        iv_anim.setBackgroundResource(R.drawable.loadanimation);
        animation = ((AnimationDrawable) iv_anim.getBackground());
        builder.setView(view);
        tv_title.setText(this.getString(R.string.loading));
        dialog = builder.create();
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        if (btnId == R.id.tourist) {
            action1();
        } else if (btnId == R.id.account) {
            action2();
        } else if (btnId == R.id.signup) {
            action3();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        profileTracker.stopTracking();
        //LoginManager.getInstance().logOut();
        LoginManager.OnDestory();
    }

    public void action1() {
        animation.start();
        dialog.show();
        diviceId = new MacAddress(this).getMacAddressAndroid();
        brand = MacAddress.getBrand();
        Log.e("---divice---", "" + diviceId + "--------------" + brand);
        String userInfo = "username=" + diviceId + "#device&name=" + brand + "&uuid=" + diviceId;
        data = Base64Utils.backData(userInfo);
        String url = String.format(MyApp.url, apiType, appId, data);
    }

    public void action2() {
        Intent accountIntent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(accountIntent);
    }
    private void action3() {
        Intent signupIntent = new Intent(MainActivity.this, SignupActivity.class);
        startActivity(signupIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        LoginManager.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public void OnLoginSuccess(User user,String logType) {
//        Intent intent=new Intent(MainActivity.this,Result.class);
//        intent.putExtra("user",(Serializable)user);
//        intent.putExtra("logtype", logType);
//        startActivity(intent);

    }
    @Override
    public void OnLoginError(String error) {
        System.out.println(error);
    }
    private void login(){
        LoginManager.initialize(MainActivity.this);
        LoginManager.setFaceBookLoginParams(MainActivity.this, face, null, this);
    }
}

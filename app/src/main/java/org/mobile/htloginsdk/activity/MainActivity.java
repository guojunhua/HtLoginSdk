package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.bean.User;
import org.mobile.htloginsdk.utils.Base64Utils;
import org.mobile.htloginsdk.utils.DaoUtils;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.mobile.htloginsdk.utils.LoadingDialog;
import org.mobile.htloginsdk.utils.LogInStateListener;
import org.mobile.htloginsdk.utils.LoginManager;
import org.mobile.htloginsdk.utils.MacAddress;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.x;

public class MainActivity extends Activity implements View.OnClickListener, LogInStateListener {
    private String diviceId;
    private String brand;
    private Button btn_tourist, btn_account;
    public String name;
    public String id;
    public Button btn_signup;
    public Button btn_facebook;
    private SharedPreferences sp;
    private String appId;
    private SharedPreferences.Editor edit;
    private String data;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        initView();
        login();
    }

    private void initView() {
        btn_tourist = ((Button) findViewById(R.id.login_tourist));
        btn_account = ((Button) findViewById(R.id.login_account));
        btn_signup = ((Button) findViewById(R.id.login_signup));
        btn_facebook = ((Button) findViewById(R.id.login_facebook));
        btn_signup.setOnClickListener(this);
        btn_tourist.setOnClickListener(this);
        btn_account.setOnClickListener(this);
        dialog = new LoadingDialog(this);
        dialog.setCancelable(false);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (!sp.getString("appId", "").equals("")) {
            appId = sp.getString("appId", "");
        }
        edit = sp.edit();
    }

    @Override
    public void onClick(View v) {
        int btnId = v.getId();
        if (btnId == R.id.login_tourist) {
            toutistLogin();
        } else if (btnId == R.id.login_account) {
            accountLogin();
        } else if (btnId == R.id.login_signup) {
            accountSignup();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginManager.OnDestory();
    }

    public void toutistLogin() {
        dialog.show();
        diviceId = new MacAddress(this).getMacAddressAndroid();
        brand = MacAddress.getBrand();
        Log.e("---divice---", "" + diviceId + "--------------" + brand);
        String userInfo = "username=" + diviceId + "#device&name=" + brand + "&uuid=" + diviceId;
        data = Base64Utils.backData(userInfo);
        String url = String.format(MyApp.url, "login", appId, data);
        Log.e("---url", appId + "  " + url);
        requestJsonData(url, diviceId, brand, 1);
    }

    public void accountLogin() {
        Intent login = new Intent(MainActivity.this, LoginActivity.class);
        login.putExtra("loginType",1);
        startActivity(login);
        finish();
    }

    private void accountSignup() {
        startActivity(new Intent(MainActivity.this, SignupActivity.class));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoginManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnLoginSuccess(User user, String logType) {
        dialog.show();
        id = user.getUserId();
        name = user.getUserName();
        String userInfo = "username=" + id + "#facebook&name=" + name + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
        data = Base64Utils.backData(userInfo);
        String url = String.format(MyApp.url, "login", appId, data);
        requestJsonData(url, id, name, 3);
    }

    @Override
    public void OnLoginError(String error) {
        System.out.println(error);
    }

    private void login() {
        LoginManager.initialize(MainActivity.this);
        LoginManager.setFaceBookLoginParams(MainActivity.this, btn_facebook, null, this);
    }

    public void requestJsonData(String url, final String username, final String password, final int stats) {
        final HtLoginManager htLoginManager = HtLoginManager.getInstance();
        x.http().get(MyApp.getInstance().getRequestParams(url), new Callback.CommonCallback<String>() {
            private LoginBean loginBean;

            @Override
            public void onSuccess(String result) {
                loginBean = new Gson().fromJson(result, LoginBean.class);
                htLoginManager.setLoginBean(loginBean);
                if (loginBean != null) {
                    if (loginBean.getCode() == 0) {
                        DbManager db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
                        DaoUtils.saveData(username, password, stats, 1, loginBean,"","", db);
                        edit.putString("username", username);
                        edit.putInt("loginStats", stats);
                        edit.putInt("bindStats", 1);
                        edit.apply();
                        Toast.makeText(MainActivity.this, R.string.login_success_orher, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (loginBean.getCode()==40106){
                        Toast.makeText(MainActivity.this, R.string.user_exist_tip, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(MainActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                        Log.e("LoginErrorMassage","Code:"+loginBean.getCode()+"Massage:"+loginBean.getMsg());
                    }
                    dialog.dismiss();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                htLoginManager.setException(ex);
                htLoginManager.setMsg(isOnCallback);
            }
            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(MainActivity.this, R.string.loginBack, Toast.LENGTH_SHORT).show();
    }
}

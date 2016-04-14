package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import org.mobile.htloginsdk.utils.HtLoginManager;
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
        diviceId = new MacAddress(this).getMacAddressAndroid();
        brand = MacAddress.getBrand();
        Log.e("---divice---", "" + diviceId + "--------------" + brand);
        String userInfo = "username=" + diviceId + "#device&name=" + brand + "&uuid=" + diviceId;
        data = Base64Utils.backData(userInfo);
        String url = String.format(MyApp.url, "login", "100000", data);
        Log.e("---url",appId+"  "+url);
        requestJsonData(url, diviceId, brand, 1);
    }

    public void accountLogin() {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
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
                        MyApp.getInstance().saveData(username, password, stats,1, loginBean,db);
                        edit.putString("username", username);
                        edit.putInt("loginStats", stats);
                        edit.putBoolean("bindStats",false);
                        edit.apply();
                        Toast.makeText(MainActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (loginBean.getCode() == 1) {
                        //Toast.makeText(MainActivity.this, R.string.error_operation, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40100) {
                        //Toast.makeText(MainActivity.this, R.string.error_RSA, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40101) {
                        //Toast.makeText(MainActivity.this, R.string.error_parametr, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40102) {
                        //Toast.makeText(MainActivity.this, R.string.error_token, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40103) {
                        //Toast.makeText(MainActivity.this, R.string.over_time, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40105) {
                        //Toast.makeText(MainActivity.this, R.string.login_field_tip, Toast.LENGTH_SHORT).show();

                    } else if (loginBean.getCode() == 40106) {
                        //Toast.makeText(MainActivity.this, R.string.user_exist_tip, Toast.LENGTH_SHORT).show();

                    }
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                loginManager.setException(error);
//                loginManager.setMsg(msg);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }
}

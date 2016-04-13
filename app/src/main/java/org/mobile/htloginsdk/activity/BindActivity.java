package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.utils.Base64Utils;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public class BindActivity extends Activity implements View.OnClickListener{
    private ImageView bind_back;
    private EditText edit_account;
    private EditText edit_password;
    private EditText edit_password_again;
    private Button bind;
    private String username;
    private String password;
    private String passwordAgain;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bind);
        initView();
    }

    private void initView() {
        bind_back = ((ImageView) findViewById(R.id.bind_back));
        edit_account = ((EditText) findViewById(R.id.bind_edit_account));
        edit_password = ((EditText) findViewById(R.id.bind_edit_password));
        edit_password_again = ((EditText) findViewById(R.id.bind_edit_password_again));
        bind = ((Button) findViewById(R.id.bind_btn));
        bind.setOnClickListener(this);
        bind_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
       if (v.getId()==R.id.bind_back){
           finish();
       }else if (v.getId()==R.id.bind_btn){
           username = edit_account.getText().toString();
           password = edit_password.getText().toString();
           passwordAgain = edit_password_again.getText().toString();
           if (Base64Utils.isEmpty(username, password, passwordAgain)) {
               //Toast.makeText(SignupActivity.this, R.string.null_email_or_password_tip, Toast.LENGTH_SHORT).show();
           } else if (username.length() < 6 || username.length() > 32) {
               //Toast.makeText(SignupActivity.this, R.string.illegal_length_tip, Toast.LENGTH_SHORT).show();
               edit_account.setText("");
           } else if (Base64Utils.isContains(username) && !Base64Utils.isEmail(username)) {
               //Toast.makeText(SignupActivity.this, R.string.illegal_uname_tip, Toast.LENGTH_SHORT).show();
               edit_account.setText("");
           } else if (password.length() < 6 || password.length() > 32) {
               //Toast.makeText(SignupActivity.this, R.string.illegal_pwd_tip, Toast.LENGTH_SHORT).show();
               edit_password.setText("");
           } else if (!password.equals(passwordAgain)) {
               //Toast.makeText(SignupActivity.this, R.string.illegal_contain_tip, Toast.LENGTH_SHORT).show();
           } else {
               String userInfo = "username=" + username + "&password=" + password;
               Log.e("--hvd--", " " + userInfo);
               data = Base64Utils.backData(userInfo);
               //String url = String.format(MyApp.url, "register", MyApp.getAppId(), data);
//               Log.e("---url--", " " + url);
//               requestJsonData(url);
           }
       }
    }
    public void requestJsonData(String url) {
        final HtLoginManager htLoginManager = HtLoginManager.getInstance();
        x.http().get(MyApp.getInstance().getRequestParams(url), new Callback.CommonCallback<String>() {
            private LoginBean loginBean;

            @Override
            public void onSuccess(String result) {
                loginBean = new Gson().fromJson(result, LoginBean.class);
                htLoginManager.setLoginBean(loginBean);
                if (loginBean != null) {
                    if (loginBean.getCode() == 0) {
                        Toast.makeText(BindActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
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

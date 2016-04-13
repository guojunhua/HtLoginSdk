package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.utils.Base64Utils;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.mobile.htloginsdk.utils.MacAddress;
import org.xutils.common.Callback;
import org.xutils.x;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public class LoginActivity extends Activity implements View.OnClickListener {
    private ImageView login_back;
    private EditText edit_account;
    private EditText edit_password;
    private CheckBox check;
    private TextView forgetPassword;
    private Button login;
    private String username;
    private String password;
    private String data;
    private TextView login_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        initView();
        //将TextView的显示文字设置为SpannableString
        login_agreement.setText(getClickableSpan());
        //设置该句使文本的超连接起作用
        login_agreement.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void initView() {
        login_back = ((ImageView) findViewById(R.id.login_back));
        edit_account = ((EditText) findViewById(R.id.login_edit_account));
        edit_password = ((EditText) findViewById(R.id.login_edit_password));
        check = ((CheckBox) findViewById(R.id.login_check));
        forgetPassword = ((TextView) findViewById(R.id.forgetpassword));
        login = ((Button) findViewById(R.id.login_btn));
        login_agreement = ((TextView) findViewById(R.id.login_agreement));
        login.setOnClickListener(this);
        login_back.setOnClickListener(this);
        forgetPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login_back) {
            finish();
        } else if (v.getId() == R.id.login_btn) {
            username = edit_account.getText().toString();
            password = edit_password.getText().toString();
            if (Base64Utils.isEmpty(username, password)) {
                //Toast.makeText(LoginActivity.this, R.string.null_email_or_password_tip, Toast.LENGTH_SHORT).show();
            } else if (username.length() < 6 || username.length() > 32) {
                // Toast.makeText(LoginActivity.this, R.string.illegal_length_tip, Toast.LENGTH_SHORT).show();
                edit_account.setText("");
            } else if (Base64Utils.isContains(username) && !Base64Utils.isEmail(username)) {
                //Toast.makeText(LoginActivity.this, R.string.illegal_uname_tip, Toast.LENGTH_SHORT).show();
                edit_account.setText("");
            } else if (password.length() < 6 || password.length() > 32) {
                //Toast.makeText(LoginActivity.this, R.string.illegal_pwd_tip, Toast.LENGTH_SHORT).show();
                edit_password.setText("");
            } else if (!check.isChecked()) {
                //Toast.makeText(LoginActivity.this, R.string.illegal_contain_tip, Toast.LENGTH_SHORT).show();
            } else {
                String userInfo = "username=" + username + "&password=" + password + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
                data = Base64Utils.backData(userInfo);
//                String url = String.format(MyApp.url, "login", MyApp.getAppId(), data) + "&format=json";
//                Log.e("---gege---", " " + userInfo + "----" + url);
//                requestJsonData(url);
            }
        } else if (v.getId() == R.id.forgetpassword) {
            startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
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
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
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

    //设置超链接文字
    private SpannableString getClickableSpan() {
        SpannableString spanStr = new SpannableString(getString(R.string.ACCOUNT));
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                startActivity(new Intent(LoginActivity.this, WebActivity.class));
            }
        }, 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景
        spanStr.setSpan(new ForegroundColorSpan(Color.rgb(242, 20, 20)), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {

                startActivity(new Intent(LoginActivity.this, WebActivity.class));
            }
        }, 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(Color.rgb(242, 20, 20)), 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }
}

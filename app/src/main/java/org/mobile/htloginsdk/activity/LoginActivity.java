package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
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
import org.mobile.htloginsdk.utils.DaoUtils;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.mobile.htloginsdk.utils.LoadingDialog;
import org.mobile.htloginsdk.utils.MacAddress;
import org.xutils.DbManager;
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
    private SharedPreferences sp;
    private String appId;
    private SharedPreferences.Editor edit;
    private TextView login_agreement;
    private LoadingDialog dialog;

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
        if (v.getId() == R.id.login_back) {
            onBack();
        } else if (v.getId() == R.id.login_btn) {
            username = edit_account.getText().toString();
            password = edit_password.getText().toString();
            if (Base64Utils.isEmpty(username, password)) {
                Toast.makeText(LoginActivity.this, R.string.null_email_or_password_tip, Toast.LENGTH_SHORT).show();
            } else if (username.length() < 6 || username.length() > 32) {
                Toast.makeText(LoginActivity.this, R.string.illegal_length_tip, Toast.LENGTH_SHORT).show();
                edit_account.setText("");
            } else if (!Base64Utils.isEmail(username)) {
                Toast.makeText(LoginActivity.this, R.string.illegal_uname_tip, Toast.LENGTH_SHORT).show();
                edit_account.setText("");
            } else if (password.length() < 6 || password.length() > 32) {
                Toast.makeText(LoginActivity.this, R.string.illegal_pwd_tip, Toast.LENGTH_SHORT).show();
                edit_password.setText("");
            } else if (!check.isChecked()) {
                Toast.makeText(LoginActivity.this, R.string.agreement_tip, Toast.LENGTH_SHORT).show();
            } else {
                dialog.show();
                String userInfo = "username=" + username + "&password=" + password + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
                data = Base64Utils.backData(userInfo);
                String url = String.format(MyApp.url, "login", appId, data) + "&format=json";
                Log.e("---gege---", " " + userInfo + "----" + url);
                requestJsonData(url);
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
                        DbManager db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
                        DaoUtils.saveData(username, password, 2, 2, loginBean, username, password, db);
                        edit.putString("username", username);
                        edit.putInt("loginStats", 2);
                        edit.putInt("bindStats", 2);
                        edit.apply();
                        Toast.makeText(LoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (loginBean.getCode() == 40106) {
                        Toast.makeText(LoginActivity.this, R.string.user_exist_tip, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
                        Log.e("LoginErrorMassage", "Code:" + loginBean.getCode() + "Massage:" + loginBean.getMsg());
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

    //设置超链接文字
    private SpannableString getClickableSpan() {
        SpannableString spanStr = new SpannableString(getString(R.string.ACCOUNT));
        //设置下划线文字
        spanStr.setSpan(new UnderlineSpan(), 3, 7, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的单击事件
        spanStr.setSpan(new ClickableSpan() {

            @Override
            public void onClick(View widget) {
                Intent service = new Intent(LoginActivity.this, AgreementActivity.class);
                service.putExtra("type", 1);
                startActivity(service);
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
                Intent service = new Intent(LoginActivity.this, AgreementActivity.class);
                service.putExtra("type", 2);
                startActivity(service);
            }
        }, 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //设置文字的前景色
        spanStr.setSpan(new ForegroundColorSpan(Color.rgb(242, 20, 20)), 8, 12, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return spanStr;
    }

    public void onBack() {
        int loginType = getIntent().getIntExtra("loginType", 0);
        if (loginType != 0 && loginType == 1) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else if (loginType != 0 && loginType == 2) {
            startActivity(new Intent(LoginActivity.this, BindLoginActivity.class));
            finish();
        } else if (loginType != 0 && loginType == 3) {
            startActivity(new Intent(LoginActivity.this, AccountLoginActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        onBack();
    }
}

package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.content.SharedPreferences;
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
import org.mobile.htloginsdk.utils.LoadingDialog;
import org.xutils.DbManager;
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
    private SharedPreferences sp;
    private String appId;
    private SharedPreferences.Editor edit;
    private LoadingDialog dialog;

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
       if (v.getId()==R.id.bind_back){
           finish();
       }else if (v.getId()==R.id.bind_btn){
           username = edit_account.getText().toString();
           password = edit_password.getText().toString();
           passwordAgain = edit_password_again.getText().toString();
           if (Base64Utils.isEmpty(username, password, passwordAgain)) {
               Toast.makeText(BindActivity.this, R.string.null_email_or_password_tip, Toast.LENGTH_SHORT).show();
           } else if (username.length() < 6 || username.length() > 32) {
               Toast.makeText(BindActivity.this, R.string.illegal_length_tip, Toast.LENGTH_SHORT).show();
               edit_account.setText("");
           } else if (!Base64Utils.isEmail(username)) {
               Toast.makeText(BindActivity.this, R.string.illegal_uname_tip, Toast.LENGTH_SHORT).show();
               edit_account.setText("");
           } else if (password.length() < 6 || password.length() > 32) {
               Toast.makeText(BindActivity.this, R.string.illegal_pwd_tip, Toast.LENGTH_SHORT).show();
               edit_password.setText("");
           } else if (!password.equals(passwordAgain)) {
               Toast.makeText(BindActivity.this, R.string.signup_password_different, Toast.LENGTH_SHORT).show();
           } else {
               dialog.show();
               String userInfo = "username=" + username + "&password=" + password;
               Log.e("--hvd--", " " + userInfo);
               data = Base64Utils.backData(userInfo);
               String url = String.format(MyApp.url, "bind", appId, data);
               Log.e("---url--", " " + url);
               requestJsonData(url);
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
//                        DbManager db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
//                        MyApp.getInstance().saveData(username, password, 2,2, loginBean,db);
                        edit.putString("username", username);
                        edit.putInt("loginStats", 2);
                        edit.putInt("bindStats",2);
                        edit.apply();
                        Toast.makeText(BindActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (loginBean.getCode()==40106){
                        Toast.makeText(BindActivity.this, R.string.user_exist_tip, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(BindActivity.this, R.string.bind_fail, Toast.LENGTH_SHORT).show();
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
}

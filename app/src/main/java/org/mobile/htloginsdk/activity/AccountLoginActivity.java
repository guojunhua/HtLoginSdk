package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.bean.User;
import org.mobile.htloginsdk.bean.UserLogin;
import org.mobile.htloginsdk.utils.Base64Utils;
import org.mobile.htloginsdk.utils.DaoUtils;
import org.mobile.htloginsdk.utils.HtLoginManager;
import org.mobile.htloginsdk.utils.LoadingDialog;
import org.mobile.htloginsdk.utils.LogInStateListener;
import org.mobile.htloginsdk.utils.LoginManager;
import org.mobile.htloginsdk.utils.MacAddress;
import org.xutils.DbManager;
import org.xutils.common.Callback;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public class AccountLoginActivity extends Activity implements View.OnClickListener, LogInStateListener, AdapterView.OnItemClickListener {
    private ImageView logo;
    private TextView email;
    private ImageView down;
    private Button login;
    private Button signup;
    private Button facebook;
    public String name;
    public String id;
    private SharedPreferences sp;
    private String appId;
    private SharedPreferences.Editor edit;
    private String data;
    private List<UserLogin> datas;
    private List<String> numbers;
    private NumbersAdapter numbersAdapter;
    private PopupWindow popupWindow;
    private DbManager db;
    private LinearLayout linear;
    private String loginAccount;
    private LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_accountlogin);
        initView();
        logo.setImageResource(R.drawable.account);
        email.setText(sp.getString("username", ""));
        login();
    }

    private void initView() {
        logo = ((ImageView) findViewById(R.id.accountlogin_logo));
        email = ((TextView) findViewById(R.id.accountlogin_email));
        down = ((ImageView) findViewById(R.id.accountlogin_down));
        login = ((Button) findViewById(R.id.accountlogin_login));
        signup = ((Button) findViewById(R.id.accountlogin_signup));
        facebook = ((Button) findViewById(R.id.accountlogin_facebook));
        linear = ((LinearLayout) findViewById(R.id.accountlogin_linear));
        down.setOnClickListener(this);
        login.setOnClickListener(this);
        signup.setOnClickListener(this);
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
        if (v.getId() == R.id.accountlogin_down) {
            showSelectNumberDialog();
        } else if (v.getId() == R.id.accountlogin_login) {
            loginAccount();
        } else if (v.getId() == R.id.accountlogin_signup) {
            startActivity(new Intent(AccountLoginActivity.this, SignupActivity.class));
        }
    }

    private void loginAccount() {
        dialog.show();
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        loginAccount = email.getText().toString();
        UserLogin userLogin = DaoUtils.findOne(loginAccount, db);
        if (userLogin != null) {
            String userInfo = "username=" + userLogin.getUsername() + "&password=" + userLogin.getPassword() + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
            data = Base64Utils.backData(userInfo);
            String url = String.format(MyApp.url, "login", appId, data) + "&format=json";
            Log.e("---gege---", " " + userInfo + "----" + url);
            requestJsonData(url, userLogin.getUsername(), userLogin.getPassword(), 2, 2, userLogin.getUsername(), userLogin.getPassword());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        LoginManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnLoginSuccess(User user, String logType) {
        dialog.show();
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        UserLogin userLogin = DaoUtils.findOne(user.getUserId(), db);
        if (userLogin != null) {
            id = user.getUserId();
            name = user.getUserName();
            String userInfo = "username=" + userLogin.getUsername() + "#facebook&name=" + userLogin.getPassword() + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
            data = Base64Utils.backData(userInfo);
            String url = String.format(MyApp.url, "login", appId, data);
            requestJsonData(url, userLogin.getUsername(), userLogin.getPassword(), 3, userLogin.getIsBind(), userLogin.getEmail(), userLogin.getEmailPassword());
        } else {
            id = user.getUserId();
            name = user.getUserName();
            String userInfo = "username=" + id + "#facebook&name=" + name + "&uuid=" + new MacAddress(this).getMacAddressAndroid();
            data = Base64Utils.backData(userInfo);
            String url = String.format(MyApp.url, "login", appId, data);
            requestJsonData(url, id, name, 3, 1, "", "");
        }
    }

    @Override
    public void OnLoginError(String error) {
        System.out.println(error);
    }

    private void login() {
        LoginManager.initialize(AccountLoginActivity.this);
        LoginManager.setFaceBookLoginParams(AccountLoginActivity.this, facebook, null, this);
    }

    /**
     * 弹出选择号码对话框
     */
    private void showSelectNumberDialog() {
        numbers = getNumbers();
        ListView lv = new ListView(this);
        lv.setBackgroundResource(R.drawable.whilt);
        lv.setDivider(new ColorDrawable(Color.rgb(242, 20, 20)));
        lv.setDividerHeight(1);
        lv.setOnItemClickListener(this);
        numbersAdapter = new NumbersAdapter();
        lv.setAdapter(numbersAdapter);
        popupWindow = new PopupWindow(lv, linear.getWidth(), LinearLayout.LayoutParams.WRAP_CONTENT);
        // 设置点击外部可以被关闭
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置popupWindow可以得到焦点
        popupWindow.setFocusable(true);
        // 显示
        popupWindow.showAsDropDown(linear);
    }

    private List<String> getNumbers() {
        List<String> numbers = new ArrayList<String>();
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        datas = DaoUtils.init(db);
        Log.e("---user1", datas.toString());
        for (int i = 0; i < datas.size(); i++) {
            if (datas.get(i).getIsBind() != 1&&datas.get(i).getLoginStats()==2) {
                numbers.add(datas.get(i).getUsername());
            }
        }
        numbers.add("其他账户登录");
        return numbers;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if ((position + 1) == numbers.size()) {
            Intent accountLogin = new Intent(AccountLoginActivity.this, LoginActivity.class);
            accountLogin.putExtra("loginType",3);
            startActivity(accountLogin);
            popupWindow.dismiss();
            finish();
        } else {
            String number = numbers.get(position);
            email.setText(number);
        }
        popupWindow.dismiss();
    }

    public void requestJsonData(String url, final String username, final String password, final int stats, final int isBind, final String email, final String emailPassword) {
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
                        DaoUtils.saveData(username, password, stats, isBind, loginBean, email, emailPassword, db);
                        edit.putString("username", username);
                        edit.putInt("loginStats", stats);
                        edit.putInt("bindStats", isBind);
                        edit.apply();
                        Toast.makeText(AccountLoginActivity.this, R.string.login_success, Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (loginBean.getCode() == 40106) {
                        Toast.makeText(AccountLoginActivity.this, R.string.user_exist_tip, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(AccountLoginActivity.this, R.string.login_fail, Toast.LENGTH_SHORT).show();
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

    class NumbersAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numbers != null ? numbers.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return numbers != null ? numbers.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            NumberViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new NumberViewHolder();
                convertView = LayoutInflater.from(AccountLoginActivity.this).inflate(R.layout.item_spinner_numbers, null);
                mHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                mHolder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
                mHolder.logo = ((ImageView) convertView.findViewById(R.id.id_logo));
                convertView.setTag(mHolder);
            } else {
                mHolder = (NumberViewHolder) convertView.getTag();
            }

            if ((position + 1) == numbers.size()) {
                mHolder.logo.setVisibility(View.INVISIBLE);
                mHolder.ibDelete.setVisibility(View.INVISIBLE);
                mHolder.tvNumber.setText(numbers.get(position));
            } else {
                mHolder.logo.setVisibility(View.VISIBLE);
                mHolder.ibDelete.setVisibility(View.VISIBLE);
                mHolder.logo.setImageResource(R.drawable.account);
                mHolder.tvNumber.setText(numbers.get(position));
                mHolder.ibDelete.setTag(position);
                mHolder.ibDelete.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(final View v) {
                        final AlertDialog.Builder builder = new AlertDialog.Builder(AccountLoginActivity.this);
                        View view = LayoutInflater.from(AccountLoginActivity.this).inflate(R.layout.dialog_view, null);
                        builder.setCustomTitle(view);
                        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                int index = (Integer) v.getTag();
                                DaoUtils.deleteDate(numbers.get(position), db);
                                datas = DaoUtils.init(db);
                                if ((datas.size()-1) == 0) {
                                    startActivity(new Intent(AccountLoginActivity.this, MainActivity.class));
                                    popupWindow.dismiss();
                                    finish();
                                }
                                numbers.remove(index);
                                numbersAdapter.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).create().show();
                    }
                });
            }

            return convertView;
        }

        public class NumberViewHolder {
            public TextView tvNumber;
            public ImageButton ibDelete;
            public ImageView logo;
        }
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(AccountLoginActivity.this, R.string.loginBack, Toast.LENGTH_SHORT).show();
    }
}

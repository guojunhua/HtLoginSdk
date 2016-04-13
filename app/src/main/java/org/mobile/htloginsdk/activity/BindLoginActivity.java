package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
import org.mobile.htloginsdk.utils.HtLoginManager;
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
public class BindLoginActivity extends Activity implements View.OnClickListener, LogInStateListener, AdapterView.OnItemClickListener {
    private ImageView logo;
    private TextView account;
    private ImageView down;
    private Button login;
    private Button bind;
    private Button facebook;
    public String name;
    public String id;
    private SharedPreferences sp;
    private String appId;
    private SharedPreferences.Editor edit;
    private String data;
    private List<UserLogin> numbers;
    private NumbersAdapter numbersAdapter;
    private PopupWindow popupWindow;
    private DbManager db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_bindlogin);
        initView();
        if (sp.getInt("loginStats",0)==1){
            logo.setImageResource(R.drawable.tourst);
            account.setText("游客ID" + sp.getString("username", ""));
        }else if (sp.getInt("loginStats",0)==3){
            logo.setImageResource(R.drawable.facebook_logo);
            account.setText(sp.getString("username", ""));
        }
        login();
    }

    private void initView() {
        logo = ((ImageView) findViewById(R.id.bindlogin_logo));
        account = ((TextView) findViewById(R.id.bindlogin_account));
        down = ((ImageView) findViewById(R.id.bindlogin_dowm));
        login = ((Button) findViewById(R.id.bindlogin_btn_login));
        bind = ((Button) findViewById(R.id.bindlogin_btn_bind));
        facebook = ((Button) findViewById(R.id.bindlogin_btn_facebook));
        down.setOnClickListener(this);
        login.setOnClickListener(this);
        bind.setOnClickListener(this);
        sp = getSharedPreferences("login", MODE_PRIVATE);
        if (!sp.getString("appId", "").equals("")) {
            appId = sp.getString("appId", "");
        }
        edit = sp.edit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bindlogin_dowm) {
            showSelectNumberDialog();
        } else if (v.getId() == R.id.bindlogin_btn_login) {

        } else if (v.getId() == R.id.bindlogin_btn_bind) {
            startActivity(new Intent(BindLoginActivity.this, BindActivity.class));
        }
    }

    /**
     * 弹出选择号码对话框
     */
    private void showSelectNumberDialog() {
        numbers = getNumbers();
        Log.e("--user",numbers.toString());
        ListView lv = new ListView(this);
        lv.setBackgroundResource(R.drawable.whilt);
        // 隐藏滚动条
        lv.setVerticalScrollBarEnabled(false);
        // 让listView没有分割线
        lv.setDivider(new ColorDrawable(Color.rgb(242, 20, 20)));
        lv.setDividerHeight(1);
        lv.setOnItemClickListener(this);
        numbersAdapter = new NumbersAdapter();
        lv.setAdapter(numbersAdapter);
        popupWindow = new PopupWindow(lv, 685, 200);
        // 设置点击外部可以被关闭
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

        // 设置popupWindow可以得到焦点
        popupWindow.setFocusable(true);

        popupWindow.showAsDropDown(account, -110, 1);        // 显示

    }
    private List<UserLogin> getNumbers() {
        db = x.getDb(((MyApp) getApplicationContext()).getDaoConfig());
        List<UserLogin> numbers = new ArrayList<UserLogin>();
        try {
            List<UserLogin> data = db.selector(UserLogin.class).findAll();
            Log.e("---user1",data.toString());
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i).getIsBind()==1) {
                    numbers.add(data.get(i));
                }
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
        return numbers;
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(this, "ListView的第" + position + "个Item被点击了..", Toast.LENGTH_LONG).show();
        String number = numbers.get(position).getUsername();
        account.setText(number);
        popupWindow.dismiss();
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
        LoginManager.initialize(BindLoginActivity.this);
        LoginManager.setFaceBookLoginParams(BindLoginActivity.this, facebook, null, this);
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
                        MyApp.getInstance().saveData(username, password, stats,1, loginBean);
                        edit.putString("username", username);
                        edit.putInt("loginStats", stats);
                        edit.putBoolean("bindStats", false);
                        edit.apply();
                        Toast.makeText(BindLoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
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
    class NumbersAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return numbers!=null?numbers.size():0;
        }
        @Override
        public Object getItem(int position) {
            return numbers!=null?numbers.get(position):null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            NumberViewHolder mHolder = null;
            if (convertView == null) {
                mHolder = new NumberViewHolder();
                convertView = LayoutInflater.from(BindLoginActivity.this).inflate(R.layout.item_spinner_numbers, null);
                mHolder.tvNumber = (TextView) convertView.findViewById(R.id.tv_number);
                mHolder.ibDelete = (ImageButton) convertView.findViewById(R.id.ib_delete);
                mHolder.logo = ((ImageView) convertView.findViewById(R.id.id_logo));
                convertView.setTag(mHolder);
            } else {
                mHolder = (NumberViewHolder) convertView.getTag();
            }

            mHolder.tvNumber.setText(numbers.get(position).getUsername());
            mHolder.ibDelete.setTag(position);
            mHolder.ibDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(BindLoginActivity.this);
                    View view = LayoutInflater.from(BindLoginActivity.this).inflate(R.layout.dialog_view,null);
                    builder.setCustomTitle(view);
                    builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            int index = (Integer) v.getTag();
                            numbers.remove(index);
                            numbersAdapter.notifyDataSetChanged();
                            if (numbers.size() == 0) {
                                popupWindow.dismiss();
                            }
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

            return convertView;
        }
        public class NumberViewHolder {
            public TextView tvNumber;
            public ImageButton ibDelete;
            public ImageView logo;
        }

    }
}

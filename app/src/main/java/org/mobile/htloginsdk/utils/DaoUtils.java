package org.mobile.htloginsdk.utils;

import android.util.Log;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.bean.LoginBean;
import org.mobile.htloginsdk.bean.User;
import org.mobile.htloginsdk.bean.UserLogin;
import org.xutils.DbManager;
import org.xutils.ex.DbException;
import org.xutils.x;

import java.util.List;

/**
 * Created by 郭君华 on 2016/4/15.
 * Email：guojunhua3369@163.com
 */
public class DaoUtils {

    public static void saveData(String username, String password, int stats, int isBind, LoginBean loginBean, String email, String emailPassword,DbManager db) {
        if (loginBean != null) {
            try {
                db.save(new UserLogin(username, password, stats, isBind, loginBean.getData().getUid(), loginBean.getData().getToken(), loginBean.getData().getName(), email, emailPassword));
                Log.e("---save", "保存成功");
            } catch (DbException e) {
                e.printStackTrace();
            }
        }
    }
    public static List<UserLogin> init(DbManager db) {
        List<UserLogin> datas = null;
        try {
            datas = db.selector(UserLogin.class).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return datas;
    }
    public static void deleteDate(String username,DbManager db) {
        try {
            UserLogin userLogin = db.selector(UserLogin.class).where("username", "=", username).findFirst();
            if (userLogin != null) {
                db.delete(userLogin);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }
    public static UserLogin findOne(String username,DbManager db){
        UserLogin userLogin = null;
        try {
           userLogin = db.selector(UserLogin.class).where("username","=",username).findFirst();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return userLogin;
    }
}

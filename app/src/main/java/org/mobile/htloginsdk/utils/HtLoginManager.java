package org.mobile.htloginsdk.utils;

import android.util.Log;

import org.mobile.htloginsdk.bean.LoginBean;
import org.xutils.ex.HttpException;

/**
 * Created by 郭君华 on 2016/3/4.
 * Email：guojunhua3369@163.com
 */
public class HtLoginManager {
    private static volatile HtLoginManager instance;
    private LoginBean loginBean;
    private Throwable exception;
    private boolean msg;

    public HtLoginManager() {
    }

    public HtLoginManager(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public HtLoginManager(Throwable ex, boolean isOnCallback) {
        this.exception = exception;
        this.msg = msg;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public boolean getMsg() {
        return msg;
    }

    public void setMsg(boolean msg) {
        this.msg = msg;
    }

    public static HtLoginManager getInstance() {
        if (instance == null) {
            synchronized (HtLoginManager.class) {
                if (instance == null) {
                    instance = new HtLoginManager();
                }
            }
        }

        return instance;
    }
    public void registerCallback(
            final HeTuCallback<LoginBean> callback) {
        if (loginBean!=null){
            Log.e("--callback--","hg"+loginBean.toString());
            callback.onSuccess(loginBean);
        }
        if (exception!=null){
            callback.onFailure(exception,msg);
        }
    }
}

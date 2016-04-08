package org.mobile.htloginsdk;

import android.util.Log;
import com.lidroid.xutils.exception.HttpException;

import org.mobile.htloginsdk.bean.LoginBean;

/**
 * Created by 郭君华 on 2016/3/4.
 * Email：guojunhua3369@163.com
 */
public class HtLoginManager {
    private static volatile HtLoginManager instance;
    private LoginBean loginBean;
    private HttpException exception;
    private String msg;

    public HtLoginManager() {
    }

    public HtLoginManager(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public HtLoginManager(HttpException exception, String msg) {
        this.exception = exception;
        this.msg = msg;
    }

    public LoginBean getLoginBean() {
        return loginBean;
    }

    public void setLoginBean(LoginBean loginBean) {
        this.loginBean = loginBean;
    }

    public HttpException getException() {
        return exception;
    }

    public void setException(HttpException exception) {
        this.exception = exception;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
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

package org.mobile.htloginsdk.utils;

import org.mobile.htloginsdk.bean.User;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public interface LogInStateListener {
    public  void OnLoginSuccess(User user,String logType);
    public  void OnLoginError(String error);
}
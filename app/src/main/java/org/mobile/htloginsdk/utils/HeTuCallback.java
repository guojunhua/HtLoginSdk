package org.mobile.htloginsdk.utils;


import org.xutils.ex.HttpException;

/**
 * Created by 郭君华 on 2016/3/3.
 * Email：guojunhua3369@163.com
 */
public abstract class HeTuCallback<T> {

    public abstract void onSuccess(T responseInfo);

    public abstract void onFailure(Throwable ex, boolean isOnCallback);
}

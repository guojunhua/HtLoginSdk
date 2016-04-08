package org.mobile.htloginsdk;

import com.lidroid.xutils.exception.HttpException;

/**
 * Created by 郭君华 on 2016/3/3.
 * Email：guojunhua3369@163.com
 */
public abstract class HeTuCallback<T> {

    public abstract void onSuccess(T responseInfo);

    public abstract void onFailure(HttpException error, String msg);
}

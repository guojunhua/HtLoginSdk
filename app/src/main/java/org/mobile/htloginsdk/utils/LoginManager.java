package org.mobile.htloginsdk.utils;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
public  class LoginManager {
    private static FaceBookLoginUtil fbUtil=null;
    /**
     * 登陆工具类，
     *
     */
    public static void initialize(Context context){
        fbUtil=FaceBookLoginUtil.getInstance();
    }
    /**
     * 1 if you use facebook account login,you must call this method and set params.
     * @param activity
     * @param loginButton
     * @param arrarPermission
     * @param loginstateListener
     */
    public  static void setFaceBookLoginParams(Activity activity,View loginButton,String arrarPermission,LogInStateListener loginstateListener){
        fbUtil.SetFaceBookLoginActivity(activity);
        fbUtil.SetFaceBookLoginButton(loginButton);
        fbUtil.SetFaceBookReadPermission(arrarPermission);
        fbUtil.SetOnFaceBookLoginStateListener(loginstateListener);
        fbUtil.open();
    }
    /**
     * if you use facebook logout,you must call this method and set params
     * @param logoutButton
     * @param logOutStateListener
     */
    public static void setFaceBookLogOutParams(View logoutButton,LogOutStateListener logOutStateListener){
        //fbUtil.SetFaceBookLoginActivity(activity);
        fbUtil.SetFaceBookLogOutButton(logoutButton);
        fbUtil.SetOnFaceBookLogOutListener(logOutStateListener);
        fbUtil.open();
    };
    public static void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(fbUtil!=null){
            fbUtil.onActivityResult(requestCode, resultCode, data);
        }
    }
    public static void OnDestory(){
        if(fbUtil!=null){
            fbUtil.OnDestory();
        }
    }
}
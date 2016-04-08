package org.mobile.htloginsdk.utils;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;
import org.mobile.htloginsdk.bean.User;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

public class FaceBookLoginUtil{
    private  static FaceBookLoginUtil mFaceBookLoginUtil=null;
    private static Activity loginActivity=null;
    private View loginClickView=null;
    private View logOutView=null;
    private  List<String> permissions;
    private  LogInStateListener mBookLoginStateChanged;
    private LogOutStateListener mBookLogOutStateListener;
    private CallbackManager callbackManager;
    private FaceBookCallBackListener callback=new FaceBookCallBackListener();
    private  OnFaceBookLoginClickListener onFaceBookLoginClickListener=new OnFaceBookLoginClickListener();
    private boolean isLogOut=false;
    public static FaceBookLoginUtil getInstance() {
        if(mFaceBookLoginUtil==null){
            mFaceBookLoginUtil=new FaceBookLoginUtil();
        }
        return mFaceBookLoginUtil;
    }
    public  void SetFaceBookLoginActivity(Activity activity){
        if(activity==null){
            throw new  NullPointerException("login activity is null");
        }else{
            loginActivity=activity;
        }
    }

    public  void SetFaceBookLoginButton(View view){
        loginClickView=view;
    }

    public void SetFaceBookLogOutButton(View view){
        logOutView=view;
    }

    public  void SetFaceBookReadPermission(String  array){
        if(array==null){
            permissions=Arrays.asList("public_profile");
        }else{
            permissions=Arrays.asList(array);
        }
    }

    public  void SetOnFaceBookLoginStateListener(LogInStateListener LoginStateChanged){
        if(LoginStateChanged==null){
            throw new NullPointerException("LoginStateListener is null");
        }else{
            mBookLoginStateChanged=LoginStateChanged;
        }
    }

    public void SetOnFaceBookLogOutListener(LogOutStateListener logoutListener){
        mBookLogOutStateListener=logoutListener;
    }

    public void open() {
        FacebookSdk.sdkInitialize(loginActivity);
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(callbackManager, callback);
        if(loginClickView!=null){
            loginClickView.setOnClickListener(onFaceBookLoginClickListener);
        }
        if(logOutView!=null){
            logOutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    isLogOut=true;
                    mBookLogOutStateListener.OnLogOutListener(isLogOut,"facebook");
                }
            });
        }
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    public void OnDestory(){
        //profileTracker.stopTracking();
    }

    private class FaceBookCallBackListener implements FacebookCallback<LoginResult>{
        @Override
        public void onSuccess(LoginResult result) {
            fetchUserInfo(result.getAccessToken());
        }
        @Override
        public void onCancel() {
            mBookLoginStateChanged.OnLoginError("user cancle log in facebook!");
        }
        @Override
        public void onError(FacebookException error) {
            mBookLoginStateChanged.OnLoginError(error.getMessage());
        }

    }
    private  class OnFaceBookLoginClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            LoginManager.getInstance().logInWithReadPermissions(loginActivity,permissions);
        }
    }
    private void fetchUserInfo(AccessToken accessToken){
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            GraphResponse response) {
                        try{
                            if(response.getError()!=null){
                                mBookLoginStateChanged.OnLoginError(response.getError().getErrorMessage());
                            }else if(response.getConnection().getResponseCode()==200){
                                User user=new User();
                                user.setEmail(object.getString("email"));
                                user.setGender(object.getString("gender"));
                                user.setLink(object.getString("link"));
                                user.setFirstname(object.getString("first_name"));
                                user.setLastname(object.getString("last_name"));
                                user.setLocale(object.getString("locale"));
                                user.setTimezone(object.getString("timezone"));
                                user.setUserId(object.getString("id"));
                                user.setUserName(object.getString("name"));
                                mBookLoginStateChanged.OnLoginSuccess(user, "facebook");
                            }
                        }catch(Exception e){
                            mBookLoginStateChanged.OnLoginError(e.getMessage());
                        }
                    }
                });
        request.executeAsync();
    }
}
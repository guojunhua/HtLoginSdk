package org.mobile.htloginsdk.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.model.GameRequestContent;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.GameRequestDialog;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.identifier.AdvertisingIdClient;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;

import org.mobile.htloginsdk.MyApp;
import org.mobile.htloginsdk.R;
import org.xutils.ex.HttpException;

import java.io.IOException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 郭君华 on 2016/1/5.
 * Email：guojunhua3369@163.com
 */
public class Base64Utils {

    //base64解码
    public static byte[] base64Dec(String str) {
        return Base64.decode(str, Base64.DEFAULT);
    }

    /**
     * 转换16进制
     *
     * @param bytes
     * @return
     */
    public static String bytes2hex(byte[] bytes) {
        final String HEX = "0123456789abcdef";
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            // 取出这个字节的高4位，然后与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt((b >> 4) & 0x0f));
            // 取出这个字节的低位，与0x0f与运算，得到一个0-15之间的数据，通过HEX.charAt(0-15)即为16进制数
            sb.append(HEX.charAt(b & 0x0f));
        }
        return sb.toString();
    }

    public static boolean isEmpty(String email, String password) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password))
            return true;
        return false;
    }
    public static boolean isEmpty(String email, String password,String passwordAgain) {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)||TextUtils.isEmpty(passwordAgain))
            return true;
        return false;
    }

    public static boolean isEmail(String email) {
        //^(\w+((-\w+)|(\.\w+))*)\+\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$
        //^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$
        //^([a-z0-9A-Z]+[-|\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\.)+[a-zA-Z]{2,}$
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        return matcher.matches();
    }

    public static boolean isString(String name) {
        String check = "^(?!_)(?!.*?_$)[a-zA-Z0-9_]+$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(name);
        return matcher.matches();
    }

    public static boolean isContains(String username) {
        String s = "@";
        return username.contains(s);
    }

    public static List<String> dataLength(String userInfo) {
        int needLength = 117;
        List<String> resultList = new ArrayList<String>();
        String subStr;
        int endIndex = 0;
        while (userInfo.length() > 0) {
            // 判断截取的长度
            endIndex = userInfo.length() > needLength ? needLength : userInfo.length();
            // 获得街区后的长度
            subStr = userInfo.substring(0, endIndex);
            // 截取剩余的字符串
            userInfo = userInfo.substring(endIndex);
            resultList.add(subStr);
        }
        return resultList;
    }

    public static String backData(String userInfo) {
        ArrayList<String> strings = ((ArrayList<String>) dataLength(userInfo));
        ArrayList<byte[]> bytes = new ArrayList<byte[]>();
        for (int i = 0; i < strings.size(); i++) {
            bytes.add(encryptData(strings.get(i)));
        }
        return bytes2hex(sysCopy(bytes));
    }

    public static byte[] sysCopy(List<byte[]> srcArrays) {
        int len = 0;
        for (byte[] srcArray : srcArrays) {
            len += srcArray.length;
        }
        byte[] destArray = new byte[len];
        int destLen = 0;
        for (byte[] srcArray : srcArrays) {
            System.arraycopy(srcArray, 0, destArray, destLen, srcArray.length);
            destLen += srcArray.length;
        }
        return destArray;
    }

    /**
     * 借助字节输出流ByteArrayOutputStream来实现字节数组的合并
     * <p/>
     * public static byte[] streamCopy(List<byte[]> srcArrays) {
     * byte[] destAray = null;
     * ByteArrayOutputStream bos = new ByteArrayOutputStream();
     * try {
     * for (byte[] srcArray:srcArrays) {
     * bos.write(srcArray);
     * }
     * bos.flush();
     * destAray = bos.toByteArray();
     * } catch (IOException e) {
     * e.printStackTrace();
     * } finally {
     * try {
     * bos.close();
     * } catch (IOException e) {
     * }
     * }
     * return destAray;
     * }
     */
    public static byte[] encryptData(String userInfo) {
        PublicKey publicKey = null;
        byte[] data = null;
        try {
            publicKey = RSAUtils.loadPublicKey(MyApp.publicKeyStr);
            data = RSAUtils.encryptData(userInfo.getBytes(), publicKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
    public static void facebookInvite(Context context,String title,String msg){
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(context);
        CallbackManager callbackManager = CallbackManager.Factory.create();
        GameRequestDialog requestDialog = new GameRequestDialog((Activity) context);
        requestDialog.registerCallback(callbackManager, new FacebookCallback<GameRequestDialog.Result>() {
            public void onSuccess(GameRequestDialog.Result result) {
                String id = result.getRequestId();
                Log.e("---guo---", id);
            }

            public void onCancel() {
            }

            public void onError(FacebookException error) {
            }
        });
        GameRequestContent content = new GameRequestContent.Builder()
                .setTitle(title)
                .setMessage(msg)
                .build();

        requestDialog.show(content);
    }
    /**
     *
     * @param captionStr  标题
     * @param desStr  描述
     * @param linkString  分享链接
     * @param pictureString  图片链接
     */
    public static void doShare(Context context,String captionStr, String desStr, String linkString, String pictureString) {
        ShareDialog shareDialog;
        FacebookSdk.sdkInitialize(context);
        shareDialog = new ShareDialog((Activity) context);
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(captionStr)
                    .setContentDescription(desStr)
                    .setContentUrl(Uri.parse(linkString))
                    .setImageUrl(Uri.parse(pictureString))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    /**
     *
     * @param context 上下文
     * @param type 类型 reg/log
     * @param version 版本号
     * @param coo_server 合作伙伴服务器id
     * @param coo_uid  合作伙伴用户UID
     */
//    public static void bindStatistics(final Context context, String type, String version,String coo_server, String coo_uid) {
//        SharedPreferences sp;
//        sp = new MyApp().getSharedPreferences("login", Context.MODE_APPEND);
//        HttpUtils httpUtils = new HttpUtils();
//        String appId = sp.getString("appId", "");
//        String uid = sp.getString("loginUid", "");
//        String channel = sp.getString("channel","");
//        String uuid = new MacAddress(context).getMacAddressAndroid();
//        String brand = MacAddress.getBrand();
//        AdvertisingIdClient.Info adInfo = null;
//        try {
//            adInfo = AdvertisingIdClient.getAdvertisingIdInfo(context);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesNotAvailableException e) {
//            e.printStackTrace();
//        } catch (GooglePlayServicesRepairableException e) {
//            e.printStackTrace();
//        }
//        String idfa = adInfo.getId();
//        String url = String.format(MyApp.bindUrl, appId, type, version, channel, uid, coo_server, coo_uid,uuid,idfa,brand);
//        httpUtils.send(HttpMethod.GET, url, new RequestCallBack<String>() {
//            @Override
//            public void onSuccess(ResponseInfo<String> responseInfo) {
//                Toast.makeText(context, "成功了", Toast.LENGTH_SHORT).show();
//            }
//            @Override
//            public void onFailure(HttpException error, String msg) {
//                LogUtils.e("VolleyUtils==" + error.getMessage() + msg);
//            }
//        });
//    }
}

package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.mobile.htloginsdk.R;

/**
 * Created by 郭君华 on 2016/4/12.
 * Email：guojunhua3369@163.com
 */
public class WebActivity extends Activity {
    private WebView webView;

    //http://passport.gamehetu.com/license/service
    //http://passport.gamehetu.com/license/privacy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_web);
        webView = ((WebView) findViewById(R.id.webview));
        webView.loadUrl("http://passport.gamehetu.com/license/service");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return super.shouldOverrideUrlLoading(view, url);
            }
        });
    }
}

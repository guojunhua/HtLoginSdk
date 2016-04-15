package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import org.mobile.htloginsdk.R;

/**
 * Created by 郭君华 on 2016/4/12.
 * Email：guojunhua3369@163.com
 */
public class AgreementActivity extends Activity {

    private TextView title;
    private TextView content;
    private Button agreeMent_btn;
    private CheckBox agreement_ch;
    private int type;

    //http://passport.gamehetu.com/license/service
    //http://passport.gamehetu.com/license/privacy
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_agreement);
        title = ((TextView) findViewById(R.id.agreement_title));
        content = ((TextView) findViewById(R.id.agreement_content));
        agreeMent_btn = ((Button) findViewById(R.id.agreement_btn));
        agreement_ch = ((CheckBox) findViewById(R.id.agreement_checkBox));
        agreeMent_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (agreement_ch.isChecked()) {
                    finish();
                } else {
                    Toast.makeText(AgreementActivity.this, R.string.agreement_tip, Toast.LENGTH_SHORT).show();
                }
            }
        });
        type = getIntent().getIntExtra("type", 0);
        if (type == 1) {
            title.setText(getString(R.string.service_provision_name));
            content.setText(getString(R.string.service_provision));
        } else if (type == 2) {
            title.setText(getString(R.string.privacy_clause_name));
            content.setText(getString(R.string.privacy_clause));
        }
    }
}

package org.mobile.htloginsdk.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.mobile.htloginsdk.R;
import org.mobile.htloginsdk.utils.Base64Utils;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public class ForgetPasswordActivity extends Activity implements View.OnClickListener{
    private Button reset;
    private ImageView reset_back;
    private EditText edit_reset;
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_forgetpassword);
        reset = ((Button) findViewById(R.id.reset));
        reset_back = ((ImageView) findViewById(R.id.reset_back));
        edit_reset = ((EditText) findViewById(R.id.reset_edit));
        reset.setOnClickListener(this);
        reset_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.reset){
            email = edit_reset.getText().toString();
            if (!Base64Utils.isEmail(email)){
                Toast.makeText(ForgetPasswordActivity.this, R.string.illegal_uname_tip, Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(ForgetPasswordActivity.this, R.string.forget_password, Toast.LENGTH_SHORT).show();
            }
        }else if (v.getId()==R.id.reset_back){
            finish();
        }
    }
}

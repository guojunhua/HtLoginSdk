package org.mobile.htloginsdk;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by 郭君华 on 2016/4/5.
 * Email：guojunhua3369@163.com
 */
public class ForgetPasswordActivity extends Activity implements View.OnClickListener{
    private Button reset;
    private ImageView reset_back;
    private EditText edit_reset;

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

        }else if (v.getId()==R.id.reset_back){
            finish();
        }
    }
}

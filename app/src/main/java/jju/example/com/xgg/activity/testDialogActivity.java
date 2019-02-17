package jju.example.com.xgg.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import jju.example.com.xgg.R;
import jju.example.com.xgg.widget.LoginErrorDialog;

public class testDialogActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnTest;
    private LoginErrorDialog loginErrorDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_dialog);
        init();
    }

    private void init() {

        btnTest = findViewById(R.id.btn_test);

        btnTest.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_test:
            loginErrorDialog = new LoginErrorDialog(this,R.style.LoginErrorDialog);
            loginErrorDialog.setMsg("验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误验证码错误！");
            loginErrorDialog.show();
                //String str =  Color.red(0xf5f5f5)+"";
                //Log.i("%%%%%%%%%%%%%%%%%%%%%%%", "onClick: "+str);


        }
    }

    public String valueOf(@ColorInt int color) {
        float r = ((color >> 16) & 0xff) ;
        float g = ((color >>  8) & 0xff) ;
        float b = ((color      ) & 0xff) ;
        float a = ((color >> 24) & 0xff);
        return   "r:"+Float.toString(r)+",g:"+Float.toString(g)+",b:"+Float.toString(b);


    }
}

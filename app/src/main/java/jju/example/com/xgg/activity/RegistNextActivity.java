package jju.example.com.xgg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.SPUtil;
import jju.example.com.xgg.widget.LoginErrorDialog;

public class RegistNextActivity extends FullTranStatActivity implements View.OnClickListener {


    private ImageButton ibBack;      //返回键
    private String cust_account;      //用户账号
    private String cust_password;    //用户密码
    private String cust_RePassword;  //再次输入密码
    private EditText etPassword;     //密码输入框
    private EditText etRepassword;   //密码再次输入框
    private Button btnRegist;        //注册
    private LoginErrorDialog errorDialog;  //错误提示框
    private AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_next);

        setStatusBarFullTransparent(true);      //状态栏透明
        setFitSystemWindow(true);               //添加状态栏高度

        Intent intent = getIntent();
        cust_account = intent.getStringExtra("cust_account");  //获取验证码页面传过来的手机号

        init();

    }

    private void init() {
        ibBack = findViewById(R.id.ib_regist_next_back);            //返回键
        etPassword = findViewById(R.id.et_regist_next_password);    //密码
        etRepassword = findViewById(R.id.et_regist_next_repassword);//再次输入密码
        btnRegist = findViewById(R.id.btn_regist_next_regist);      //注册

        btnRegist.setOnClickListener(this);                         //注册点击监听
        ibBack.setOnClickListener(this);                            //返回键点击监听

        etListener();                                               //输入框监听
    }


    /**
     * 点击监听
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.ib_regist_next_back:        //返回按钮监听
                finish();                         //结束当前Activity
                break;

            case R.id.btn_regist_next_regist:      //监听按钮监听
                regist();                          //提交注册
                break;
        }
    }

    /**
     * 注册
     */
    private void regist() {
        cust_password = String.valueOf(etPassword.getText());      //获取密码
        cust_RePassword = String.valueOf(etRepassword.getText());  //获取再次输入密码

        if(!cust_password.equals(cust_RePassword)){       //校验两次输入的密码
            Log.i("RegistNextActivity：", "注册失败: 两次输入密码不一致！");
            errorDialog = new LoginErrorDialog(this,R.style.LoginErrorDialog);
            errorDialog.setMsg("两次输入密码不一致！");
            errorDialog.show();
            return;
        }

        RequestParams params=new RequestParams();     //封装参数
        params.put("cust_account",cust_account);
        //params.put("cust_account","18779323078");    //单独页面测试使用
        params.put("cust_password",cust_password);
        Log.i("RegistNextActivity", "params: "+params.toString());

        httpClient.post(Constant.CUSTOMER_REGIST, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("RegistNextActivity", "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功

                    String jsonString = new String(responseBody);
                    BasePojo<Customer> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<Customer>>(){}.getType());
                    Log.d("RegistNextActivity", "注册成功：\n"+basePojo.toString());

                    Customer customer = new Customer();
                    customer = basePojo.getList().get(0);
                    SPUtil.saveCustomer(RegistNextActivity.this,customer);       //保存用户信息到本地

                    Intent intent = new Intent(RegistNextActivity.this,MainActivity.class);
                    startActivity(intent);     //注册成功跳转到首页

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("RegistNextActivity", "onFailure: 连接服务器失败！");
                Log.i("RegistNextActivity", "statusCode: "+statusCode);
                Log.i("RegistNextActivity", "headers: "+headers);
                Log.i("RegistNextActivity", "responseBody: "+responseBody);
                Log.i("RegistNextActivity", "error: "+error);
            }
        });


    }

    /**
     * 设置注册按钮按钮是否禁用
     */
    private void setBtnRegist() {
        if (!TextUtils.isEmpty(etPassword.getText()) && !TextUtils.isEmpty(etRepassword.getText()) && !btnRegist.isEnabled()) {     //账号与密码同时不为空激活按钮
            btnRegist.setEnabled(true);

        }else if(TextUtils.isEmpty(etPassword.getText()) || TextUtils.isEmpty(etPassword.getText())){
            btnRegist.setEnabled(false);
        }
    }

    /**
     * 监听EditView的内容变化
     */
    private void etListener() {
        /**
         * 密码输入框监听
         */
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnRegist();     //设置注册按钮按钮是否禁用
                Log.i("RegistNextActivity", "onTextChanged: etPssword"+s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        /**
         * 再次密码输入框监听
         */
        etRepassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnRegist();  //设置注册按钮按钮是否禁用
                Log.i("RegistNextActivity", "onTextChanged: etRePssword"+s);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }



}

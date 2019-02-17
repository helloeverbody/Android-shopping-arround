package jju.example.com.xgg.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.SPUtil;
import jju.example.com.xgg.widget.LoginErrorDialog;

public class LoginActivity extends FullTranStatActivity implements View.OnClickListener {

    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final String TAG = "登录页面";
    private ImageButton ibBack;  //返回键
    private EditText etAccount;  //账号输入框
    private EditText etPassword; //密码输入框
    private Button btnLogin;     //登录键
    private TextView tvToRegist; //跳转注册键
    private LoginErrorDialog errorDialog;  //错误提示框
    private AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求

    private String cust_account;       //账号
    private String cust_password;      //密码

    //分割EditText输入的手机号码
    public boolean isBankCard=true;
    private String addString=" ";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setStatusBarFullTransparent(true);
        setFitSystemWindow(true);


        showMessage();          //提示“请先登录”

        init();
        etListener();
    }

    /**
     * 提示“请先登录”
     */
    private void showMessage() {
        Log.i(TAG, "showMessage: 显示登录信息");

        Intent intent = getIntent();
        Boolean isShow = intent.getBooleanExtra("isShow",false);
        if (isShow){
            Toast.makeText(this,"请您先登录！",Toast.LENGTH_SHORT).show();
        }


    }


    private void init() {
        ibBack = findViewById(R.id.ib_login_back);
        etAccount = findViewById(R.id.et_login_account);
        etPassword = findViewById(R.id.et_login_password);
        btnLogin = findViewById(R.id.btn_login_login);
        tvToRegist = findViewById(R.id.tv_login_regist);


        btnLogin.setOnClickListener(this);
        tvToRegist.setOnClickListener(this);
        ibBack.setOnClickListener(this);


    }

    /**
     * 监听EditView的内容变化
     */
    private void etListener() {

        etAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                setBtnLogin();

                if (isBankCard) {
                    String finalString="";
                    int index=0;
                    String telString=s.toString().replace(" ", "");
                    if ((index+3)<telString.length()) {
                        finalString+=(telString.substring(index, index+3)+addString);
                        index+=3;
                    }
                    while ((index+4)<telString.length()) {
                        finalString+=(telString.substring(index, index+4)+addString);
                        index+=4;
                    }
                    finalString+=telString.substring(index,telString.length());

                    etAccount.removeTextChangedListener(this);  //先移除监听，不然会导致堆栈溢出
                    etAccount.setText(finalString);
                    //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                    etAccount.setSelection(finalString.length());
                    etAccount.addTextChangedListener(this);


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setBtnLogin();



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 设置登录按钮是否禁用
     */
    public void setBtnLogin(){

        if (!TextUtils.isEmpty(etPassword.getText()) && !TextUtils.isEmpty(etAccount.getText()) && !btnLogin.isEnabled()) {     //账号与密码同时不为空激活按钮
            btnLogin.setEnabled(true);

        }else if(TextUtils.isEmpty(etPassword.getText()) || TextUtils.isEmpty(etAccount.getText())){
            btnLogin.setEnabled(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_login:      //按下登录按钮
                login();
                break;
            case R.id.tv_login_regist:
                toRegist();
                break;
            case R.id.ib_login_back:
                setResult(RESULT_FAIL);
                finish();                   //结束当前activity
                break;
        }
    }

    /**
     * 跳转到注册
     */
    private void toRegist() {

        Intent intent = new Intent(this,RegistActivity.class);
        startActivity(intent);
    }

    /**
     * 登录操作
     */
    private void login() {
        cust_account = etAccount.getText().toString().replace(" ","");   //获取账号
        cust_password = etPassword.getText().toString(); //获取密码

        RequestParams params=new RequestParams();     //封装参数
        params.put("cust_account",cust_account);
        params.put("cust_password",cust_password);
        Log.i("LoginActivity", "params: "+params.toString());

        httpClient.post(Constant.CUSTOMER_LOGIN, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("LoginActivity", "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<Customer> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<Customer>>(){}.getType());

                    if(basePojo.isSuccess()){

                        Customer customer = new Customer();
                        customer = basePojo.getList().get(0);
                        SPUtil.saveCustomer(LoginActivity.this,customer);       //保存用户信息到本地
                        Log.d("LoginActivity", "登录成功：\n"+basePojo.toString());
                        //Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        //startActivity(intent);     //注册成功跳转到首页
                        Intent intent = getIntent();
                        setResult(RESULT_SUCCESS,intent);    // 跳转回Mine
                        finish();
                    }else{
                        Log.i("LoginActivity：", "登录失败: "+basePojo.getMsg());
                        errorDialog = new LoginErrorDialog(LoginActivity.this,R.style.LoginErrorDialog);
                        errorDialog.setMsg(basePojo.getMsg());
                        errorDialog.show();
                    }

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("LoginActivity", "onFailure: 连接服务器失败！");
            }
        });

        //Toast.makeText(this,"登录",Toast.LENGTH_SHORT).show();
    }
}

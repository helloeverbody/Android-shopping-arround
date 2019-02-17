package jju.example.com.xgg.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.smssdk.SMSSDK;
import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.Order;
import jju.example.com.xgg.pojo.OrderDetail;
import jju.example.com.xgg.util.OrderInfoUtil2_0;
import jju.example.com.xgg.util.OrderUtil;

public class CheckoutCountActivity extends FullTranStatActivity implements View.OnClickListener {
    private static final String TAG = "FullTranStatActivity";
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private LinearLayout llPay;         //支付按钮
    private TextView tvOrderSubject;      //订单标题
    private TextView tvOrderAmount;       //订单初始金额
    private TextView tvOrderFinalAmount;  //订单最终金额
    private ImageView ivBack;             //返回键

    private String orderReturn = null;   // 订单信息
    private Order order = new Order();   //订单信息
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    private static final int SHOP_DETAIL_REQUEST_CODE = 1;

    private boolean payFlag = false;  //支付结果标记


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {    //支付结果回调

            /*Toast.makeText(CheckoutCountActivity.this, msg.obj.toString(),
                    Toast.LENGTH_LONG).show();*/

            Log.i(TAG, "**************支付结果回调******************");
            Log.i("支付结果回调:", msg.obj.toString());
            Log.i(TAG, "*******************************************");

            Intent intent = new Intent();
            Map<String,String> result = new HashMap<String,String>();
            result = (Map<String, String>) msg.obj;
            Log.i(TAG, "**************status******************");
            Log.i("支付结果回调:", result.get("resultStatus"));
            Log.i(TAG, "*******************************************");
            if(result.get("resultStatus").equals("9000")){
                payFlag = true;
                Log.i(TAG, "**************status******************");
                Log.i("payFlag:", Boolean.toString(payFlag));
                Log.i(TAG, "*******************************************");
                intent.putExtra("memo",result.get("memo"));
                intent.putExtra("success",payFlag);

            }else{
                payFlag = false;
                Log.i(TAG, "**************status******************");
                Log.i("payFlag:", Boolean.toString(payFlag));
                Log.i(TAG, "*******************************************");
                intent.putExtra("memo",result.get("memo"));
                intent.putExtra("success",payFlag);
            }
            setResult(RESULT_SUCCESS,intent);
            finish();
        };
    };


    Runnable payRunnable = new Runnable() {       //调用支付宝支付接口

        @Override
        public void run() {
            PayTask alipay = new PayTask(CheckoutCountActivity.this);
            Map <String, String> result = alipay.payV2(orderReturn,true);
            Log.i("result", result.toString());

            Message msg = new Message();
            msg.what = SDK_PAY_FLAG;
            msg.obj = result;
            mHandler.sendMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout_count);
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);  //调用支付宝沙箱支付
        setStatusBarFullTransparent(true);
        setFitSystemWindow(true);
        loadData();   //获取数据
        init();

    }
    /**
     * 生成订单
     */
    private void createOrder() {
/*
        order.setOrde_name("一号店");
        order.setOrde_number(out_trade_no);
        order.setOrde_amount(50);
        List<OrderDetail> orderDetailList = new ArrayList<>();

        for(int i=0; i<2; i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrdeta_prod_name("香蕉");
            orderDetailList.add(orderDetail);
        }

        order.setDetails(orderDetailList);*/
        Intent intent = getIntent();
        String orderIntent = intent.getStringExtra("orderIntent");
        Log.i(TAG, "**************获取的订单字符串**********************");
        Log.i("orderIntent:", orderIntent);
        Log.i(TAG, "*******************************************");
        order = new Gson().fromJson(orderIntent,Order.class);
    }

    /**
     * 获取数据
     */
    private void loadData() {
        createOrder();  //生成订单
    }

    private void init() {
        llPay = findViewById(R.id.check_count_tv_order_sure_pay);                      //支付按钮
        tvOrderAmount = findViewById(R.id.check_count_tv_order_amount);                //支付初始金额
        tvOrderFinalAmount = findViewById(R.id.check_count_tv_order_final_amount);     //支付最终金额
        tvOrderSubject= findViewById(R.id.check_count_tv_order_subject);               //订单标题
        ivBack= findViewById(R.id.check_count_iv_back);                                //返回键
        tvOrderSubject.setText(order.getOrde_name());
        tvOrderFinalAmount.setText(Double.toString(order.getOrde_amount()));
        tvOrderAmount.setText(Double.toString(order.getOrde_amount()));

        ivBack.setOnClickListener(this);
        llPay.setOnClickListener(this);

    }

    /**
     * 页面点击监听
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.check_count_tv_order_sure_pay:
                getOrderInfo();   //确认支付
                break;
            case R.id.check_count_iv_back:
                Intent intent = new Intent();
                intent.putExtra("success",payFlag);
                intent.putExtra("memo","取消支付");
                setResult(RESULT_SUCCESS,intent);
                finish();
                break;
        }
    }

    //获取服务生成的支付宝订单信息
    public void getOrderInfo(){

        String out_trade_no = OrderInfoUtil2_0.getOutTradeNo();
        Log.i("out_trade_no:", out_trade_no);
        Map<String,String> modelMap = new HashMap<>();

        String body = order.getDetails().get(0).getOrdeta_prod_name();

        if(order.getDetails().size()>1){ //如果该订单不只一种商品添加“等”来显示
            body += "等";
        }

        modelMap.put("body",body);
        modelMap.put("subject",order.getOrde_name());
        modelMap.put("out_trade_no",order.getOrde_number());
        modelMap.put("total_amount",Double.toString(order.getOrde_amount()));
        modelMap.put("product_code","QUICK_MSECURITY_PAY");

        //String orderInfo = OrderInfoUtil2_0.buildOrderParam(modelMap);
        String orderInfo = new Gson().toJson(modelMap);
        Log.i(TAG, "**************订单信息**********************");
        Log.i("orderInfo:", orderInfo);
        Log.i(TAG, "*******************************************");
        RequestParams params=new RequestParams();     //封装参数

        params.put("orderInfo",orderInfo);

        Log.i("RegistNextActivity", "params: "+params.toString());
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        httpClient.post(Constant.ALIPAY_CREATE_ORDER, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i("order", "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    orderReturn = new String(responseBody);
                    Log.d("order", "获取订单成功：\n"+orderReturn);
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
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

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("success",payFlag);
        intent.putExtra("memo","取消支付");
        setResult(RESULT_SUCCESS,intent);
        finish();
    }


}

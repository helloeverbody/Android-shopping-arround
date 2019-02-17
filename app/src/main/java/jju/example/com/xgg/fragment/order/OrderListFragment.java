package jju.example.com.xgg.fragment.order;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.CheckoutCountActivity;
import jju.example.com.xgg.activity.ShopDetailActivity;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.fragment.BaseFragment;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.pojo.Order;
import jju.example.com.xgg.pojo.OrderDetail;
import jju.example.com.xgg.util.DateUtil;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.OrderInfoUtil2_0;
import jju.example.com.xgg.util.OrderUtil;
import jju.example.com.xgg.util.SPUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderListFragment extends BaseFragment {

    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final int ORDERLIST_REQUEST_CODE = 2;
    private static final String TAG = "订单页面";
    private RecyclerView rvOrder;       //订单数据列
    private OrderAdapter orderAdapter;  //订单适配器
    private List<Order> orderList = new ArrayList <>(); //订单数据集合
    private Order order = new Order();                  //订单
    private Customer customer = new Customer();         //用户信息
    public OrderListFragment() {
        // Required empty public constructor
    }
    private int orde_status=0;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        loadData();
        initView(view);     //初始化视图
        initRecyclerView(); //初始化订单数据列
        return view;
    }

    /**
     * 加载数据
     */
    private void loadData() {
        if (SPUtil.isLogin(getContext())){
            customer = SPUtil.getCustomer(getContext());
            loadOrderData();        //加载订单数据
        }
    }

    /**
     * 初始化视图
     * @param view
     */
    private void initView(View view) {
        rvOrder = view.findViewById(R.id.order_list_rv_order);  //订单数据列
    }

    /**
     * 初始化recycleView
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        orderAdapter = new OrderAdapter(R.layout.layout_item_order, orderList); //初始化订单适配器
        rvOrder.setLayoutManager(layoutManager);
        rvOrder.setAdapter(orderAdapter);

        orderAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener(){
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
                    case R.id.lio_btn_again:
                        Log.i(TAG, "onItemChildClick: 点击再来一单");
                        balance(position);      //结算
                        break;
                    case R.id.lio_btn_comment:
                        Log.i(TAG, "onItemChildClick: 点击评价");
                        break;
                }
            }
        });


    }

    /**
     * 订单适配器快速构造
     */
    public class OrderAdapter extends BaseQuickAdapter<Order,BaseViewHolder>{

        public OrderAdapter(int layoutResId, @Nullable List<Order> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, Order item) {
            Button btnCommit = helper.getView(R.id.lio_btn_comment);
            RequestOptions options = new RequestOptions()
                    //.centerCrop()
                    .placeholder(R.mipmap.img_shop);
            DecimalFormat df = new DecimalFormat( "0.00");  //显示两位小数

            Glide.with(getContext())
                    .load(item.getOrde_head())
                    .apply(options)
                    .into((ImageView) helper.getView(R.id.lio_iv_shop_head));      //加载订单店铺图片
            int productCount = 0;
            for (int i = 0; i < item.getDetails().size(); i++) {
                productCount += item.getDetails().get(i).getOrdeta_prod_count();
            }
            String status = null;
            switch (item.getOrde_status()){
                case "0":
                    status = "订单待确认";
                    btnCommit.setVisibility(View.INVISIBLE);
                    break;
                case "1":
                    status = "订单已确认";
                    break;
                case "2":
                    status = "订单已完成";
                    break;
            }
            String prodName=null;
            if (item.getDetails().size()>1){
                prodName = item.getDetails().get(0).getOrdeta_prod_name()+"等";
            }else{
                prodName = item.getDetails().get(0).getOrdeta_prod_name();
            }
            Log.i(TAG, "convert: "+item.getOrde_name());
            helper .setText(R.id.lio_tv_shop_name,item.getOrde_name())
                    .setText(R.id.lio_tv_product_name,prodName)
                    .setText(R.id.lio_tv_product_count,Integer.toString(productCount))
                    .setText(R.id.lio_tv_order_price,df.format(item.getOrde_amount()))
                    .setText(R.id.lio_tv_order_status,status)
                    .addOnClickListener(R.id.lio_btn_again)
                    .addOnClickListener(R.id.lio_btn_comment);
        }
    }

    public void loadOrderData(){
        String status = "";
        Bundle bundle = getArguments();
        Log.i(TAG, "loadData: "+bundle.getString("title"));
        String title = bundle.getString("title",null);
        if(title != null && title.equals("待评价") ){
            status = " and orde_status=2 ";
        }
        String condition = " and orde_cust_id ="+customer.getCust_id()+" and orde_id = ordeta_orde_id";        //查询条件

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.ORDER_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<Order> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<Order>>(){}.getType());

                    if(basePojo.isSuccess()){
                        orderList = new ArrayList <>();
                        orderList = basePojo.getList();
                        initRecyclerView();
                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                    }else{
                        Log.i(TAG, "获取失败: "+basePojo.getMsg());
                    }

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: 连接服务器失败！");
            }
        });
    }

    /**
     * 结算购物车
     */
    private void balance(int position) {

        order = orderList.get(position);      //生成订单
        order.setOrde_number(OrderInfoUtil2_0.getOutTradeNo());  //订单号
        order.setOrde_create_time(DateUtil.getDateTime());       //订单生成时间
        //支付精灵支付
        /*String suject = order.getOrde_name();
        String body = null;
        if (order.getDetails().size()>1){
            body = order.getDetails().get(0).getOrdeta_prod_name()+"等";

        }else{
            body = order.getDetails().get(0).getOrdeta_prod_name();
        }
        String orderId = order.getOrde_number();
        String payUserId = Integer.toString(order.getOrde_cust_id());
        pay(suject,body,orderId,payUserId);*/
        
        pay();   //支付宝支付

    }

    /**
     * 支付宝支付
     */
    private void pay() {
        String orderIntent = new Gson().toJson(order);
        Log.i(TAG, "**************订单json数据******************");
        Log.i("订单json数据:", orderIntent);
        Log.i(TAG, "*******************************************");

        Intent intent = new Intent(getContext(),CheckoutCountActivity.class);
        intent.putExtra("orderIntent",orderIntent);
        startActivityForResult(intent,ORDERLIST_REQUEST_CODE);
        
    }


   /* *//**
     * 支付 支付精灵
     *//*
    private void pay(String suject,String body,String orderId,String payUserId) {

        *//**
         * 发起支付
         *
         * @param subject       商品名称,不可为空和空字符串
         * @param body          商品内容,不可为空和空字符串
         * @param amount        支付金额，单位分，不能为null和<1
         * @param orderId       商户系统的订单号(如果有订单的概念),没有可为空
         * @param payUserId     商户系统的用户id(如果有用户的概念),没有可为空
         * @param backPara      支付成功后支付精灵会用此参数回调配置的url
         *					(回调url在后台应用->添加应用时候配置)
         * demo: backPara 的value(建议json) ： {"a":1,"b":"2"},如不需要可为空。
         * @param payResultListener，不能为null 支付结果回调
         *//**//*
        EPay.getInstance(getContext()).pay(suject, body, 1,
                orderId, payUserId,null, new PayResultListener() {
                    *//**//**
                     * @param context
                     * @param payId   支付精灵支付id
                     * @param orderId   商户系统订单id
                     * @param payUserId 商户系统用户ID
                     * @param payResult
                     * @param payType   支付类型:1 支付宝，2 微信 3 银联
                     * @param amount    支付金额
                     * @see EPayResult#FAIL_CODE
                     * @see EPayResult#SUCCESS_CODE
                     * 1支付成功，2支付失败
                     *//**//*
                    @Override
                    public void onFinish(Context context, Long payId, String orderId, String payUserId,
                                         EPayResult payResult , int payType, Integer amount) {
                        EPay.getInstance(context).closePayView();//关闭快捷支付页面
                        if(payResult.getCode() == EPayResult.SUCCESS_CODE.getCode()){
                            order.setOrde_status("0");
                            order.setOrde_pay(Integer.toString(payType));
                            addOrderData(order);//增加订单

                            //支付成功逻辑处理
                            Toast.makeText(getContext(), payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }else if(payResult.getCode() == EPayResult.FAIL_CODE.getCode()){
                            //支付失败逻辑处理
                            Toast.makeText(getContext(), payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                });*//*
    }*/
    

    /**
     * 增加订单数据
     */
    public void addOrderData(Order order){

        String paras = new Gson().toJson(order);

        Log.i(TAG, "增加订单: condition:"+paras);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("paras",paras);

        httpClient.post(Constant.ORDER_ADD, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<Order> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<Order>>(){}.getType());

                    if(basePojo.isSuccess()){

                        Log.i(TAG, "onSuccess: "+basePojo.getMsg());


                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                    }else{
                        Log.i(TAG, "获取失败: "+basePojo.getMsg());
                    }

                }


            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: 连接服务器失败！");
                Log.i(TAG, "onFailure: statusCode:"+statusCode);
                Log.i(TAG, "onFailure: headers:"+headers);
                Log.i(TAG, "onFailure: responseBody:"+responseBody);
                Log.i(TAG, "onFailure: error："+error);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_SUCCESS && requestCode == ORDERLIST_REQUEST_CODE){
            if(data.getBooleanExtra("success",false)){
                String memo = data.getStringExtra("memo");
                memo =  memo == null|| memo.equals("") ? "支付成功" : memo;
                Toast.makeText(getContext(),memo,Toast.LENGTH_SHORT).show();
                addOrderData(order);
            }else{
                String memo = data.getStringExtra("memo");
                memo =  memo == null|| memo.equals("") ? "取消支付" : memo;

                Toast.makeText(getContext(),memo,Toast.LENGTH_SHORT).show();
            }
        }


    }
}

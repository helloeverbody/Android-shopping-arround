package jju.example.com.xgg.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ItemShopClassifyAdapter;
import jju.example.com.xgg.adapter.ItemShopProductAdapter;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.config.Payelves;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.pojo.Order;
import jju.example.com.xgg.pojo.OrderDetail;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.pojo.ProductClassify;
import jju.example.com.xgg.pojo.Shop;
import jju.example.com.xgg.pojo.ShoppingCart;
import jju.example.com.xgg.util.DateUtil;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.OrderInfoUtil2_0;
import jju.example.com.xgg.util.OrderUtil;
import jju.example.com.xgg.util.SPUtil;
import q.rorbin.badgeview.QBadgeView;

public class ShopDetailActivity extends FullTranStatActivity implements View.OnClickListener {

    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final int SHOP_DETAIL_RESULT_CODE = 1;
    private static final int SHOP_DETAIL_REQUEST_CODE = 1;
    private String TAG = "店铺详情页面";
    private TextView tvShopName;                //店铺名
    private TextView tvShopIntrodution;     //店铺公告
    private ImageView ivShopImg;        //店铺图片
    private RecyclerView rvLeft;        //分类
    private RecyclerView rvRight;       //商品
    private ImageView ivBack;           //返回键
    private Button btnBalacce;          //结算按钮
    private ItemShopClassifyAdapter itemShopClassifyAdapter;    //分类适配器
    private ItemShopProductAdapter itemShopProductAdapter;      //商品适配器

    private List<ProductClassify> productclassifyList = new ArrayList <>();    //商品分类集合
    private List<Product> productList = new ArrayList <>();    //商品集合
    private int currentProductPosition;     //点击增加商品时的position

    private int currentClassify;            //当前所在分类
    private int currentClassifyId;          //当前所在分类id
    private int selectedProdId;             //当前分类第一个商品的id
    private int selectedClassify;           //被选中的分类行

    private ImageView ivCart;       //购物车图标
    private RecyclerView rvCart;    //购物车列表
    private LinearLayout llCart;    //购物车所在布局
    List<ShoppingCart> cartList = new ArrayList <>(); //购物车集合
    private BottomSheetLayout bslCart;   //购物车底部弹出
    private View bottomSheet;            //购物车弹出视图
    private ViewGroup anim_mask_layout;  //加入购物车动画的根布局

    private TextView tvCartAmount;      //购物车总额
    private double cartAmount = 0;      //购物车总额
    private DecimalFormat df = new DecimalFormat("0.00");    //格式化数额小数点
    private int cartCount = 0;          //购物车数目
    QBadgeView qBadgeView ;             //购物车角标
    private CartAdapter cartAdapter;    //购物车适配器

    private RefreshLayout refreshLayout;        //刷新，加载框架
    private Handler mHanlder;

    private Product product = new Product();      //商品信息
    private Customer customer = new Customer();   //用户
    private Order order1 = new Order();                          //订单




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_detail);

        setStatusBarFullTransparent(false);      //状态栏透明


        /*模拟数据开始*/
        //Shop shop1 = new Shop();
        //shop1.setShop_id(1);
        //SPUtil.saveShop(this,shop1);
        //Customer customer1 = new Customer();
        //customer1.setCust_id(192);
        //SPUtil.saveCustomer(this,customer1);
        /*模拟数据结束*/
        initView();
        loadData();     //加载数据


//        for (int i=0; i<20; i++){
//            Product product = new Product();
//            product.setProd_name("苹果"+i);
//            product.setProd_price(30d);
//            productList.add(product);
//        }

    }

    /**
     * 加载数据
     */
    private void loadData() {
        product = SPUtil.getProduct(this);            //获取店铺数据
        customer = SPUtil.getCustomer(this);    //获取用户数据
        tvShopName.setText(product.getShop_name());
        tvShopIntrodution.setText("公告："+product.getShop_notice());
        if(product.getShop_head()!=null){
            Glide.with(this).load(product.getShop_head());
        }else {
            Glide.with(this).load(R.mipmap.img_shop);
        }
        currentClassify = 0;
        Intent intent = getIntent();
        currentClassifyId = intent.getIntExtra("classifyId",-1);
        selectedProdId = intent.getIntExtra("productId",-1);
        loadClassifyData();                         //加载店铺内商品分类数据
        loadCartData();                             //加载购物车数据

    }


    private void initView() {
        tvShopName = findViewById(R.id.tv_shop_name);   //店铺名
        tvShopIntrodution = findViewById(R.id.tv_shop_introdution);     //店铺公告
        ivShopImg = findViewById(R.id.shop_detail_iv_shop_head);        //店铺图片
        rvLeft = findViewById(R.id.shop_detail_rv_classify);
        rvRight = findViewById(R.id.shop_detail_rv_products);
        ivCart = findViewById(R.id.shop_detail_iv_cart); //购物车
        tvCartAmount = findViewById(R.id.shop_detail_cart_amount);  //购物车总额
        bslCart = findViewById(R.id.shop_detail_bsl_cart); //购物车底部弹出
        anim_mask_layout = (RelativeLayout)findViewById(R.id.shop_detail_containerLayout);
        llCart = findViewById(R.id.shop_detail_ll_cart);
        refreshLayout = (RefreshLayout)findViewById(R.id.refreshLayout); //刷新、加载
        ivBack = findViewById(R.id.search_iv_back);         //返回键
        mHanlder = new Handler(getMainLooper());
        btnBalacce = findViewById(R.id.shop_detail_btn_balance);        //结算按钮
        /*//支付服务初始化
        EPay.getInstance(getApplicationContext()).init( Payelves.OPEN_ID,  Payelves.TOKEN,
                Payelves.APPKEY, Payelves.CHANNEL);*/
        qBadgeView = new QBadgeView(ShopDetailActivity.this);
        cartAdapter = new CartAdapter(R.layout.item_shop_detail_cart,cartList); //购物车适配器 //初始化购物车适配器
        setEnabledRefresh();



        initHeadFoot();     //自定义刷新、加载提示语
        llCart.setOnClickListener(this);    //购物车监听
        ivBack.setOnClickListener(this);    //返回键监听
        btnBalacce.setOnClickListener(this);//结算按钮监听

        //initLeftRecyclerView();     //初始化分类recylclerView
        //initRightRecyclerView();    //初始化商品recylclerView



        //刷新、加载监听
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {



            @Override
            public void onRefresh(RefreshLayout refreshlayout) {


                if(currentClassify>0){
                    currentClassify = currentClassify -1;
                    rvLeft.smoothScrollToPosition(currentClassify);
                    List<Boolean> flagList = new ArrayList <>();
                    for (int i=0; i<productclassifyList.size(); i++){
                        if(i == currentClassify){
                            flagList.add(true);
                        }else {
                            flagList.add(false);
                        }
                    }

                    itemShopClassifyAdapter.setFlag(flagList);
                    itemShopClassifyAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onRefresh: 已执行刷新"+currentClassify);

                }

                productList.clear();     //清除productList
                loadProductData();       //重新加载所属分类的商品信息

            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {


            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {


                if(currentClassify<productclassifyList.size()){
                    currentClassify = currentClassify +1;
                    rvLeft.smoothScrollToPosition(currentClassify);
                    List<Boolean> flagList = new ArrayList <>();
                    for (int i=0; i<productclassifyList.size(); i++){
                        if(i == currentClassify){
                            flagList.add(true);
                        }else {
                            flagList.add(false);
                        }
                    }

                    itemShopClassifyAdapter.setFlag(flagList);
                    itemShopClassifyAdapter.notifyDataSetChanged();
                    Log.i(TAG, "onRefresh: 已执行刷新"+currentClassify);

                }

                productList.clear();     //清除productList
                loadProductData();       //重新加载所属分类的商品信息

            }
        });
    }



    /**
     * 设置商品类表是否可上拉或下拉
     */
    private void setEnabledRefresh() {
        if(currentClassify <=0){
            refreshLayout.setEnableRefresh(false);
            refreshLayout.setEnableLoadMore(true);
        }
        if(currentClassify>=productclassifyList.size()-1){
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setEnableLoadMore(false);
        }
        if(currentClassify>0 && currentClassify<productclassifyList.size()-1){
            refreshLayout.setEnableRefresh(true);
            refreshLayout.setEnableLoadMore(true);
        }
    }

    /**
     * 自定义刷新加载提示语
     */
    private void initHeadFoot() {
        /*刷新提示语*/
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉逛上一类";//"下拉可以刷新";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放逛上一类";//"释放立即刷新";
        /*加载提示语*/
        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉逛下一类";//"上拉加载更多";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放逛下一类";//"释放立即加载";
    }

    /**
     * 初始化分类适配器
     */
    private void initLeftRecyclerView() {

        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);

        rvLeft.setLayoutManager(layoutManager);

        itemShopClassifyAdapter = new ItemShopClassifyAdapter(this,productclassifyList);
        List<Boolean> flagList = new ArrayList <>();
        for (int i=0; i<productclassifyList.size(); i++){
            if(i == currentClassify){
                flagList.add(true);
            }else {
                flagList.add(false);
            }
        }
        itemShopClassifyAdapter.setFlag(flagList);
        rvLeft.setAdapter(itemShopClassifyAdapter);




        //分类点击监听
        itemShopClassifyAdapter.setmOnItemClickListener(new ItemShopClassifyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {


                currentClassify = position;
                setEnabledRefresh();
                List<Boolean> booleanList = new ArrayList <>();
                for(int i=0; i<productclassifyList.size(); i++){
                    if(i == position){
                        booleanList.add(true);
                    }else{
                        booleanList.add(false);
                    }
                }
                productList.clear();
                loadClassifyData();     //更新商品列表数据
                itemShopClassifyAdapter.setFlag(booleanList);
                itemShopClassifyAdapter.notifyDataSetChanged();
                itemShopProductAdapter.notifyDataSetChanged();



            }

            @Override
            public void onLongClick(int position) {

            }
        });



    }

    /*商品列表*/
    private void initRightRecyclerView() {

        GridLayoutManager layoutManager =  new GridLayoutManager(this,2);

        rvRight.setLayoutManager(layoutManager);

        itemShopProductAdapter = new ItemShopProductAdapter(this,productList);

        rvRight.setAdapter(itemShopProductAdapter);

        /**
         * 点击增加商品
         */
        itemShopProductAdapter.setmOnItemClickListener(new ItemShopProductAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                currentProductPosition = position;   //传递position

            }

            @Override
            public void onLongClick(int position) {

            }
        });




    }


    /**
     * 购物车弹出视图初始化
     * @return
     */
    private View createBottomSheetView(){
        View view = LayoutInflater.from(this).inflate(R.layout.bottom_sheet_cart,(ViewGroup) getWindow().getDecorView(),false);
        TextView clear = (TextView) view.findViewById(R.id.bsc_tv_clear);
        rvCart = view.findViewById(R.id.bsc_rv_cart);
        clear.setOnClickListener(this);

        Log.i(TAG, "createBottomSheetView: "+cartList.toString());
        cartAdapter.bindToRecyclerView(rvCart);
        cartAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {   //购物车内部点击监听
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                TextView tvProdCount = (TextView) adapter.getViewByPosition(position,R.id.isdc_tv_product_count);
                TextView tvProdPrice = (TextView) adapter.getViewByPosition(position,R.id.isdc_tv_product_price);
                int prodCount = Integer.parseInt(tvProdCount.getText().toString());
                double prodPrice = Double.parseDouble(tvProdPrice.getText().toString());
                switch (view.getId()){
                    case R.id.isdc_iv_out_cart:
                        Log.i(TAG, "onItemClick: 点击减少购物车"+position);


                        cartAmount -= cartList.get(position).getCart_prod_price();      //刷新购物车总额
                        tvCartAmount.setText(df.format(cartAmount));                    //刷新购物车总额
                        cartCount--;                //购物车角标显示数字
                        qBadgeView.bindTarget(ivCart).setBadgeNumber(cartCount);//更新购物车角标显示数字

                        if(tvProdCount.getText().toString().equals("1")){
                            int cart_id = cartList.get(position).getCart_id() ;
                            deleteCartData(cart_id);        //在数据库中删除该购物车记录
                            adapter.remove(position);
                        }else{
                            prodPrice -= cartList.get(position).getCart_prod_price();  //购物车单种商品总额
                            tvProdPrice.setText(df.format(prodPrice));                  //购物车单种商品总额

                            prodCount = prodCount - 1;
                            tvProdCount.setText(Integer.toString(prodCount));
                            cartList.get(position).setCart_prod_count(prodCount);
                            updateCartData(cartList.get(position));     //更新数据库
                        }




                        if (cartCount==0){
                            bslCart.dismissSheet();     //隐藏购物车弹窗
                        }
                        break;
                    case R.id.isdc_iv_add_cart:
                        Log.i(TAG, "onItemClick: 点击增加购物车"+position);


                            prodCount = prodCount + 1;
                            tvProdCount.setText(Integer.toString(prodCount));
                            cartList.get(position).setCart_prod_count(prodCount);
                            updateCartData(cartList.get(position));     //更新数据库
                            cartCount++;                //购物车角标显示数字
                            qBadgeView.bindTarget(ivCart).setBadgeNumber(cartCount);//更新购物车角标显示数字

                            prodPrice += cartList.get(position).getCart_prod_price();  //购物车单种商品总额
                            tvProdPrice.setText(df.format(prodPrice));                  //购物车单种商品总额
                            cartAmount += cartList.get(position).getCart_prod_price();      //刷新购物车总额
                            tvCartAmount.setText(df.format(cartAmount));                    //刷新购物车总额
                        break;

                }
            }


        });



        LinearLayoutManager lmCart =  new LinearLayoutManager(ShopDetailActivity.this);
        rvCart.setLayoutManager(lmCart);
        rvCart.setAdapter(cartAdapter);



        return view;
    }

    /**
     * 显示购物车
     */
    private void showBottomSheet(){
        if(bottomSheet==null){
            bottomSheet = createBottomSheetView();
        }
        if(bslCart.isSheetShowing()){
            bslCart.dismissSheet();
        }else {
            if(cartList.size()!=0){

                bslCart.showWithSheetView(bottomSheet);

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bsc_tv_clear:     //清空购物车
                clearCartData();        //清空购物车数据
                break;
            case R.id.shop_detail_ll_cart: //点击购物车
                showBottomSheet();
                Log.i(TAG, "onClick: 点击购物车");
                break;
            case R.id.search_iv_back:       //点击返回键
                setResult(RESULT_FAIL);
                finish();                   //返回上个页面
                break;
            case R.id.shop_detail_btn_balance: //结算按钮
                balance();
                break;
        }
    }

    /**
     * 结算购物车
     */
    private void balance() {
        createOrder();      //生成订单
        String suject = order1.getOrde_name();
        String body = order1.getDetails().get(0).getOrdeta_prod_name()+"等";
        String orderId = order1.getOrde_number();
        String payUserId = Integer.toString(order1.getOrde_cust_id());
        /*pay(suject,body,orderId,payUserId);*/  //支付精灵支付
        pay();  //支付宝支付

    }

    /**
     * 支付宝支付
     */
    private void pay() {
        String orderIntent = new Gson().toJson(order1);
        Log.i(TAG, "**************订单json数据******************");
        Log.i("订单json数据:", orderIntent);
        Log.i(TAG, "*******************************************");

        Intent intent = new Intent(this,CheckoutCountActivity.class);
        intent.putExtra("orderIntent",orderIntent);
        startActivityForResult(intent,SHOP_DETAIL_REQUEST_CODE);
    }

    /**
     * 生成订单
     */
    private void createOrder() {
        order1.setOrde_name(product.getShop_name());
        order1.setOrde_head(product.getShop_head());
        order1.setOrde_shop_id(product.getShop_id());
        order1.setOrde_cust_id(customer.getCust_id());
        order1.setOrde_create_time(DateUtil.getDateTime());
        order1.setOrde_status("0");
        order1.setOrde_pay("未知");
        order1.setOrde_pay_status("0");
        order1.setOrderBy(customer.getCust_name());
        String out_trade_no = OrderInfoUtil2_0.getOutTradeNo();
        Log.i("out_trade_no:", out_trade_no);
        order1.setOrde_number(out_trade_no);
        order1.setOrde_amount(Double.parseDouble(tvCartAmount.getText().toString()));
        List<OrderDetail> orderDetailList = new ArrayList <>();
        for(int i=0; i<cartList.size(); i++){
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setOrdeta_prod_name(cartList.get(i).getCart_prod_name());
            orderDetail.setOrdeta_prod_id(cartList.get(i).getCart_prod_id());
            orderDetail.setOrdeta_prod_price(cartList.get(i).getCart_prod_price());
            orderDetail.setOrdeta_prod_count(cartList.get(i).getCart_prod_count());
            orderDetailList.add(orderDetail);
        }
        order1.setDetails(orderDetailList);
        addOrderData(order1);
    }
    /**
     * 支付精灵——支付
     */
   /* private void pay(String suject,String body,String orderId,String payUserId) {

        *
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

        EPay.getInstance(this).pay(suject, body, 1,
                orderId, payUserId,null, new PayResultListener() {
                    *
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

                    @Override
                    public void onFinish(Context context, Long payId, String orderId, String payUserId,
                                         EPayResult payResult , int payType, Integer amount) {
                        EPay.getInstance(context).closePayView();//关闭快捷支付页面
                        if(payResult.getCode() == EPayResult.SUCCESS_CODE.getCode()){
                            clearCartData();    //清空购物车
                            order.setOrde_status("0");
                            order.setOrde_pay(Integer.toString(payType));
                            addOrderData(order);//增加订单

                            //支付成功逻辑处理
                            Toast.makeText(ShopDetailActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }else if(payResult.getCode() == EPayResult.FAIL_CODE.getCode()){
                            //支付失败逻辑处理
                            Toast.makeText(ShopDetailActivity.this, payResult.getMsg(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }*/




    /**
     * 购物车适配器快速构造
     */
    public class CartAdapter extends BaseQuickAdapter<ShoppingCart, BaseViewHolder> {
        public CartAdapter(int layoutResId, List data) {
            super(layoutResId, data);
            Log.i(TAG, "CartAdapter: data"+data.toString());
        }

        @Override
        protected void convert(BaseViewHolder helper, ShoppingCart item) {

            DecimalFormat df = new DecimalFormat( "0.00");  //显示两位小数

            helper.setText(R.id.isdc_tv_product_name,item.getCart_prod_name() )     //商品名
                  .setText(R.id.isdc_tv_product_count,Integer.toString(item.getCart_prod_count()))
                  .setText(R.id.isdc_tv_product_price,df.format(item.getCart_prod_price()*item.getCart_prod_count()))
                  .addOnClickListener(R.id.isdc_iv_out_cart)    //购物车商品减1
                  .addOnClickListener(R.id.isdc_iv_add_cart);   //购物车商品加1
        }
    }


    /**
     * 购物车添加动画
     * @param start_location
     */
    public void playAnimation(int[] start_location){
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.ic_add_to_cart);
        setAnim(img,start_location);
    }

    private Animation createAnim(int startX, int startY){
        int[] des = new int[2];
        ivCart.getLocationInWindow(des);
        int ivCartWidth = ivCart.getMeasuredWidth();

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0]-startX+ivCartWidth/2-20, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1]-startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1,0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(500);

        return set;
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,int[] location) {
        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y-loc[1]);
        vg.addView(view);
    }
    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(anim_mask_layout, v, start_location);
        Animation set = createAnim(start_location[0],start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(final Animation animation) {


                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                        //购物车角标
                        cartCount++;

                        qBadgeView.bindTarget(ivCart).setBadgeNumber(cartCount);
                        Log.i(TAG, "run: 购物车角标1："+cartCount);

                        int count= 0;
                        Log.i(TAG, "onClick: cartList:"+cartList.size());


                        for(int i=0; i<cartList.size(); i++){
                            //购物车里已有该商品，直接修改数量
                            if(cartList.get(i).getCart_prod_id() == productList.get(currentProductPosition).getProd_id()){
                                cartList.get(i).setCart_prod_count(cartList.get(i).getCart_prod_count()+1);
                                updateCartData(cartList.get(i));        //购物车修改
                                Log.i(TAG, "onClick: 购物车修改");
                                break;       //跳出本次循环
                            }
                            //购物车里没有该商品，直接添加
                            if(i == cartList.size()-1){
                                ShoppingCart shoppingCart = new ShoppingCart();
                                shoppingCart.setCart_prod_count(1);
                                shoppingCart.setCart_shop_id(product.getShop_id());
                                shoppingCart.setCart_prod_price(productList.get(currentProductPosition).getProd_price());
                                shoppingCart.setCart_prod_id(productList.get(currentProductPosition).getProd_id());
                                shoppingCart.setCart_prod_name(productList.get(currentProductPosition).getProd_name());

                                cartList.add(shoppingCart);
                                addCartData(shoppingCart);      //购物车添加
                                Log.i(TAG, "onClick: 购物车添加"+count++);
                                break;      //跳出循环，不能少，否则陷入死循环
                            }


                        }
                        if(cartList.size()==0){
                            ShoppingCart shoppingCart = new ShoppingCart();
                            shoppingCart.setCart_prod_count(1);
                            shoppingCart.setCart_shop_id(product.getShop_id());
                            shoppingCart.setCart_prod_price(productList.get(currentProductPosition).getProd_price());
                            shoppingCart.setCart_prod_id(productList.get(currentProductPosition).getProd_id());
                            shoppingCart.setCart_prod_name(productList.get(currentProductPosition).getProd_name());

                            //cartList.add(shoppingCart);
                            addCartData(shoppingCart);      //购物车添加
                        }
                        cartAmount += productList.get(currentProductPosition).getProd_price();
                        Log.i(TAG, "onClick: 购物车总额：" +cartAmount);
                        tvCartAmount.setText(df.format(cartAmount));
                        cartAdapter.notifyDataSetChanged();   //更新购物车数据库


                    }
                },100);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        v.startAnimation(set);
    }


    /**
     * 加载店铺分类数据
     */
    public void loadClassifyData(){

        String condition = " and pc_shop_id="+product.getShop_id();
        Log.i(TAG, "loadClassifyData: condition:"+condition);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.PRODUCTCLASSIFY_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ProductClassify> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ProductClassify>>(){}.getType());

                    if(basePojo.isSuccess()){
                        productclassifyList = basePojo.getList();

                        if(currentClassifyId != -1){
                            Log.i(TAG, "传过来的id"+currentClassifyId);
                            for(int i=0; i<productclassifyList.size(); i++){
                                int id = productclassifyList.get(i).getPc_id();
                                Log.i(TAG, "onSuccess: currentClassifyId"+i+": " +id);
                                if(id == currentClassifyId){
                                    Log.i(TAG, "当前所在分类: "+i);
                                    currentClassify = i;
                                    selectedClassify = currentClassify;
                                    currentClassifyId = -1;
                                    break;
                                }
                            }
                        }
                        initLeftRecyclerView();     //初始化分类recylclerView
                        loadProductData();          //加载所属分类的商品
                        setEnabledRefresh();
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
    /**
     * 加载所属分类产品数据
     */
    public void loadProductData(){

        String condition = " and prod_clas_id="+productclassifyList.get(currentClassify).getPc_id();

        Log.i(TAG, "查询商品: condition:"+condition);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.PRODUCT_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<Product> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<Product>>(){}.getType());

                    if(basePojo.isSuccess()){
                        productList = basePojo.getList();

                        if(selectedClassify == currentClassify && selectedProdId !=-1){     //将上个页面点击的商品置于首位
                            Product productTemp = new Product();
                            for(int i=0; i<productList.size(); i++){
                                if(selectedProdId == productList.get(i).getProd_id()){
                                    productTemp = productList.get(0);
                                    productList.set(0,productList.get(i));
                                    productList.set(i,productTemp);
                                }
                            }
                        }


                        initRightRecyclerView();     //初始商品列表
                        if(refreshLayout != null){
                            itemShopProductAdapter.notifyDataSetChanged();
                            Log.i(TAG, "执行了更新商品列表"+refreshLayout.getState().toString());
                            switch (refreshLayout.getState()){
                                case Refreshing:        //上拉刷新
                                    refreshLayout.finishRefresh(0);

                                    break;
                                case Loading:           //下拉加载
                                    refreshLayout.finishLoadMore(0/*,false*/);//传入false表示加载失败
                                    break;
                            }
                            setEnabledRefresh();
                        }

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

    /**
     * 加载购物车数据
     */
    public void loadCartData(){

        String condition = " and cart_shop_id="+product.getShop_id()+" and cart_cust_id = "+customer.getCust_id();

        Log.i(TAG, "查询商品: condition:"+condition);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.SHOPPINGCART_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ShoppingCart> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ShoppingCart>>(){}.getType());

                    if(basePojo.isSuccess()){
                        List<ShoppingCart> cartListTemp = new ArrayList <>();
                        cartListTemp = basePojo.getList();
                        cartList.clear();
                        cartList.addAll(cartListTemp);
                        cartAdapter.notifyDataSetChanged();     //更新数据



                        if(cartCount==0){
                            for(ShoppingCart shoppingCart : cartList){
                                cartCount +=  shoppingCart.getCart_prod_count();        //计算购物车数目
                                cartAmount += shoppingCart.getCart_prod_price() * shoppingCart.getCart_prod_count();   //计算购物车总额

                            }
                            qBadgeView.bindTarget(ivCart).setBadgeNumber(cartCount);//更新购物车角标显示数字
                            tvCartAmount.setText(df.format(cartAmount));            //购物车总额
                            Log.i(TAG, "onSuccess: 购物车角标2"+cartCount);
                        }

                        setBtnBalance();        //设置结算按钮是否可点击



                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                    }else{
                        tvCartAmount.setText(df.format(cartAmount));            //购物车总额
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

    /**
     * 设置结算按钮是否可点击
     */
    private void setBtnBalance() {

        if(cartList.size()>0){
            btnBalacce.setEnabled(true);    //设置结算键可点击
        }else{
            btnBalacce.setEnabled(false);   //设置结算键不可点击
        }
    }


    /**
     * 增加购物车数据
     */
    public void addCartData(ShoppingCart shoppingCart){
        shoppingCart.setCart_cust_id(customer.getCust_id()); //设置购物用户id
        String paras = new Gson().toJson(shoppingCart);

        Log.i(TAG, "查询商品: condition:"+paras);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("paras",paras);

        httpClient.post(Constant.SHOPPINGCART_ADD, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ShoppingCart> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ShoppingCart>>(){}.getType());

                    if(basePojo.isSuccess()){

                        Log.i(TAG, "onSuccess: "+basePojo.getMsg());

                        loadCartData();
                        setBtnBalance();    //设置结算按钮是否可点击的

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

    /**
     * 购物车数据修改
     * @param shoppingCart
     */
    public void updateCartData(ShoppingCart shoppingCart) {

        shoppingCart.setCart_cust_id(customer.getCust_id()); //设置购物用户id
        String paras = new Gson().toJson(shoppingCart);

        Log.i(TAG, "查询商品: condition:"+paras);

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("paras",paras);

        httpClient.post(Constant.SHOPPINGCART_UPDATE, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ShoppingCart> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ShoppingCart>>(){}.getType());

                    if(basePojo.isSuccess()){
                        setBtnBalance();    //设置结算按钮是否可点击的
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

    /**
     * 购物车数据删除
     * @param cart_id 购物车id
     */
    public void deleteCartData(int cart_id) {



        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("cart_id",cart_id);

        httpClient.post(Constant.SHOPPINGCART_DELETE, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ShoppingCart> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ShoppingCart>>(){}.getType());

                    if(basePojo.isSuccess()){
                        Log.i(TAG, "onSuccess: "+basePojo.getMsg());
                        setBtnBalance();    //设置结算按钮是否可点击的
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

    /**
     * 清空购物车
     */
    public void clearCartData() {

        String condition = " and cart_cust_id = "+customer.getCust_id()+ " and cart_shop_id = "+product.getShop_id();

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.SHOPPINGCART_DELETE_BY_CONDITION, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onSuccess: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<ShoppingCart> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<ShoppingCart>>(){}.getType());

                    if(basePojo.isSuccess()){   //清空购物车成功

                        cartCount = 0;          //购物车数目为0
                        cartList.clear();       //购物车数据清空
                        cartAmount = 0;
                        tvCartAmount.setText("0"); //购物车金额置0
                        cartAdapter.notifyDataSetChanged();     //购物车适配器更新数据
                        bslCart.dismissSheet();                 //隐藏购物车弹窗
                        qBadgeView.bindTarget(ivCart).setBadgeNumber(cartCount);//更新购物车角标显示数字
                        setBtnBalance();    //设置结算按钮是否可点击的
                    }else{
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
                        Log.i(TAG,"order_id:"+basePojo.getList().get(0).getOrde_id());
                        order1.setOrde_id(basePojo.getList().get(0).getOrde_id());
                        clearCartData();        //清空购物车数据
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
    public void OrderUpdata(){
        order1.setOrde_pay_status("1");
        order1.setOrde_pay("支付宝");
        String paras = new Gson().toJson(order1);
        Log.i(TAG,"updataorder:"+paras);
        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("paras",paras);
        httpClient.post(Constant.ORDER_UPDATA, params, new AsyncHttpResponseHandler() {

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
    public void onBackPressed() {
        setResult(RESULT_FAIL);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_SUCCESS && requestCode == SHOP_DETAIL_REQUEST_CODE){
            if(data.getBooleanExtra("success",false)){
                String memo = data.getStringExtra("memo");
                memo =  memo == null|| memo.equals("") ? "支付成功" : memo;
                Toast.makeText(this,memo,Toast.LENGTH_SHORT).show();
                OrderUpdata();
            }else{
                String memo = data.getStringExtra("memo");
                memo =  memo == null|| memo.equals("") ? "取消支付" : memo;
                Toast.makeText(this,memo,Toast.LENGTH_SHORT).show();
            }
        }


    }
}

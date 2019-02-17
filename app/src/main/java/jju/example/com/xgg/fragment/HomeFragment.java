package jju.example.com.xgg.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.LoginActivity;
import jju.example.com.xgg.activity.SearchActivity;
import jju.example.com.xgg.activity.ShopDetailActivity;
import jju.example.com.xgg.adapter.ClassifyAdapter;
import jju.example.com.xgg.adapter.ProductListGridAdapter;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.pojo.Shop;
import jju.example.com.xgg.util.GlideImageLoader;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.MessageEvent;
import jju.example.com.xgg.util.MessageEvent1;
import jju.example.com.xgg.util.SPUtil;
import jju.example.com.xgg.view.HomeSearbar;
import jju.example.com.xgg.view.StarRatingView;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements View.OnClickListener {
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码

    private static final int REQUEST_HOME_PRODUCT1 = 1;       // 商品1的请求码
    private static final int REQUEST_HOME_PRODUCT2 = 2;       // 商品2的请求码
    private static final int REQUEST_HOME_PRODUCT3 = 3;       // 商品3的请求码
    private static final int REQUEST_HOME_SHOP = 0;               // 店铺的请求码
    private int classifyId = -1;         //传递分类id
    private int productId = -1;          //传递商品id

    private static final String TAG = "----首页----";
    private boolean isStatDark = true;
    private int homeStatAlpha = 0;

    private MaterialRefreshLayout refreshLayout;    // 下拉刷新控件
    private Banner banner;                          // 广告栏控件
    private TextView tvSearch;                      // 搜索框
    private GridView gv;    //分类导航控件
    private ClassifyAdapter classifyAdapter;    // 分类导航列表适配器
    private List<Integer> bannerImgList = new ArrayList<>();
    private LinearLayout llStasusbar ;

    private RecyclerView rvProdList;
    private ProductListGridAdapter productListGridAdapter;

    private HomeSearbar homeSearbar;
    private NestedScrollView nsvHome;

    private ImageView ivHomeFruit;

    private List<List<Product>> productList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // 获取新建视图View中布局文件的空间
        initView(view);
        initBanner();
        //initBaseQuickAdapter();  //快速构造适配器
        loadProductData();      //加载商品列表数据
        getWH();        //获取屏幕宽高
        return view;
    }

    /**
     * 快速构建商品列表适配器
     */
    private void initBaseQuickAdapter() {
        final ProductAdapter productAdapter = new ProductAdapter(R.layout.item_product_recommend_list,productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvProdList.setLayoutManager(layoutManager);
        rvProdList.setAdapter(productAdapter);
        productAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {


            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {



                SPUtil.saveProduct(getContext(),productList.get(position).get(0));      //保存商品信息

                switch (view.getId()){
                    case R.id.iprl_ll_product1:
                        Log.i(TAG, "onItemChildClick: 点击了第一个商品："+productList.get(position).get(0).getProd_name());
                        classifyId = productList.get(position).get(0).getProd_clas_id();
                        productId = productList.get(position).get(0).getProd_id();
                        isLogin(REQUEST_HOME_PRODUCT1);

                        break;
                    case R.id.iprl_ll_product2:
                        Log.i(TAG, "onItemChildClick: 点击了第二个商品："+productList.get(position).get(1).getProd_name());
                        classifyId = productList.get(position).get(1).getProd_clas_id();
                        productId = productList.get(position).get(1).getProd_id();
                        isLogin(REQUEST_HOME_PRODUCT2);
                        break;
                    case R.id.iprl_ll_product3:
                        Log.i(TAG, "onItemChildClick: 点击了第三个商品："+productList.get(position).get(2).getProd_name());
                        classifyId = productList.get(position).get(2).getProd_clas_id();
                        productId = productList.get(position).get(2).getProd_id();
                        isLogin(REQUEST_HOME_PRODUCT3);
                        break;
                    case R.id.iprl_iv_head:
                        classifyId = -1;
                        productId = -1;
                        isLogin(REQUEST_HOME_SHOP);

                        Log.i(TAG, "onItemChildClick: 点击了店铺图片");
                        break;
                }


            }


        });

    }
    /**
     * 判断是否登录
     */
    private void isLogin(int request) {
        if(SPUtil.isLogin(getContext())){
            Intent intent = new Intent(getContext(),ShopDetailActivity.class );     //跳转到店铺详情
            if (classifyId != -1){
                intent.putExtra("classifyId",classifyId);  //传递分类id
            }
            if (productId != -1){
                intent.putExtra("productId",productId);  //传递商品id
            }
            startActivity(intent);
        }else{
            Intent intent = new Intent(getContext(),LoginActivity.class );     //跳转到店铺详情
            intent.putExtra("isShow",true);  //传递分类id
            startActivityForResult(intent,request);
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_SUCCESS){
            switch (requestCode){
                case REQUEST_HOME_PRODUCT1:
                    isLogin(REQUEST_HOME_PRODUCT1);
                    break;
                case REQUEST_HOME_PRODUCT2:
                    isLogin(REQUEST_HOME_PRODUCT2);
                    break;
                case REQUEST_HOME_PRODUCT3:
                    isLogin(REQUEST_HOME_PRODUCT3);
                    break;
                case REQUEST_HOME_SHOP:
                    isLogin(REQUEST_HOME_SHOP);
                    break;
            }
        }


    }

    /**
     * 控件点击
     * @param v
     */
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_search_bar_text:     //点击搜索框

                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);          //跳转搜索页面
                break;
        }
    }

    public class ProductAdapter extends BaseQuickAdapter<List<Product>, BaseViewHolder>{


        public ProductAdapter(int layoutResId, @Nullable List <List<Product>> data) {
            super(layoutResId, data);
        }
        RequestOptions options = new RequestOptions()
                //.centerCrop()
                .placeholder(R.mipmap.img_shop);
        DecimalFormat df = new DecimalFormat( "0.00 ");
        DecimalFormat df1 = new DecimalFormat( "0.0 ");

        @Override
        protected void convert(BaseViewHolder helper, List<Product> item) {

            helper.setText(R.id.iprl_tv_name,item.get(0).getShop_name())
                    .setText(R.id.iprl_tv_level,df1.format(item.get(0).getShop_level())) //评分
                    .addOnClickListener(R.id.iprl_iv_head);
            StarRatingView ratingView = (StarRatingView)helper.getView(R.id.iprl_srv_level);
            ratingView.setRate((int) item.get(0).getShop_level()*2);      //评分

            Glide.with(getContext())
                    .load(item.get(0).getShop_head())
                    .apply(options)
                    .into((ImageView) helper.getView(R.id.iprl_iv_head));      //加载店铺图片

            switch (item.size()){
                default:
                case 3:     //商品数>=3
                    helper.setText(R.id.iprl_tv_product3_name,item.get(2).getProd_name())
                            .setText(R.id.iprl_tv_product3_price,"￥"+df.format(item.get(2).getProd_price()))
                            .addOnClickListener(R.id.iprl_ll_product1);
                    Glide.with(getContext())
                            .load(item.get(2).getProd_head())
                            .apply(options)
                            .into((ImageView) helper.getView(R.id.iprl_iv_product3_head));      //加载商品3图片
                    Log.i(TAG, "convert: 执行2");
                case 2:     //商品数=2
                    helper.setText(R.id.iprl_tv_product2_name,item.get(1).getProd_name());
                    helper.setText(R.id.iprl_tv_product2_price,"￥"+df.format(item.get(1).getProd_price()))
                            .addOnClickListener(R.id.iprl_ll_product2);
                    Glide.with(getContext())
                            .load(item.get(1).getProd_head())
                            .apply(options)
                            .into((ImageView) helper.getView(R.id.iprl_iv_product2_head));      //加载商品2图片
                    Log.i(TAG, "convert: 执行1");
                case 1:     //商品数=1
                    helper.setText(R.id.iprl_tv_product1_name,item.get(0).getProd_name());
                    helper.setText(R.id.iprl_tv_product1_price,"￥"+df.format(item.get(0).getProd_price()))
                            .addOnClickListener(R.id.iprl_ll_product3);
                    Glide.with(getContext())
                            .load(item.get(0).getProd_head())
                            .apply(options)
                            .into((ImageView) helper.getView(R.id.iprl_iv_product1_head));      //加载商品1图片
                    Log.i(TAG, "convert: 执行0");
                    break;
            }

            switch (item.size()){
                case 1:
                    helper.getView(R.id.iprl_ll_product2).setVisibility(View.INVISIBLE);
                case 2:
                    helper.getView(R.id.iprl_ll_product3).setVisibility(View.INVISIBLE);
                default:
                    break;
            }

        }
    }



    private void initView(View view) {

       refreshLayout = view.findViewById(R.id.refresh_home);            //下拉刷新控件
       banner = view.findViewById(R.id.banner_home);
       tvSearch = view.findViewById(R.id.home_search_bar_text);         //搜索框
       gv = view.findViewById(R.id.gv_home);
       llStasusbar = view.findViewById(R.id.llStatusbar);
       homeSearbar = view.findViewById(R.id.home_search_bar);
       nsvHome = view.findViewById(R.id.nsv_home_fragment);
       rvProdList = view.findViewById(R.id.rv_product_list_grid_home);

       ViewGroup.LayoutParams layoutParams = llStasusbar.getLayoutParams();
       layoutParams.height = getStatusBarHeight();

       ivHomeFruit = view.findViewById(R.id.iv_home_fruit);
       tvSearch.setOnClickListener(this);       //点击搜索框

        Glide.with(getContext())
                .load(R.mipmap.home_fruit_title)
                //.centerCrop() 千万不要加，加了就没有圆角效果了
                .apply(new RequestOptions().transforms(new CenterCrop(), new RoundedCorners(20)))
                .into(ivHomeFruit);

       classifyAdapter = new ClassifyAdapter();
       gv.setAdapter(classifyAdapter);


        classifyAdapter.setmOnItemClickListener(new ClassifyAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Bundle bundle = new Bundle();
                bundle.putInt("currentClassify",position);
                EventBus.getDefault().post(new MessageEvent(bundle));  // 通知Fragment刷新
                Log.i(TAG, "onClick: 点击分类图标");
            }

            @Override
            public void onLongClick(int position) {

            }
        });
       
       

        initRefreshLayout();            //初始化下拉刷新控件

        final int height = llStasusbar.getLayoutParams().height+48;
        Log.i("#######################", "initView: hight1="+height);
        Log.i("#######################", "initView: hight2="+llStasusbar.getLayoutParams().height);
        Log.i("#######################", "initView: hight3="+llStasusbar.getMeasuredHeight());

        nsvHome.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            // 将透明度声明成局部变量用于判断
            int alpha = 0;
            int count = 0;
            float scale = 0;

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if(scrollY <= height){
                    scale = (float) scrollY / height;
                    alpha = (int) (255 * scale);
                    setAlpha(alpha);
                    //随着滑动距离改变透明度
                    Log.i("#######################", "scrollY = "+scrollY);
                    Log.i("#######################", "scale = "+scale);
                    Log.i("#######################", "al = "+alpha);

                    //llStasusbar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                    Log.i("#######################", "颜色: "+Color.argb(alpha, 255, 255, 255));
                    homeSearbar.setHsBackground(Color.argb(alpha, 255, 255, 255));
                    if (Build.VERSION.SDK_INT >= 21){
                        View decorView = getActivity().getWindow().getDecorView();
                        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                        getActivity().getWindow().setStatusBarColor(Color.argb(alpha, 255, 255, 255));
                    }
                    llStasusbar.setVisibility(View.INVISIBLE);
                    homeSearbar.setChangeType(true);

                    setStatDark(false);      //保存当前状态栏为图标颜色为黑
                    if(scrollY == 0){
                        if (Build.VERSION.SDK_INT >= 21){
                            View decorView = getActivity().getWindow().getDecorView();
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
                        }
                        homeSearbar.setChangeType(false);
                        llStasusbar.setVisibility(View.VISIBLE);

                        setStatDark(true);      //保存当前状态栏为图标颜色为白


                    }

                }else {
                    if(alpha < 255){
                        Log.i("#######################", "执行次数: "+(++count));
                        //防止频繁重复设置相同的值影响性能
                        alpha = 255;
                        setAlpha(alpha);
                        setStatDark(false);      //保存当前状态栏为图标颜色为黑
                        //llStasusbar.setBackgroundColor(Color.argb(alpha, 255, 255, 255));
                        Log.i("#######################", "颜色: "+Color.argb(alpha, 255, 255, 255));
                        homeSearbar.setHsBackground(Color.argb(alpha, 255, 255, 255));
                        if (Build.VERSION.SDK_INT >= 21){
                            View decorView = getActivity().getWindow().getDecorView();
                            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                            getActivity().getWindow().setStatusBarColor(Color.argb(alpha, 255, 255, 255));
                        }

                    }
                }
            }
        });




    }

    private void initBanner() {

        bannerImgList.add(R.mipmap.banner1);
        bannerImgList.add(R.mipmap.banner2);
        bannerImgList.add(R.mipmap.banner3);

        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setImages(bannerImgList);
        banner.start();
    }

    /**
     * 下拉刷新控件
     */
    private void initRefreshLayout() {
        refreshLayout.setIsOverLay(false);  // 是否是侵入式下拉刷新
        refreshLayout.setWaveShow(true);    // 是否显示波浪纹
        refreshLayout.setWaveColor(getResources().getColor(R.color.logoOrange));   // 设置波浪纹颜色
        refreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            // 下拉刷新执行的方法
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                loadProductData(); // 重新加载商家数据
            }
        });
    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    
    /**
     * 设置状态栏的状态
     * @param statDark
     */
    public void setStatDark(boolean statDark) {
        isStatDark = statDark;
        EventBus.getDefault().post(new MessageEvent1(isStatDark,homeStatAlpha));     //传递状态栏当前状态
    }

    public void setAlpha(int alpha){
        homeStatAlpha = alpha;
    }

    /**
     * 获取当前设备屏幕高宽
     */
    private void getWH() {
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;         // 屏幕宽度（像素）
        int height = dm.heightPixels;       // 屏幕高度（像素）
        float density = dm.density;         // 屏幕密度（0.75 / 1.0 / 1.5）
        int densityDpi = dm.densityDpi;     // 屏幕密度dpi（120 / 160 / 240）
        // 屏幕宽度算法:屏幕宽度（像素）/屏幕密度
        int screenWidth = (int) (width / density);  // 屏幕宽度(dp)
        int screenHeight = (int) (height / density);// 屏幕高度(dp)
        Log.e("width=======>",screenWidth+"");
        Log.e("height=======>",screenHeight+"");

    }

    /**
     * 加载商品数据
     */
    public void loadProductData(){
        String condition = " and shop_id=prod_shop_id ORDER BY shop_id";        //查询条件

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);

        httpClient.post(Constant.PRODUCTSHOP_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<List<Product>> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<List<Product>>>(){}.getType());

                    if(basePojo.isSuccess()){
                        productList = new ArrayList <>();
                        productList = basePojo.getList();
                        initBaseQuickAdapter();
                        refreshLayout.finishRefresh();  //完成刷新
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

}

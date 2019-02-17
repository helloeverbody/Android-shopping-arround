package jju.example.com.xgg.fragment.classifypage;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.ShopDetailActivity;
import jju.example.com.xgg.adapter.ProductListGridAdapter;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.fragment.BaseFragment;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.util.GlideImageLoader;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.SPUtil;
import jju.example.com.xgg.view.StarRatingView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyPageFragment2 extends BaseFragment {

    private static final String TAG = "分类页面2";
    private String msg;
    private Banner banner;
    private RecyclerView rvProducts;
    private List<Integer> bannerImgList = new ArrayList<>();
    private ProductListGridAdapter productListGridAdapter;
    private List<List<Product>> productList = new ArrayList <>();

    public ClassifyPageFragment2() {
        // Required empty public constructor
    }
    @SuppressLint("ValidFragment")
    public ClassifyPageFragment2(String msg) {
        this.msg = msg;
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classify_page, container, false);

        initView(view);
        initBanner();
        loadProductData();

        return view;
    }

    /*private void initRecyclerView() {

        GridLayoutManager layoutManager =  new GridLayoutManager(getContext(),2);

        rvProducts.setLayoutManager(layoutManager);

        productListGridAdapter = new ProductListGridAdapter(getContext(),getProducts());

        rvProducts.setAdapter(productListGridAdapter);



    }*/

    private void initBanner() {

        bannerImgList.add(R.mipmap.home_banner_img1);
        bannerImgList.add(R.mipmap.home_banner_img2);
        bannerImgList.add(R.mipmap.home_banner_img3);

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

    private void initView(View view) {
        banner = view.findViewById(R.id.banner_classify_page);
        rvProducts = view.findViewById(R.id.rv_product_list_grid_classify_page);

    }
/*

    */
/**
 * 测试，待删除
 * @return
 *//*

    private List<Product> getProducts() {

        Product product = new Product();
        product.setProd_name("菲律宾进口香蕉");
        product.setProd_price(33d);

        List<Product> products = new ArrayList<Product>();

        for(int i=0; i<10; i++){
            products.add(product);
        }

        Log.i("#######################", "Product: "+products.toString());
        return products;
    }

*/

    /**
     * 加载商品数据
     */
    public void loadProductData(){
        String condition = " and shop_id=prod_shop_id and shop_clas_id = 2 ORDER BY shop_id";        //查询条件

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
     * 快速构建商品列表适配器
     */
    private void initBaseQuickAdapter() {
        ProductAdapter productAdapter = new ProductAdapter(R.layout.item_product_recommend_list,productList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvProducts.setLayoutManager(layoutManager);
        rvProducts.setAdapter(productAdapter);
        productAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {



                SPUtil.saveProduct(getContext(),productList.get(position).get(0));      //保存商品信息
                Intent intent = new Intent(getContext(),ShopDetailActivity.class );     //跳转到店铺详情
                switch (view.getId()){
                    case R.id.iprl_ll_product1:
                        Log.i(TAG, "onItemChildClick: 点击了第一个商品："+productList.get(position).get(0).getProd_name());

                        intent.putExtra("classifyId",productList.get(position).get(0).getProd_clas_id());  //传递分类id
                        intent.putExtra("productId",productList.get(position).get(0).getProd_id());  //传递商品id

                        break;
                    case R.id.iprl_ll_product2:
                        Log.i(TAG, "onItemChildClick: 点击了第二个商品："+productList.get(position).get(1).getProd_name());
                        intent.putExtra("classifyId",productList.get(position).get(1).getProd_clas_id());
                        intent.putExtra("productId",productList.get(position).get(1).getProd_id());  //传递商品id
                        break;
                    case R.id.iprl_ll_product3:
                        Log.i(TAG, "onItemChildClick: 点击了第三个商品："+productList.get(position).get(2).getProd_name());
                        intent.putExtra("classifyId",productList.get(position).get(2).getProd_clas_id());
                        intent.putExtra("productId",productList.get(position).get(2).getProd_id());  //传递商品id
                        break;
                    case R.id.iprl_iv_head:
                        intent.putExtra("classifyId",productList.get(position).get(0).getProd_clas_id());
                        Log.i(TAG, "onItemChildClick: 点击了店铺图片");
                        break;
                }
                startActivity(intent);
            }
        });

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



}

package jju.example.com.xgg.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ItemShopListAdapter;
import jju.example.com.xgg.config.Constant;
import jju.example.com.xgg.pojo.BasePojo;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.util.JsonUtil;
import jju.example.com.xgg.util.SPUtil;

public class SearchActivity extends FullTranStatActivity implements View.OnClickListener, TextView.OnEditorActionListener {

    private static final int SHOW_SEARCH_HISTOTY = 1;
    private static final int SHOW_SEARCH_PRODUCTS = 2;
    private static final int SEARCH_REQUEST_CODE = 1;
    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final String TAG = "商品搜索页面";
    private EditText etSearch;              //搜索框
    private ImageView ivTextDel;            //搜索框文字删除
    private RecyclerView rvSearchHistory;   //搜索历史列表
    private TextView tvGoSearch;            //搜索按钮
    private ImageView ivBack;               //返回键
    private SearchHistoryAdapter searchHistoryAdapter;    //搜索历史适配器
    private List<String> searchHistoryList = new ArrayList <>(); //搜索历史集合

    private LinearLayout llSearchHistory;       //搜索历史
    private LinearLayout llProduct;             //搜索结果
    private RecyclerView rvProduct;             //商品列表
    private ItemShopListAdapter itemShopListAdapter;        //商品列表适配器
    private List<List<Product>> productList = new ArrayList <>();  //商品列表数据

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setStatusBarFullTransparent(true);      //状态栏透明
        setFitSystemWindow(true);               //添加状态栏高度
        loadData();                             //加载数据
        init();                                 //初始化
        initRvSearchHistory();                  //初始化搜索历史列表

    }

    /**
     * 加载数据
     */
    private void loadData() {

        searchHistoryList = new ArrayList <>(Arrays.asList(new String[]{"苹果","香蕉","橘子","葡萄","一号店","二号店","三号店","五号店","八号店","梨"}));
    }


    /**
     * 初始化
     */
    private void init() {
        etSearch = findViewById(R.id.search_et_search);                 //搜索框
        ivTextDel = findViewById(R.id.search_iv_text_delete);           //搜索框文字删除
        rvSearchHistory = findViewById(R.id.search_rv_search_history);  //搜索历史列表
        tvGoSearch = findViewById(R.id.search_tv_go_search);            //搜索按钮
        ivBack = findViewById(R.id.search_iv_back);                     //返回键
        llSearchHistory = findViewById(R.id.search_ll_search_history);  //搜索历史
        llProduct = findViewById(R.id.search_ll_product);               //搜索结果
        rvProduct = findViewById(R.id.search_rv_product);               //商品列表

        setShow(SHOW_SEARCH_HISTOTY);

        etSearch.setOnEditorActionListener(this);
        tvGoSearch.setOnClickListener(this);
        ivTextDel.setOnClickListener(this);
        ivBack.setOnClickListener(this);

        etSearch.addTextChangedListener(new TextWatcher() {         //输入框监听
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0){       //输入框有字符串时，显示文本删除按钮
                    ivTextDel.setVisibility(View.VISIBLE);
                }else {
                    ivTextDel.setVisibility(View.GONE);
                    setShow(SHOW_SEARCH_HISTOTY);
                }
                //此语句不可少，否则输入的光标会出现在最左边，不会随输入的值往右移动
                etSearch.setSelection(s.length());



            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    //初始化搜索历史适配器
    private void initRvSearchHistory() {
        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history_layout,searchHistoryList);     //搜索历史适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,120);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){
            @Override
            public int getSpanSize(int position) {
                /**
                 * 重点在这里，这个getSpanSize方法的返回值其实指的是单个item所占用的我们设置的每行的item个数
                 */
                int stringLenth = searchHistoryList.get(position).length();
                int spanSize = 0;

                switch (stringLenth){
                    case 1:
                        spanSize = 15;
                        break;
                    case 2:
                        spanSize = 20;
                        break;
                    case 3:
                    case 4:
                        spanSize = 24;
                        break;
                    case 5:
                        spanSize = 30;
                        break;
                    case 6:
                    case 7:
                        spanSize = 40;
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        spanSize = 60;
                        break;
                    default:
                        spanSize = 120;
                        break;
                }

                return spanSize;
            }
        });
        rvSearchHistory.setAdapter(searchHistoryAdapter);
        rvSearchHistory.setLayoutManager(gridLayoutManager);

        searchHistoryAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //搜索历史
                TextView tvSearchHistory = (TextView) adapter.getViewByPosition(rvSearchHistory,position,R.id.ishl_tv_search_history);
                etSearch.setText(tvSearchHistory.getText());
                if(etSearch.getText().toString().isEmpty()){        //判断输入框是否为空
                    Toast.makeText(SearchActivity.this,"请输入商家或商品名！",Toast.LENGTH_SHORT).show();
                }
                updateSearchHistory();      //更新搜索历史
                setShow(SHOW_SEARCH_PRODUCTS);  //隐藏搜素历史
                etSearch.clearFocus();          //移除焦点
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(),0);        //隐藏软键盘
                search();          //执行搜索

            }
        });

    }

    /**
     * 初始化商品适配器
     */
    public void initRvProduct(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvProduct.setLayoutManager(layoutManager);
        itemShopListAdapter = new ItemShopListAdapter(this,productList);
        rvProduct.setAdapter(itemShopListAdapter);
        //商品列表点击监听
        itemShopListAdapter.setmOnItemClickListener(new ItemShopListAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position, View v) {
                switch (v.getId()){
                    case R.id.ispl_iv_shop_head:        //点击店铺头像
                        Log.i(TAG, "onClick: 点击头像2");
                        SPUtil.saveProduct(SearchActivity.this,productList.get(position).get(0));  //保存商品信息到本地
                        if(!SPUtil.isLogin(SearchActivity.this)){
                            Intent intent = new Intent(SearchActivity.this,LoginActivity.class);
                            intent.putExtra("isShow",true);
                            startActivityForResult(intent,SEARCH_REQUEST_CODE);
                        }else{
                            SPUtil.saveProduct(SearchActivity.this,productList.get(position).get(0));
                            Intent intent = new Intent(SearchActivity.this,ShopDetailActivity.class);
                            startActivity(intent);          //跳转页面
                        }

                        break;
                }
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.search_tv_go_search:
                if(etSearch.getText().toString().isEmpty()){        //判断输入框是否为空
                    Toast.makeText(this,"请输入商家或商品名！",Toast.LENGTH_SHORT).show();
                    break;
                }

                updateSearchHistory();      //更新搜索历史
                etSearch.clearFocus();          //移除焦点
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(),0);        //隐藏软键盘
                setShow(SHOW_SEARCH_PRODUCTS);  //隐藏搜素历史
                search();          //执行搜索
                break;
            case R.id.search_iv_text_delete:
                etSearch.setText("");
                break;

            case R.id.search_iv_back:       //点击返回键
                finish();                   //结束当前页面
                break;
        }
    }
    //更新搜索历史
    private void updateSearchHistory() {
        String searchString = etSearch.getText().toString();
        if(searchHistoryList.contains(searchString)){
            String searchHistotyTemp = searchHistoryList.get(0);
            searchHistoryList.set(0,searchString);
            for(int i=1; i<searchHistoryList.size(); i++){
                if(searchHistoryList.get(i).equals(searchString)){
                    searchHistoryList.set(i,searchHistotyTemp);
                    break;
                }
            }
            searchHistoryAdapter.notifyDataSetChanged(); //更新搜索历史适配器
            return;
        }
        for(int i=searchHistoryList.size()-1;i>0; i--){
            searchHistoryList.set(i,searchHistoryList.get(i-1));
        }
        searchHistoryList.set(0,searchString);
        searchHistoryAdapter.notifyDataSetChanged(); //更新搜索历史适配器
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

        switch (actionId){
            case EditorInfo.IME_ACTION_SEARCH:       //点击软键盘搜索
                if(etSearch.getText().toString().isEmpty()){        //判断输入框是否为空
                    Toast.makeText(this,"请输入商家或商品名！",Toast.LENGTH_SHORT).show();
                    break;
                }
                updateSearchHistory();      //更新搜索历史
                setShow(SHOW_SEARCH_PRODUCTS);  //隐藏搜素历史
                etSearch.clearFocus();          //移除焦点
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(etSearch.getWindowToken(),0);        //隐藏软键盘
                search();          //执行搜索

                break;
        }
        return false;
    }

    /**
     * 设置显示搜索历史或搜索结果
     */
    public void setShow(int show){
        switch(show){
            case SHOW_SEARCH_HISTOTY:
                llSearchHistory.setVisibility(View.VISIBLE);
                llProduct.setVisibility(View.GONE);
                break;
            case SHOW_SEARCH_PRODUCTS:
                llSearchHistory.setVisibility(View.GONE);
                llProduct.setVisibility(View.VISIBLE);
                break;
        }
    }

    /**
     * 执行搜索
     */
    public void search(){


        String condition = " and (prod_name like '%"+etSearch.getText().toString()+"%' or shop_name like '%"+etSearch.getText().toString()+"%')" +
                " and shop_id=prod_shop_id ORDER BY shop_id";        //查询条件

        AsyncHttpClient httpClient = new AsyncHttpClient();  //异步网络框架 接收selevet请求
        RequestParams params=new RequestParams();     //封装参数
        params.put("condition",condition);
        Log.i(TAG, "search: 搜索条件"+params);
        httpClient.post(Constant.PRODUCTSHOP_FIND, params, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.i(TAG, "onFailure: 连接服务器成功！");
                if(statusCode==200){        //响应成功
                    String jsonString = new String(responseBody);
                    BasePojo<List<Product>> basePojo = null; //把JSON字符串转为对象
                    basePojo = JsonUtil.getBaseFromJson(jsonString, new TypeToken<BasePojo<List<Product>>>(){}.getType());

                    if(basePojo.isSuccess()){
                        productList = basePojo.getList();
                        Log.i(TAG, "onSuccess: productList"+productList);
                        initRvProduct();   //初始化商品列表适配器


                        Log.d(TAG, "获取成功：\n"+basePojo.toString());
                    }else{
                        Log.i(TAG, "获取失败: "+basePojo.getMsg());
                    }

                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(TAG, "onFailure: statusCode："+statusCode);
                Log.i(TAG, "onFailure: responseBody："+responseBody);
                Log.i(TAG, "onFailure: error："+error);
                Log.i(TAG, "onFailure: 连接服务器失败！");
            }
        });
    }

    /**
     * 搜索历史Item适配器快速构造
     */
    public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public SearchHistoryAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.ishl_tv_search_history,item) ;    //搜索历史
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == SEARCH_REQUEST_CODE){
            Log.i(TAG, "onActivityResult: 从店铺详情返回");
        }
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_SUCCESS){

            Intent intent = new Intent(SearchActivity.this,ShopDetailActivity.class);
            if (data != null && data.getIntExtra("classifyId",-1) != -1){
                intent.putExtra("classifyId",data.getIntExtra("classifyId",-1));
            }
            if (data != null && data.getIntExtra("productId",-1) != -1){
                intent.putExtra("productId",data.getIntExtra("productId",-1));
                Log.i(TAG, "onActivityResult: 传递的数据"+data.getIntExtra("classifyId",-1));
            }
            startActivity(intent);
        }

    }
}

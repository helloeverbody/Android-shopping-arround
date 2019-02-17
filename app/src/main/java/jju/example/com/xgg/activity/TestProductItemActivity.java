package jju.example.com.xgg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ItemProductListAdapter;
import jju.example.com.xgg.adapter.ItemShopListAdapter;
import jju.example.com.xgg.adapter.ProductListGridAdapter;
import jju.example.com.xgg.pojo.Product;

public class TestProductItemActivity extends AppCompatActivity {

    private RecyclerView rvTest;
    private ItemShopListAdapter itemProductListAdapter;
    private List<List<Product>> list = new ArrayList <>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);

        rvTest = findViewById(R.id.test_rv_test);
        List<Product> productList = new ArrayList <>();
        Product product = new Product();

        product.setShop_name("一号店");
        product.setProd_name("苹果");



        for(int i=0; i<2; i++){
            productList.add(product);       //第一个商品
        }
        Product product1 = new Product();
        product1.setProd_name("芒果");
        productList.add(product1);
        productList.get(0).setProd_name("香蕉");

        list.add(productList);

//        for(int i=0; i<2; i++){
//            productList.add(product);       //第一个商品
//        }
//
//
//        productList.get(2).setProd_name("芒果");
//        productList.get(3).setProd_name("梨子");
//
//        list.add(productList);




        for (List<Product> products : list){
            Log.e("ceshi", "onCreate: products:"+products);
        }

        Log.i("测试测试测试测试", "onCreate: "+list.get(0).size());
        initRecyclerView();
    }
    private void initRecyclerView() {

        LinearLayoutManager layoutManager =  new LinearLayoutManager(this);

        rvTest.setLayoutManager(layoutManager);

        itemProductListAdapter = new ItemShopListAdapter(this,list);

        rvTest.setAdapter(itemProductListAdapter);



    }
}

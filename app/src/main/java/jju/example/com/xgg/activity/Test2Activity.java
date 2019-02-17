package jju.example.com.xgg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ProductListGridAdapter;
import jju.example.com.xgg.pojo.Product;

public class Test2Activity extends AppCompatActivity {

    private RecyclerView rvTest;
    private ProductListGridAdapter productListGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        init();

        initRecyclerView();
    }

    private void init() {
        rvTest = findViewById(R.id.rv_test);
    }

    private void initRecyclerView() {
        GridLayoutManager layoutManager = new GridLayoutManager(this,2);

        rvTest.setLayoutManager(layoutManager);

        productListGridAdapter = new ProductListGridAdapter(this,getProducts());
        rvTest.setAdapter(productListGridAdapter);


    }

    private List<Product> getProducts() {

        Product product = new Product();
        product.setProd_name("菲律宾进口香蕉");
        product.setProd_price(33d);

        List<Product> products = new ArrayList<Product>();

        for(int i=0; i<10; i++){
            products.add(product);
        }


        return products;
    }
}

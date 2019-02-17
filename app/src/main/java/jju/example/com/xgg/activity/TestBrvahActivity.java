package jju.example.com.xgg.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;

public class TestBrvahActivity extends AppCompatActivity {
    private RecyclerView rvCart;
    private List<String> cartList = new ArrayList <>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_brvah);
        init();

        /*模拟数据*/
        for(int i=0; i<3; i++){
            cartList.add("苹果" + i);
        }
        CartAdapter cartAdapter = new CartAdapter(R.layout.item_shop_detail_cart,cartList);
        LinearLayoutManager lmCart =  new LinearLayoutManager(this);
        rvCart.setLayoutManager(lmCart);
        rvCart.setAdapter(cartAdapter);

    }

    private void init() {
        rvCart = findViewById(R.id.test_brvah_rv_cart);
    }
    /**
     * 购物车Item适配器快速构造
     */
    public class CartAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public CartAdapter(int layoutResId, List data) {
            super(layoutResId, data);
            Log.i(TAG, "CartAdapter: data"+data.toString());
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.isdc_tv_product_name,item )     //商品名
                    .addOnClickListener(R.id.isdc_iv_out_cart)    //购物车商品减1
                    .addOnClickListener(R.id.isdc_iv_add_cart);   //购物车商品加1
        }
    }
}

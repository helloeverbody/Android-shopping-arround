package jju.example.com.xgg.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ItemShoppingCartAdapter;
import jju.example.com.xgg.adapter.ProductListGridAdapter;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.pojo.ShoppingCart;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShopCartFragment extends BaseFragment {

    private RecyclerView rvShoppingCart;
    private RecyclerView rvRecommendProduct;
    private ItemShoppingCartAdapter itemShoppingCartAdapter;
    private ProductListGridAdapter productListGridAdapter;
    private List<ShoppingCart> shoppingCarts;
    private List<Product> products;



    public ShopCartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shop_cart, container, false);
        initView(view);
        initRvShoppingCart();
        initRvProducts();
        return view;
    }

    private void initRvProducts() {
        GridLayoutManager layoutManager =  new GridLayoutManager(getContext(),2);

        rvRecommendProduct.setLayoutManager(layoutManager);

        productListGridAdapter = new ProductListGridAdapter(getContext(),getProducts());

        rvRecommendProduct.setAdapter(productListGridAdapter);

    }

    private void initRvShoppingCart() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvShoppingCart.setLayoutManager(layoutManager);
        itemShoppingCartAdapter = new ItemShoppingCartAdapter(getContext(),getShoppintCarts());
        rvShoppingCart.setAdapter(itemShoppingCartAdapter);

    }

    /**
     * 测试，待删除
     * @return
     */
    private List<ShoppingCart> getShoppintCarts() {
        shoppingCarts = new ArrayList<>();
        /*ShoppingCart shoppingCart = new ShoppingCart();*/
        /*shoppingCart.setCart_id(1);*/
        /*for(int i=0; i<3; i++){
            shoppingCarts.add(shoppingCart);
        }*/
        //ShoppingCart shoppingCart;
        //shoppingCarts.add(shoppingCart);

        return shoppingCarts;

    }

    /**
     * 测试，待删除
     * @return
     */
    private List<Product> getProducts(){
        Product product = new Product();
        product.setProd_name("进口香蕉");
        product.setProd_price(15d);
        products = new ArrayList<>();
        for(int i=0; i<10; i++){
            products.add(product);
        }
        return products;
    }

    private void initView(View view) {
        rvShoppingCart = view.findViewById(R.id.rv_cart_list_shopping_cart);
        rvRecommendProduct = view.findViewById(R.id.rv_product_list_shopping_cart);
    }

}

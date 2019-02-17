package jju.example.com.xgg.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import jju.example.com.xgg.pojo.Customer;
import jju.example.com.xgg.pojo.Product;
import jju.example.com.xgg.pojo.Shop;

/**
 * Created by 112 on 2018/6/8.
 * SPUtil工具 以SharedPreferences方式保存实体类
 */

public class SPUtil {


    /*获取登录状态*/
    public static boolean isLogin(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_CUSTOMER", Context.MODE_PRIVATE);
        boolean isLogin = sp.getBoolean("isLogin",false);
        Log.i("登录状态", "isLogin: "+isLogin);
        return isLogin;
    }
    /*退出登录*/
    public static void customerLogout(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_CUSTOMER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isLogin", false);
        editor.putString("customer", null);
        //editor.putBoolean("rememberpassword", false);
        editor.commit();
    }

    /*保存用户*/
    public static void saveCustomer(Context context, Customer customer){
        SharedPreferences sp = context.getSharedPreferences("SP_CUSTOMER",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("customer", new Gson().toJson(customer));
        editor.putBoolean("isLogin",true);
        editor.commit();
    }
    /*获取用户信息*/
    public static Customer getCustomer(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_CUSTOMER",Context.MODE_PRIVATE);
        String customerJson = sp.getString("customer", "");
        Customer customer = JsonUtil.getObjectFromJson(customerJson, Customer.class);
        return customer;
    }

    /*保存商家信息*/
    public static void saveShop(Context context, Shop shop){
        SharedPreferences sp = context.getSharedPreferences("SP_SHOP",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("shop", new Gson().toJson(shop));
        editor.commit();
    }
    /*获取商家信息*/
    public static Shop getShop(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_SHOP",Context.MODE_PRIVATE);
        String shopJson = sp.getString("shop", "");
        Shop shop = JsonUtil.getObjectFromJson(shopJson, Shop.class);
        return shop;
    }
    /*保存商品信息*/
    public static void saveProduct(Context context, Product product){
        SharedPreferences sp = context.getSharedPreferences("SP_PRODUCT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("product", new Gson().toJson(product));
        editor.commit();
    }
    /*获取商品信息*/
    public static Product getProduct(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_PRODUCT",Context.MODE_PRIVATE);
        String productjson = sp.getString("product", "");
        Product product = JsonUtil.getObjectFromJson(productjson, Product.class);
        return product;
    }

    /*保存订单信息*//*
    public static void saveOrder(Context context, Product product){
        SharedPreferences sp = context.getSharedPreferences("SP_PRODUCT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("product", new Gson().toJson(product));
        editor.commit();
    }
    *//*获取订单信息*//*
    public static Product getProduct(Context context){
        SharedPreferences sp = context.getSharedPreferences("SP_PRODUCT",Context.MODE_PRIVATE);
        String productjson = sp.getString("product", "");
        Product product = JsonUtil.getObjectFromJson(productjson, Product.class);
        return product;
    }*/
}

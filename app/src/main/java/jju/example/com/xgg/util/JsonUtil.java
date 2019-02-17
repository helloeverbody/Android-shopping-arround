package jju.example.com.xgg.util;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import jju.example.com.xgg.pojo.BasePojo;

/**
 * Json解析工具类
 */

public class JsonUtil {

    /**
     * 将Json字符串解析为Object对象
     * @param json : 待解析的Json字符串
     * @param tClass : Object对象的类型
     */
    public static <T> T getObjectFromJson(String json, Class<T> tClass) {
        // 解析Json字符串
        Gson gson = new Gson();
        T t = gson.fromJson(json, tClass);
        return t;
    }

    /**
     * 将Json字符串解析为List集合
     */
    public static <T> List<T> getListFromJson(String json, Type type) {
        // 解析Json字符串
        Gson gson = new Gson();
        List<T> list = gson.fromJson(json, type);   // 解析Json
        return list;
    }

    /**
     * 按照后台封装Json的格式进行解析
     */
    public static <T> BasePojo<T> getBaseFromJson(String json, Type type) {
        // 解析Json字符串
        Gson gson = new Gson();
        BasePojo<T> basePojo = gson.fromJson(json, type);   // 解析Json
        if(basePojo != null){
        }else{
            Log.i("JsonUtil", "getBaseFromJson: 数据解析失败! ");
        }
        return basePojo;
    }
}

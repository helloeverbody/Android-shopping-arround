package jju.example.com.xgg.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by 112 on 2018/12/6.
 */

public class DateUtil {

    /**
     * 获取当前时间 格式 yyyy-MM-dd HH:mm:ss
     * @return 当前时间字符串
     */
    public static String getDateTime(){
        String dateTime = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        dateTime = df.format(new Date());// new Date()为获取当前系统时间
        return dateTime;
    }
}

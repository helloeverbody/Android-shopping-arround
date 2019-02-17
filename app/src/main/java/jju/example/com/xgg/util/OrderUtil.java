package jju.example.com.xgg.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by 112 on 2018/11/20.
 */

public class OrderUtil {

    /**
     * 生成时间戳订单号
     * @return
     */
    public static String getOrderNumber() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSS");
        String fileName = format.format(new Timestamp(System
                .currentTimeMillis()));
        return fileName;
    }
}

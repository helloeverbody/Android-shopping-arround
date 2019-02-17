package jju.example.com.xgg.config;

/**
 * Created by 112 on 2018/10/29.
 */

public class Constant {

    /*阿里云主机*/
    //public static final String BASE_URL = "http://101.132.142.62:8080/xgg";
    /*教室*/
    //public static final String BASE_URL = "http://192.168.2.101:8080/xgg";

    /*模拟器*/
    //public static final String BASE_URL = "http://192.168.56.1:8080/xgg";
    /*真机*/
    //public static final String BASE_URL = "http://192.168.43.97:8080/xgg";
    /*宽带*/
   // public static final String BASE_URL = "http://xgg.nat300.top/xgg";
    public static final String BASE_URL = "http://192.168.43.132:8080/xgg";

    /** 七牛云存储空间基地址 **/
    public static final String QINIU_BASE_URL = "http://yhf.pub/";

    /*七牛云获取uptoken*/
    public static final String QINIU_UPTOKEN= BASE_URL + "/GetUptoken";
    /*用户查询*/
    public static final String CUSTOMER_FIND= BASE_URL + "/CustomerFind";
    /*用户注册*/
    public static final String CUSTOMER_REGIST= BASE_URL + "/CustomerRegist";

    /*用户登录*/
    public static final String CUSTOMER_LOGIN= BASE_URL + "/CustomerLogin";

    /*用户修改*/
    public static final String CUSTOMER_UPDATE= BASE_URL + "/CustomerUpdate";
    /*获取商品店铺信息*/
    public static final String PRODUCTSHOP_FIND= BASE_URL + "/ProductShopFind";
    /*获取商品信息*/
    public static final String PRODUCT_FIND= BASE_URL + "/ProductFind";
    /*获取店铺分类信息*/
    public static final String PRODUCTCLASSIFY_FIND= BASE_URL + "/ProductClassifyFind";
    /*获取该店铺购物车信息*/
    public static final String SHOPPINGCART_FIND= BASE_URL + "/ShoppingCartFind";
    /*增加该店铺购物车信息*/
    public static final String SHOPPINGCART_ADD= BASE_URL + "/ShoppingCartAdd";
    /*修改该店铺购物车信息*/
    public static final String SHOPPINGCART_UPDATE= BASE_URL + "/ShoppingCartUpdate";
    /*删除购物车记录*/
    public static final String SHOPPINGCART_DELETE= BASE_URL + "/ShoppingCartDelete";
    /*删除购物车记录*/
    public static final String SHOPPINGCART_DELETE_BY_CONDITION= BASE_URL + "/ShoppingCartDeleteByCondition";

    /*获取该店铺购物车信息*/
    public static final String ORDER_FIND= BASE_URL + "/OrderFind";
    /*增加订单信息*/
    public static final String ORDER_ADD= BASE_URL + "/OrderAdd";
//    修改订单信息
    public static final String ORDER_UPDATA=BASE_URL + "/OrderUpdate";
    /*支付宝生成订单*/
    public static final String ALIPAY_CREATE_ORDER= BASE_URL + "/AliPayCreateOrder";

}

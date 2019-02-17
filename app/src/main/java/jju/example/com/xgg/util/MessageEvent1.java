package jju.example.com.xgg.util;

/**
 * Created by 112 on 2018/6/1.
 */

public class MessageEvent1 {
    private String msg;
    private Boolean isTure;
    private Integer alpha;

    public MessageEvent1(String msg) {
        this.msg = msg;
    }

    public MessageEvent1(Boolean isTure) {
        this.isTure = isTure;
    }

    public MessageEvent1(Boolean isTure, Integer alpha) {
        this.isTure = isTure;
        this.alpha = alpha;
    }

    public MessageEvent1(Integer alpha) {
        this.alpha = alpha;
    }

    public String getMsg() {
        return msg;
    }

    public Boolean isTure() {
        return isTure;
    }
    public Integer getAlpha(){
        return alpha;
    }
}

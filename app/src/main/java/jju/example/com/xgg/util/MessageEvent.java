package jju.example.com.xgg.util;

import android.os.Bundle;

/**
 * Created by 112 on 2018/6/1.
 */

public class MessageEvent {
    private String msg;
    private Boolean isTure;
    private Integer alpha;
    private Bundle bundle;

    public MessageEvent(String msg) {
        this.msg = msg;
    }

    public MessageEvent(Boolean isTure) {
        this.isTure = isTure;
    }
    public MessageEvent(Bundle bundle) {
        this.bundle = bundle;
    }

    public MessageEvent(Boolean isTure, Integer alpha) {
        this.isTure = isTure;
        this.alpha = alpha;
    }

    public MessageEvent(Integer alpha) {
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
    public Bundle getBundle(){
        return bundle;
    }
}

package jju.example.com.xgg.view;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jju.example.com.xgg.R;

/**
 * Created by 112 on 2018/10/9.
 */

public class MyStatusBar extends LinearLayout {

    private View myStatBar;


    public MyStatusBar(Context context, AttributeSet attrs) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.status_bar,this,true);

        myStatBar = findViewById(R.id.my_status_bar);

        ViewGroup.LayoutParams layoutParams = myStatBar.getLayoutParams();
        layoutParams.height = getStatusBarHeight();



    }

    /**
     * 利用反射获取状态栏高度
     * @return
     */
    public int getStatusBarHeight() {
        int result = 0;
        //获取状态栏高度的资源id
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }



}

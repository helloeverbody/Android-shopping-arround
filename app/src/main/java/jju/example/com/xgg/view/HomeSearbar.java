package jju.example.com.xgg.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import jju.example.com.xgg.R;

/**
 * Created by 112 on 2018/9/26.
 */

public class HomeSearbar extends LinearLayout {
    private LinearLayout llContainer;
    private LinearLayout llLeftBg;
    private ImageView ivLeft;
    private TextView tvLeft;
    private LinearLayout llSearchText;
    private ImageView ivSearch;
    private TextView tvSearch;
    private LinearLayout llRight;
    private ImageView ivRight;
    private TextView tvRight;
    private boolean changeType = false;

    public HomeSearbar(Context context) {
        super(context);
    }

    public HomeSearbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        // 加载布局
        LayoutInflater.from(context).inflate(R.layout.home_search, this);

        init();
        TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.HomeSearbar);
        for(int i=0; i<attributes.length(); i++){
            int itemId = attributes.getIndex(i);
            switch (itemId){
                case R.styleable.HomeSearbar_changeType:
                    if(attributes.getBoolean(itemId,false)){

                        this.llLeftBg.setBackgroundResource(R.color.transparent);
                        this.ivLeft.setImageResource(R.drawable.ic_search_scan_changed);
                        this.tvLeft.setTextColor(getResources().getColor(R.color.ic_text_changed));
                        this.llSearchText.setBackgroundResource(R.drawable.home_title_bar_search_corner_bg_changed);
                        this.ivSearch.setImageResource(R.drawable.ic_search_changed);
                        this.tvSearch.setTextColor(getResources().getColor(R.color.white));
                        this.llRight.setBackgroundResource(R.color.transparent);
                        this.ivRight.setImageResource(R.drawable.ic_search_msg_changed);
                        this.tvRight.setTextColor(getResources().getColor(R.color.ic_text_changed));
                    }
                    break;
                case R.styleable.HomeSearbar_hsBackground:
                    this.llContainer.setBackgroundResource(attributes.getResourceId(itemId,R.color.transparent));
                    break;
            }
        }
    }

    private void init() {
        llContainer = findViewById(R.id.home_search_bar_container);
        llLeftBg = findViewById(R.id.home_search_bar_left_bg);
        ivLeft = findViewById(R.id.home_search_bar_scan);
        tvLeft = findViewById(R.id.home_search_bar_scan_text);
        llSearchText = findViewById(R.id.home_search_bar_textView);
        ivSearch = findViewById(R.id.home_search_bar_search);
        tvSearch = findViewById(R.id.home_search_bar_text);
        llRight = findViewById(R.id.home_search_bar_right_bg);
        ivRight = findViewById(R.id.home_search_bar_msg);
        tvRight = findViewById(R.id.home_search_bar_msg_text);




    }

    public void setHsBackground(int color) {

        this.llContainer.setBackgroundColor(color);
        invalidate();
    }

    public boolean isChangeType() {
        return changeType;
    }

    public void setChangeType(boolean changeType) {
        this.changeType = changeType;
        if (changeType) {
            this.llLeftBg.setBackgroundResource(R.color.transparent);
            this.ivLeft.setImageResource(R.drawable.ic_search_scan_changed);
            this.tvLeft.setTextColor(getResources().getColor(R.color.ic_text_changed));
            this.llSearchText.setBackgroundResource(R.drawable.home_title_bar_search_corner_bg_changed);
            this.ivSearch.setImageResource(R.drawable.ic_search_changed);
            this.tvSearch.setTextColor(getResources().getColor(R.color.white));
            this.llRight.setBackgroundResource(R.color.transparent);
            this.ivRight.setImageResource(R.drawable.ic_search_msg_changed);
            this.tvRight.setTextColor(getResources().getColor(R.color.ic_text_changed));
            invalidate();
        }else{
            this.llLeftBg.setBackgroundResource(R.drawable.shape_circle);
            this.ivLeft.setImageResource(R.drawable.ic_search_scan_);
            this.tvLeft.setTextColor(getResources().getColor(R.color.white));
            this.llSearchText.setBackgroundResource(R.drawable.home_title_bar_search_corner_bg);
            this.ivSearch.setImageResource(R.drawable.ic_search);
            this.tvSearch.setTextColor(getResources().getColor(R.color.search_bg));
            this.llRight.setBackgroundResource(R.drawable.shape_circle);
            this.ivRight.setImageResource(R.drawable.ic_search_msg);
            this.tvRight.setTextColor(getResources().getColor(R.color.white));
            invalidate();
        }

    }
}

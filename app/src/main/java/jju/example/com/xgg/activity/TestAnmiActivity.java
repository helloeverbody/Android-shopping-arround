package jju.example.com.xgg.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jju.example.com.xgg.R;

public class TestAnmiActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewGroup anim_mask_layout;
    private ImageView ivCart;
    private Button btnTest;
    private Handler mHanlder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_anmi);


        anim_mask_layout = (RelativeLayout) findViewById(R.id.test_anmi_ll_container);
        ivCart = findViewById(R.id.test_anmi_iv_cart);
        btnTest = findViewById(R.id.test_anmi_btn_test);
        mHanlder = new Handler(getMainLooper());


        btnTest.setOnClickListener(this);
    }




    /**
     * 购物车添加动画
     * @param start_location
     */
    public void playAnimation(int[] start_location){
        ImageView img = new ImageView(this);
        img.setImageResource(R.drawable.ic_add_to_cart);
        img.getMeasuredWidth();
        setAnim(img,start_location);
    }

    private Animation createAnim(int startX, int startY){
        int[] des = new int[2];
        ivCart.getLocationInWindow(des);

        int ivCartWidth1 = ivCart.getMeasuredWidth();
        int ivCartWidth2 = ivCart.getWidth();
        int ivCartHeight = ivCart.getHeight();
        Log.i("购物车宽度", "createAnim: "+ivCartWidth1);
        Log.i("购物车宽度", "createAnim: "+ivCartHeight);
        Log.i("x轴距离", "createAnim: "+Integer.toString(des[0]-startX));
        Log.i("商品详情页面", "createAnim: "+des[0] + " " + des[1]);

        AnimationSet set = new AnimationSet(false);

        Animation translationX = new TranslateAnimation(0, des[0]-startX+ivCartWidth1/2-30, 0, 0);
        translationX.setInterpolator(new LinearInterpolator());
        Animation translationY = new TranslateAnimation(0, 0, 0, des[1]-startY);
        translationY.setInterpolator(new AccelerateInterpolator());
        Animation alpha = new AlphaAnimation(1,0.5f);
        set.addAnimation(translationX);
        set.addAnimation(translationY);
        set.addAnimation(alpha);
        set.setDuration(800);

        return set;
    }

    private void addViewToAnimLayout(final ViewGroup vg, final View view,
                                     int[] location) {

        int x = location[0];
        int y = location[1];
        int[] loc = new int[2];
        vg.getLocationInWindow(loc);
        view.setX(x);
        view.setY(y-loc[1]);

        vg.addView(view);
    }
    private void setAnim(final View v, int[] start_location) {

        addViewToAnimLayout(anim_mask_layout, v, start_location);
        Animation set = createAnim(start_location[0],start_location[1]);
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(final Animation animation) {
                v.setVisibility(View.GONE);
                mHanlder.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        anim_mask_layout.removeView(v);
                    }
                },100);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        v.startAnimation(set);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_anmi_btn_test:
                int[] loc = new int[2];
                v.getLocationInWindow(loc);
                Log.i("商品列表适配器", "onClick: "+loc[0]+" "+loc[1]);
                playAnimation(loc);
                break;
        }
    }
}

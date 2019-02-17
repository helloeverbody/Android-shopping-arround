package jju.example.com.xgg.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.github.zackratos.ultimatebar.UltimateBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.fragment.ClassifyFragment;
import jju.example.com.xgg.fragment.FindFragment;
import jju.example.com.xgg.fragment.HomeFragment;
import jju.example.com.xgg.fragment.MineFragment;
import jju.example.com.xgg.fragment.OrderFragment;
import jju.example.com.xgg.fragment.ShopCartFragment;
import jju.example.com.xgg.util.MessageEvent;
import jju.example.com.xgg.util.MessageEvent1;
import jju.example.com.xgg.util.SPUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RESULT_SUCCESS = 0;     // 成功的结果码
    private static final int RESULT_FAIL = -1;       // 失败的结果码
    private static final int REQUEST_MAIN = 1;       // 失败的结果码
    private static final String TAG = "首页MAIN页面";
    private Boolean[] isStatDark = new Boolean[]{
            true,
            false,
            false,
            false,
            true
    };
    private int homeStatAlpha ;
    private RadioButton rbHome,rbClassify,rbFind,rbShopCart,rbMine;     //底部导航栏按钮
    private List<Fragment> fgList;       //装载Fragment的集合
    private int currPosition = 0;        //当前fragment页面
    private FragmentManager fm;          //fragment管理器
    private String[] tags = new String[]{
            "HomeFragment",
            "ClassifyFragment",
            "FindFragment",
            //"ShopCartFragment",
            "OrderFragment",
            "MineFragment",
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        EventBus.getDefault().register(this);   // 注册EventBus
        setContentView(R.layout.activity_main);
        init();
        initFragment();
    }

    private void init() {

        rbHome = findViewById(R.id.rb_home);
        rbClassify = findViewById(R.id.rb_classify);
        rbFind = findViewById(R.id.rb_find);
        rbShopCart = findViewById(R.id.rb_shopping_cart);
        rbMine = findViewById(R.id.rb_mine);

        rbHome.setOnClickListener(this);
        rbClassify.setOnClickListener(this);
        rbFind.setOnClickListener(this);
        rbShopCart.setOnClickListener(this);
        rbMine.setOnClickListener(this);
    }

    private void initFragment() {
        fgList = new ArrayList<>();

        fgList.add(new HomeFragment());
        fgList.add(new ClassifyFragment());
        fgList.add(new FindFragment());
        //fgList.add(new ShopCartFragment());
        fgList.add(new OrderFragment());
        fgList.add(new MineFragment());
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction(); // 实例化Fragment事物管理器
        for (int i = 0; i < fgList.size(); i++) {       // 将5个Fragment装在容器中，交给ft管理
            ft.add(R.id.fl_container, fgList.get(i), tags[i]);
        }
        ft.commit();    // 提交保存
        showFragment(0);    // 默认显示首页Fragment

    }

    private void showFragment(int i) {

        hideFragments();    // 先隐藏所有Fragment
        FragmentTransaction ft = fm.beginTransaction();
        ft.show(fgList.get(i));     // 显示Fragment
        //ft.commit();
        ft.commitAllowingStateLoss();
    }

    private void hideFragments() {

        FragmentTransaction ft = fm.beginTransaction();
        for (Fragment fg : fgList) {
            ft.hide(fg);        // 隐藏所有Fragment
        }
        //ft.commit();
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.rb_home:            //选择首页
                showFragment(0);
                initStatus(0);
                clickAgain(0);
                break;
            case R.id.rb_classify:        //选择分类
                showFragment(1);
                initStatus(1);
                clickAgain(1);
                break;
            case R.id.rb_find:            //选择发现
                showFragment(2);
                initStatus(2);
                clickAgain(2);
                break;
            case R.id.rb_shopping_cart:   //选择订单
                if(!SPUtil.isLogin(this)){
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    intent.putExtra("isShow",true);
                    startActivityForResult(intent,REQUEST_MAIN);     //跳转到登录
                }
                showFragment(3);
                initStatus(3);
                clickAgain(3);
                break;
            case R.id.rb_mine:            //选择我的
                if(SPUtil.isLogin(this)){
                    clickAgain(4);
                }
                showFragment(4);
                initStatus(4);
                clickAgain(4);
                break;
        }

    }

    private void clickAgain(int i) {
        if(currPosition == i){      // 当前显示为某页面，并再次点击了此页面的导航按钮
            EventBus.getDefault().post(new MessageEvent(tags[i]));  // 通知Fragment刷新
        }
        currPosition = i;
    }

    private void initStatus(int currPosition) {



        if(Build.VERSION.SDK_INT < 21)  return;     //版本不适用直接返回

        switch (currPosition){
            case 0:
                if(isStatDark[currPosition]){
                    View decorView = this.getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }else{
                    View decorView = this.getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    this.getWindow().setStatusBarColor(Color.argb(homeStatAlpha, 255, 255, 255));
                }
                break;

            case 1:
            case 2:
            case 3:
            case 4:

                if(isStatDark[currPosition]){
                    View decorView = this.getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }else{
                    View decorView = this.getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    this.getWindow().setStatusBarColor(Color.TRANSPARENT);
                }
                break;

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this); // 注销EventBus
    }

    // 通过EventBus接收Activity的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent1 event) {

        isStatDark[0] = event.isTure();
        homeStatAlpha = event.getAlpha();
        Log.i("#######################", "homeStatAlpha="+homeStatAlpha);

    }

    // 通过EventBus接收Activity的消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {


        if (event.getBundle() ==null) return;
        int currentClassify = event.getBundle().getInt("currentClassify",-1);
        switch (currentClassify){
            case 0:
            case 1:
            case 2:
            case 3:
                rbClassify.setChecked(true);
                showFragment(1);
                initStatus(1);
                clickAgain(1);
                break;
            default:
                break;
        }


    }

    @Override
    public void onBackPressed() {
        if(currPosition == 1){
            rbHome.setChecked(true);
            showFragment(0);
            initStatus(0);
            clickAgain(0);
        }else{
            super.onBackPressed();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_MAIN && resultCode == RESULT_SUCCESS){
            EventBus.getDefault().post(new MessageEvent("OrderFragment"));  // 通知Fragment刷新
        }

    }
}

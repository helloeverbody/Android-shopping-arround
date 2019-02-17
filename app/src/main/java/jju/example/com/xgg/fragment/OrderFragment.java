package jju.example.com.xgg.fragment;


import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.OrderFragmentAdapter;
import jju.example.com.xgg.fragment.order.OrderEvaluateFragment;
import jju.example.com.xgg.fragment.order.OrderListFragment;
import jju.example.com.xgg.util.MessageEvent;
import jju.example.com.xgg.view.NoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends BaseFragment {

    private static final String TAG = "首页订单页面";
    private String[] titles = new String[]{"全部订单","待评价"};
    private PagerSlidingTabStrip pstsOrder;  //滑动分类tap
    private NoScrollViewPager nsvpOrder;
    private List<Fragment> fragments;
    public OrderFragmentAdapter orderFragmentAdapter;
    public OrderListFragment orderListPag;
    public OrderEvaluateFragment orderEvaluatePage;

    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        initView(view);
        loadData();     //加载数据
        return view;
    }



    private void initView(View view) {
        pstsOrder = view.findViewById(R.id.order_psts_title_tabs);        //滑动标签页
        nsvpOrder = view.findViewById(R.id.order_nsvp_viewpager_order);                    //视图翻页
    }

    /**
     * 加载数据
     */
    private void loadData() {
        fragments = new ArrayList<>();
        orderListPag = new OrderListFragment();
        orderEvaluatePage = new OrderEvaluateFragment();
        for (int i = 0; i < titles.length; i++) {
            OrderListFragment fragment = new OrderListFragment();
            Bundle bundle = new Bundle();
            bundle.putString("title",titles[i]);
            fragment.setArguments(bundle);
            fragments.add(fragment);
        }
        //创建Fragment的 ViewPager 自定义适配器
        orderFragmentAdapter = new OrderFragmentAdapter(getChildFragmentManager());
        orderFragmentAdapter.setData(titles, fragments);   //设置适配器数据
        nsvpOrder.setAdapter(orderFragmentAdapter);
        nsvpOrder.setOffscreenPageLimit(4);
        pstsOrder.setViewPager(nsvpOrder);

    }






    public  float dip2px(float dipValue)
    {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return  (dipValue * scale + 0.5f);
    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        switch (event.getMsg()){
            case "OrderFragment":
                Log.i(TAG, "onMessageEvent: 再次点击订单");
                nsvpOrder.setCurrentItem(0);
                loadData();
                break;
        }
    }
}

package jju.example.com.xgg.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gxz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.SearchActivity;
import jju.example.com.xgg.adapter.ItemTitlePagerAdapter;
import jju.example.com.xgg.fragment.classifypage.ClassifyPageFragment1;
import jju.example.com.xgg.fragment.classifypage.ClassifyPageFragment2;
import jju.example.com.xgg.fragment.classifypage.ClassifyPageFragment3;
import jju.example.com.xgg.fragment.classifypage.ClassifyPageFragment4;
import jju.example.com.xgg.util.MessageEvent;
import jju.example.com.xgg.view.HomeSearbar;
import jju.example.com.xgg.view.NoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClassifyFragment extends BaseFragment implements View.OnClickListener {

    private HomeSearbar hsSearBar;      //自定义搜索框控件
    private TextView tvSearch;          //搜索框
    private PagerSlidingTabStrip ptClassTitle;  //滑动分类tap
    private NoScrollViewPager viewPager;        //fragment翻页

    private List<Fragment> classifyPageFragments = new ArrayList<>();

    private Fragment fragment = new Fragment();

    private String[] titles = new String[]{
            "水果",
            "蔬菜",
            "饮料",
            "酒水"
    };







    public ClassifyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadData();
    }

    private void loadData() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_classify, container, false);

        //获取新建视图View中布局文件的空间
        initView(view);




        return view;



    }



    private void initView(View view) {

        hsSearBar = view.findViewById(R.id.hs_searchbar_classify);
        tvSearch = view.findViewById(R.id.home_search_bar_text);    //搜索框



        /*classifyPageFragments.add(new ClassifyPageFragment1());
        classifyPageFragments.add(new ClassifyPageFragment2());
        classifyPageFragments.add(new ClassifyPageFragment3());
        classifyPageFragments.add(new ClassifyPageFragment4());*/

        for(int i=0; i<4; i++){

            ClassifyPageFragment1 classifyPageFragment1 = new ClassifyPageFragment1();
            Bundle bundle = new Bundle();
            bundle.putInt("classifyId",i+1);
            classifyPageFragment1.setArguments(bundle);
            classifyPageFragments.add(classifyPageFragment1);
        }


        viewPager = view.findViewById(R.id.vp_viewpager_classify);
        ptClassTitle = view.findViewById(R.id.title_tabs);

        viewPager.setAdapter(new ItemTitlePagerAdapter(getActivity().getSupportFragmentManager(),classifyPageFragments,titles));

        viewPager.setOffscreenPageLimit(4);
        ptClassTitle.setViewPager(viewPager);


        hsSearBar.setChangeType(true);     //设置搜索框为白底

        tvSearch.setOnClickListener(this);  //点击搜索框



    }

    @Override
    public void onMessageEvent(MessageEvent event) {
        super.onMessageEvent(event);
        if (event.getBundle() ==null) return;
        int currentClassify = event.getBundle().getInt("currentClassify",-1);
        switch (currentClassify){
            case 0:
            case 1:
            case 2:
            case 3:
                viewPager.setCurrentItem(currentClassify);
                break;
            default:
                break;
        }


    }

    /**
     * 页面控件点击
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home_search_bar_text:     //点击搜索框
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);          //跳转搜索页面
                break;
        }
    }
}

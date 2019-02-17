package jju.example.com.xgg.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.adapter.ItemActivityAdapter;
import jju.example.com.xgg.pojo.ShopActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends BaseFragment {

    private RecyclerView rvShopActivitys;
    private ItemActivityAdapter itemActivityAdapter;
    private List<ShopActivity> shopActivities = new ArrayList<>();

    public FindFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_find, container, false);
        initView(view);
        initRecyclerView();
        return view;
    }

    private void initRecyclerView() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        //设置列表的排列方向
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //配置LinearLayoutManger
        rvShopActivitys.setLayoutManager(layoutManager);

        itemActivityAdapter = new ItemActivityAdapter(getContext(),getShopActivitys());
        rvShopActivitys.setAdapter(itemActivityAdapter);
    }
    /**
     * 测试，待删除
     * @return
     */
    private List<ShopActivity> getShopActivitys() {
        ShopActivity shopActivity = new ShopActivity();
        shopActivity.setActi_name("新鲜水果");

        for(int i=0; i<5; i++){
            shopActivities.add(shopActivity);
        }
        return shopActivities;
    }

    private void initView(View view) {

        rvShopActivitys = view.findViewById(R.id.rv_activity_find);

    }



}

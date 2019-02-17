package jju.example.com.xgg.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by lisen on 2018/6/20.
 */

public class OrderFragmentAdapter extends FragmentPagerAdapter{

    private String[] titles;
    private List<Fragment> fragments;

    public OrderFragmentAdapter(FragmentManager fm){
        super(fm);
    }

    public void setData(String[] titles,List<Fragment> fragments){
        this.titles = titles;
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    public Object instantiateItem(ViewGroup container,int position){
        Fragment fragment = null;
        try {
            fragment = (Fragment) super.instantiateItem(container,position);
        }catch (Exception e){

        }return  fragment;
    }
    public void destroyItem(ViewGroup container,int position,Object object){
        super.destroyItem(container,position,object);
    }


    public CharSequence getPageTitle(int position){
        return  titles[position];
    }
}

package jju.example.com.xgg.fragment.search;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import jju.example.com.xgg.R;
import jju.example.com.xgg.activity.SearchActivity;

public class SearchHistoryFragment extends Fragment {
    private RecyclerView rvSearchHistory;   //搜索历史列表
    private SearchHistoryAdapter searchHistoryAdapter;    //搜索历史适配器
    private List<String> searchHistoryList = new ArrayList<>(); //搜索历史集合



    public SearchHistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search_history, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rvSearchHistory = view.findViewById(R.id.search_rv_search_history);  //搜索历史列表

    }


    //初始化搜索历史适配器
    private void initRvSearchHistory() {
        searchHistoryAdapter = new SearchHistoryAdapter(R.layout.item_search_history_layout,searchHistoryList);     //搜索历史适配器
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),120);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup(){

            @Override
            public int getSpanSize(int position) {

                /**
                 * 重点在这里，这个getSpanSize方法的返回值其实指的是单个item所占用的我们设置的每行的item个数
                 */
                int stringLenth = searchHistoryList.get(position).length();
                int spanSize = 0;

                switch (stringLenth){
                    case 1:
                        spanSize = 15;
                        break;
                    case 2:
                        spanSize = 20;
                        break;
                    case 3:
                    case 4:
                        spanSize = 24;
                        break;
                    case 5:
                        spanSize = 30;
                        break;
                    case 6:
                    case 7:
                        spanSize = 40;
                        break;
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                        spanSize = 60;
                        break;
                    default:
                        spanSize = 120;
                        break;


                }

                return spanSize;
            }
        });
        rvSearchHistory.setAdapter(searchHistoryAdapter);
        rvSearchHistory.setLayoutManager(gridLayoutManager);

    }


    /**
     * 搜索历史Item适配器快速构造
     */
    public class SearchHistoryAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
        public SearchHistoryAdapter(int layoutResId, List data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.ishl_tv_search_history,item) ;    //搜索历史
        }
    }

}

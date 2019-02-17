package jju.example.com.xgg.fragment.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jju.example.com.xgg.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchListFragment extends Fragment {


    public SearchListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_list, container, false);
    }

}

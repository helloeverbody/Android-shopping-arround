package jju.example.com.xgg.fragment.order;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jju.example.com.xgg.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderEvaluateFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderEvaluateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderEvaluateFragment extends Fragment {

    public OrderEvaluateFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_evaluate, container, false);
    }

}

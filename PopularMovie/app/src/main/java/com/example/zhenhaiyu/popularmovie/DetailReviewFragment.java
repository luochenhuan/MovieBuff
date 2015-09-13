package com.example.zhenhaiyu.popularmovie;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.example.zhenhaiyu.popularmovie.model.Review;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class DetailReviewFragment  extends Fragment {
    private final String LOG_TAG = DetailReviewFragment.class.getSimpleName();
    public static final String ARG_REVIEW = "ARG_REVIEW";
    private List<Review> mReviews;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailReviewFragment newInstance(List<Review> reviews) {
        DetailReviewFragment fragment = new DetailReviewFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_REVIEW, (ArrayList<Review>)reviews);
        fragment.setArguments(args);
        return fragment;
    }

    public DetailReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mReviews = getArguments().getParcelableArrayList(ARG_REVIEW);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_review, container, false);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }
}

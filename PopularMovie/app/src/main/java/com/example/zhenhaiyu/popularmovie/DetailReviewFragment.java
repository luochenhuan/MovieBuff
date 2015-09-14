package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhenhaiyu.popularmovie.api.MoviesAPI;
import com.example.zhenhaiyu.popularmovie.api.MoviesReqService;
import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.example.zhenhaiyu.popularmovie.model.Review;
import com.example.zhenhaiyu.popularmovie.model.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 *
 */
public class DetailReviewFragment  extends Fragment {
    private final String LOG_TAG = DetailReviewFragment.class.getSimpleName();
    public static final String ARG_REVIEW = "ARG_REVIEW";
    private List<Review> mReviews;
    private Movie mMovie;
    private ReviewAdapter mReviewAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    MoviesReqService mMoviesReqService;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailReviewFragment newInstance(Movie movie) {
        DetailReviewFragment fragment = new DetailReviewFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_REVIEW, movie);
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
            mMovie = getArguments().getParcelable(ARG_REVIEW);
        }
        mReviewAdapter = new ReviewAdapter(getContext(), new ArrayList<Review>());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView: ");

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_review, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.review_recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mReviewAdapter);

        if (networkConnected() == true) {
            mMoviesReqService = MoviesReqService.getInstance(getContext());
            fetchReviews(mMovie.id);
        }
        else {
            snackbarInform();
        }
        return rootView;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void fetchReviews(long id){
        // Create an HTTP client that uses a cache on the file system.
//        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(LOG_TAG, "fetchReviews: " + id);
        MoviesAPI api = mMoviesReqService.getMoviesReqService();
        api.getReviews(id, new Callback<ReviewResponse>() {
            @Override
            public void success(ReviewResponse reviewResponse, Response response) {
                // handle response here
                mReviews = reviewResponse.getReviews();
//                if (mReviews == null || mReviews.size() == 0){
//                    Log.d(LOG_TAG, "mReviews: null ");
//                }
//                else {
                    Log.d(LOG_TAG, "mPosterAdapter update");
                    mReviewAdapter.updateAllData(mReviews);
                    mReviewAdapter.notifyDataSetChanged();
//                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed!", error);
                Toast.makeText(getActivity(), "fetchMovies failed", Toast.LENGTH_LONG).show();
            }
        });

    }


    private boolean networkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void snackbarInform(){
        Snackbar.make(getActivity().findViewById(R.id.main_frag_container),
                "Woops! Seems we lost network connection ...", Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}

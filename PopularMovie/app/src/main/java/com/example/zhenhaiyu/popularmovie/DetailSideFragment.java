package com.example.zhenhaiyu.popularmovie;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhenhaiyu.popularmovie.api.MoviesAPI;
import com.example.zhenhaiyu.popularmovie.api.MoviesReqService;
import com.example.zhenhaiyu.popularmovie.data.MovieContract;
import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.example.zhenhaiyu.popularmovie.model.MovieListResponse;
import com.example.zhenhaiyu.popularmovie.model.Review;
import com.example.zhenhaiyu.popularmovie.model.ReviewResponse;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class DetailSideFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie mMovie;
    private List<Review> mReviews;
    private ReviewAdapter mReviewAdapter;

    MoviesReqService mMoviesReqService;


    @Bind(R.id.detail_content)
    CoordinatorLayout layoutRoot;
    @Bind((R.id.collapsing_toolbar))
    CollapsingToolbarLayout mCollapsingToolbar;
    @Bind(R.id.favorite_fab)
    FloatingActionButton mFavoriteFab;

    @Bind(R.id.tv_release) TextView release;
    @Bind(R.id.rb_rate_detail) RatingBar rbRate;
    @Bind(R.id.expand_text_view) ExpandableTextView expOverviewTv;
    @Bind(R.id.iv_backdrop)
    ImageView imageView;
    @Bind(R.id.tv_rate_detail) TextView tvRate;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }

        if (networkConnected() == true) {
            mMoviesReqService = MoviesReqService.getInstance(getContext());
            fetchReviews(mMovie.id);
        }
        else {
            snackbarInform();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail_side, container, false);
        ButterKnife.bind(getContext(), rootView);

        mCollapsingToolbar.setTitle(mMovie.originalTitle);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.DetailHeader_TextStyle);

        final ImageView imageView = (ImageView)rootView.findViewById(R.id.iv_backdrop);
        if (mMovie.backdropPath != null) {
            String posterURL = getString(R.string.img_base_url) +"w780/" + mMovie.backdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(getContext())
                    .load(posterURL)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }

        loadView();
        return rootView;
    }

    private void loadView() {
        Log.d(LOG_TAG, mMovie.releaseDate);
        if (!mMovie.releaseDate.equals("null"))
            release.setText("Release : " + mMovie.releaseDate);
        else
            release.setText("No Release Date in database");
        // rating
        float rateStar = (float)mMovie.avgVote/2;
        rbRate.setRating(rateStar);

        String rateTxt = mMovie.avgVote + "/10";
        tvRate.setText(rateTxt);

        //overview
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        if (!mMovie.overview.equals("null"))
            expOverviewTv.setText(mMovie.overview);
        else
            expOverviewTv.setText("");

        if (mMovie.backdropPath != null) {
            String posterURL = getString(R.string.img_base_url) +"w780/" + mMovie.backdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(getContext())
                    .load(posterURL)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }
    }

    @OnClick(R.id.favorite_fab)
    public void OnClick(View view) {
        int oldFav = toggleFav();
        String snackInfo = "";
        switch (oldFav) {
            case 0:
                snackInfo = "add to Favorites";
                break;
            case 1:
                snackInfo = "remove from Favorites";
                break;
        }
        Snackbar.make(layoutRoot, snackInfo, Snackbar.LENGTH_SHORT)
                .show();
    }

    public int toggleFav(){
        int isFav = 0;
        Cursor movieCursor = getContext().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_FAVORITE},
                MovieContract.MovieEntry.WHERE_CLAUSE_MOVIE_ID,
                new String[]{"" + mMovie.id},
                null,
                null);

        if (movieCursor.moveToFirst()) {
            isFav = movieCursor.getInt(movieCursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_FAVORITE));
            ContentValues favValues = new ContentValues();
            switch (isFav){
                case 0:
                    favValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
                    getContext().getContentResolver().update(
                            MovieContract.MovieEntry.buildUri(mMovie.id),
                            favValues,
                            MovieContract.MovieEntry.WHERE_CLAUSE_MOVIE_ID,
                            new String[]{"" + mMovie.id}
                    );
                    mFavoriteFab.setImageResource(R.drawable.ic_favorite_white_24dp);
                    break;
                case 1:
                    favValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 0);
                    getContext().getContentResolver().update(
                            MovieContract.MovieEntry.buildUri(mMovie.id),
                            favValues,
                            MovieContract.MovieEntry.WHERE_CLAUSE_MOVIE_ID,
                            new String[]{"" + mMovie.id}
                    );
                    mFavoriteFab.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    break;
            }
        }
        movieCursor.close();

        return isFav;
    }

    private void fetchReviews(long id){
        // Create an HTTP client that uses a cache on the file system.
//        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(LOG_TAG, "fetchReviews:");
        MoviesAPI api = mMoviesReqService.getMoviesReqService();
        api.getReviews(id, new Callback<ReviewResponse>() {
            @Override
            public void success(ReviewResponse reviewResponse, Response response) {
                // handle response here
                mReviews = reviewResponse.getReviews();
                mReviewAdapter.updateAllData(mReviews);
                Log.d(LOG_TAG, "mPosterAdapter update");
                mReviewAdapter.notifyDataSetChanged();
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

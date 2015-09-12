package com.example.zhenhaiyu.popularmovie;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private final String KEY_MOVIE = "saved_movie";

    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        if (savedInstanceState != null) {
            mMovie = savedInstanceState.getParcelable(KEY_MOVIE);
            Log.d(LOG_TAG, "savedInstanceState: " + mMovie.title);

        } else {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra(getResources().getString(R.string.title_activity_detail))) {
                mMovie = intent.getParcelableExtra(getResources().getString(R.string.title_activity_detail));
                Log.d(LOG_TAG, "get Intent: " + mMovie.title);
            }
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mMovie.originalTitle);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.DetailHeader_TextStyle);

        loadView();
    }

    private void loadView() {
//        TextView release = (TextView) findViewById(R.id.tv_release);
//        Log.d(LOG_TAG, mMovie.releaseDate);
//        if (!mMovie.mReleaseDate.equals("null"))
//            release.setText("Release : " + mMovie.mReleaseDate);
//        else
//            release.setText("No Release Date in database");
//        // rating
//        RatingBar rbRate = (RatingBar) findViewById(R.id.rb_rate_detail);
//        float rateStar = (float)mMovie.mAvgVote/2;
//        rbRate.setRating(rateStar);
//        TextView tvRate = (TextView) findViewById(R.id.tv_rate_detail);
//        String rateTxt = mMovie.mAvgVote + "/10";
//        tvRate.setText(rateTxt);
//
//        //overview
//        ExpandableTextView expOverviewTv = (ExpandableTextView) findViewById(R.id.expand_text_view);
//        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
//        if (!mMovie.mOverView.equals("null"))
//            expOverviewTv.setText(mMovie.mOverView);
//        else
//            expOverviewTv.setText("");
//
//        final ImageView imageView = (ImageView) findViewById(R.id.iv_backdrop);
//        if (mMovie.mBackdropPath != null) {
//            String posterURL = getString(R.string.img_base_url) +"w780/" + mMovie.mBackdropPath;
//            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
//            Picasso.with(this)
//                    .load(posterURL)
//                    .placeholder(R.drawable.img_placeholder)
//                    .error(R.drawable.movie_placeholder_error)
//                    .resize(backdropSize, 0)
//                    .into(imageView);
//        } else{
//            imageView.setImageResource(R.drawable.movie_placeholder_error);
//        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putParcelable(KEY_MOVIE, mMovie);
        super.onSaveInstanceState(outState);
    }

}

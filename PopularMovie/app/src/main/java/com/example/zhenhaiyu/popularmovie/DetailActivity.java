package com.example.zhenhaiyu.popularmovie;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private final String KEY_MOVIE = "saved_movie";

    @Bind(R.id.detail_content) CoordinatorLayout layoutRoot;
    @Bind(R.id.detail_toolbar) Toolbar mToolbar;
    @Bind((R.id.collapsing_toolbar)) CollapsingToolbarLayout mCollapsingToolbar;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

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

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mCollapsingToolbar.setTitle(mMovie.originalTitle);
//        collapsingToolbar.setExpandedTitleTextAppearance(R.style.DetailHeader_TextStyle);

        loadView();

        setupFAB();
    }

    private void loadView() {
        TextView release = (TextView) findViewById(R.id.tv_release);
        Log.d(LOG_TAG, mMovie.releaseDate);
        if (!mMovie.releaseDate.equals("null"))
            release.setText("Release : " + mMovie.releaseDate);
        else
            release.setText("No Release Date in database");
        // rating
        RatingBar rbRate = (RatingBar) findViewById(R.id.rb_rate_detail);
        float rateStar = (float)mMovie.avgVote/2;
        rbRate.setRating(rateStar);
        TextView tvRate = (TextView) findViewById(R.id.tv_rate_detail);
        String rateTxt = mMovie.avgVote + "/10";
        tvRate.setText(rateTxt);

        //overview
        ExpandableTextView expOverviewTv = (ExpandableTextView) findViewById(R.id.expand_text_view);
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        if (!mMovie.overview.equals("null"))
            expOverviewTv.setText(mMovie.overview);
        else
            expOverviewTv.setText("");

        final ImageView imageView = (ImageView) findViewById(R.id.iv_backdrop);
        if (mMovie.backdropPath != null) {
            String posterURL = getString(R.string.img_base_url) +"w780/" + mMovie.backdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(this)
                    .load(posterURL)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }
    }

    public void setupFAB(){

    }

    @OnClick(R.id.favorite_fab)
    public void toggleFav(View view) {
        Snackbar.make(layoutRoot, "Hey, I'm SnackBar!", Snackbar.LENGTH_SHORT)
                .setAction("Undo", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(DetailActivity.this, "Undo!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putParcelable(KEY_MOVIE, mMovie);
        super.onSaveInstanceState(outState);
    }

}

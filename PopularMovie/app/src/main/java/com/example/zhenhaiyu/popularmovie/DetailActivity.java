package com.example.zhenhaiyu.popularmovie;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private final String LOG_TAG = DetailActivity.class.getSimpleName();
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(getResources().getString(R.string.title_activity_detail))) {
            mMovie = intent.getParcelableExtra(getResources().getString(R.string.title_activity_detail));
            Log.d(LOG_TAG, "artist.id " + mMovie.id);
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(mMovie.mTitle);

        loadView();
    }

    private void loadView() {
        final ImageView imageView = (ImageView) findViewById(R.id.iv_backdrop);
        if (mMovie.mBackdropPath != null) {
            String posterURL = getString(R.string.img_base_url) + mMovie.mBackdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(this)
                    .load(posterURL)
                    .placeholder(R.drawable.sample_0)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }
    }

}

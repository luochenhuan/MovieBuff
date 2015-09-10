package com.example.zhenhaiyu.popularmovie;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;
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
            Log.d(LOG_TAG, "mMovie.id " + mMovie.id);
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
        TextView release = (TextView) findViewById(R.id.tv_release);
        release.setText("Release Date: " + mMovie.mReleaseDate);

        // rating
        RatingBar rbRate = (RatingBar) findViewById(R.id.rb_rate_detail);
        float rateStar = (float)mMovie.mAvgVote/2;
        rbRate.setRating(rateStar);
        TextView tvRate = (TextView) findViewById(R.id.tv_rate_detail);
        String rateTxt = mMovie.mAvgVote + "/10";
        tvRate.setText(rateTxt);

        //overview
        ExpandableTextView expOverviewTv = (ExpandableTextView) findViewById(R.id.expand_text_view);
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        expOverviewTv.setText(mMovie.mOverView);

//        final TextView overview = (TextView) findViewById(R.id.tv_overview);
//        overview.setText( mMovie.mOverView);
//        overview.setVisibility(View.GONE);

//        final TextView showAll = (TextView) findViewById(R.id.overview_detail_read_all);
//        showAll.setText( mMovie.mOverView);
//        showAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                showAll.setVisibility(View.INVISIBLE);
//                overview.setMaxLines(Integer.MAX_VALUE);
//            }
//        });

        final ImageView imageView = (ImageView) findViewById(R.id.iv_backdrop);
        if (mMovie.mBackdropPath != null) {
            String posterURL = getString(R.string.img_base_url) + mMovie.mBackdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(this)
                    .load(posterURL)
                    .placeholder(R.drawable.movie_placeholder_error)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }
    }

}

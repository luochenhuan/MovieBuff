package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhenhaiyu on 2015-08-24.
 */
public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.PosterViewHolder> {
    private List<Movie> mMovies;
    private final Activity mContext;

    public MoviePosterAdapter(Activity context, List<Movie> movies) {
        mMovies = movies;
        mContext = context;
    }

    public void updateAllData(List<Movie> movies){
        mMovies.clear();
        mMovies.addAll(movies);
    }
    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        protected CardView cv;
        protected TextView vTitle;
        protected RatingBar vRatingBar;
        protected TextView vRate;
        protected ImageView vPoster;

        public PosterViewHolder(View posterView) {
            super(posterView);
            cv = (CardView) posterView.findViewById(R.id.cv_main);
            vPoster = (ImageView) posterView.findViewById(R.id.iv_poster);
            vTitle = (TextView) posterView.findViewById(R.id.tv_poster_title);
            vRatingBar = (RatingBar) posterView.findViewById(R.id.rb_rate);
            vRate = (TextView) posterView.findViewById(R.id.tv_rate);

        }
    }

    @Override
    public int getItemCount(){
        return mMovies.size();
    }

    public Movie getItem(int position){
        return mMovies.get(position);
    }


    @Override
    public PosterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.poster_gridlayout, viewGroup, false);

        return new PosterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder posterViewHolder, int i) {
        Movie movie = mMovies.get(i);

        ImageView imageView = posterViewHolder.vPoster;
        if (movie.mPosterPath != null) {
            String posterURL = mContext.getString(R.string.img_base_url) + movie.mPosterPath;
            int posterSize = (int) mContext.getResources().getDimension(R.dimen.poster_size);
            Picasso.with(mContext)
                    .load(posterURL)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.artist_placeholder_error)
                    .resize(posterSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.artist_placeholder_error);
        }

        posterViewHolder.vTitle.setText(movie.mOriginalTitle);

        float rateStar = (float)movie.mAvgVote/2;
        posterViewHolder.vRatingBar.setRating(rateStar);

        String rateTxt = movie.mAvgVote + "/10";
        posterViewHolder.vRate.setText(rateTxt);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

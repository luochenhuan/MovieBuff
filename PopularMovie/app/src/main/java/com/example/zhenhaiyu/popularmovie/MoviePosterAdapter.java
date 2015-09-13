package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhenhaiyu.popularmovie.data.MovieContract;
import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/* good ref: http://andraskindler.com/blog/2014/migrating-to-recyclerview-from-listview/
* */
public class MoviePosterAdapter extends RecyclerView.Adapter<MoviePosterAdapter.PosterViewHolder> {
    private final String LOG_TAG = MoviePosterAdapter.class.getSimpleName();
    private final Context mContext;
    private List<Movie> mMovies;
    private static RecyclerViewClickListener mItemListener;

    public MoviePosterAdapter(Context context, RecyclerViewClickListener itemListener, List<Movie> movies) {
        mMovies = movies;
        mContext = context;
        mItemListener = itemListener;
    }

    public void updateAllData(List<Movie> movies){
        mMovies.clear();
        mMovies.addAll(movies);
        bulkUpdate();
    }

    public void bulkUpdate(){
        Cursor movieCursor = null;
        for (int i = 0; i < mMovies.size(); i++){
            Movie movie = mMovies.get(i);
            ContentValues movieValues = movie.getContentValues();

            // First, check if the location with this city name exists in the db
            movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                null,
                MovieContract.MovieEntry.WHERE_CLAUSE_MOVIE_ID,
                new String[]{""+ movie.id},
                null,
                null);

            if (movieCursor.moveToFirst()) {
                // update
//                Log.d(LOG_TAG, "update item " + movie.title);
                mContext.getContentResolver().update(
                        MovieContract.MovieEntry.buildUri(movie.id),
                        movieValues,
                        MovieContract.MovieEntry.WHERE_CLAUSE_MOVIE_ID,
                        new String[]{""+ movie.id}
                );
            } else {
//                Log.d(LOG_TAG, "insert new item " + movie.title);
                Uri insertedUri = mContext.getContentResolver().insert(
                        MovieContract.MovieEntry.CONTENT_URI,
                        movieValues
                );
            }
            movieCursor.close();

        }
    }

    /**
     * ViewHolder class
     */
    public static class PosterViewHolder extends RecyclerView.ViewHolder {
        protected View cv;
        protected TextView vTitle;
        protected TextView vRate;
        protected ImageView vPoster;

        public PosterViewHolder(View posterView) {
            super(posterView);
            cv = posterView.findViewById(R.id.cv_main);
            vPoster = (ImageView) posterView.findViewById(R.id.iv_poster);
            vTitle = (TextView) posterView.findViewById(R.id.tv_poster_title);
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
                inflate(R.layout.poster_cardview, viewGroup, false);

        return new PosterViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PosterViewHolder posterViewHolder, int i) {
        final Movie movie = mMovies.get(i);

        ImageView imageView = posterViewHolder.vPoster;
        if (movie.posterPath != null) {
            String posterURL = mContext.getString(R.string.img_base_url) + "w185/" + movie.posterPath;
//            Log.d(LOG_TAG, posterURL);

            int posterWidth = (int) mContext.getResources().getDimension(R.dimen.poster_width);
            int posterHeight = 4*posterWidth/3;
            Picasso.with(mContext)
                    .load(posterURL)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(posterWidth, posterHeight)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }

        posterViewHolder.vTitle.setText(movie.originalTitle);

        String rateTxt = movie.avgVote + "/10";
        posterViewHolder.vRate.setText(rateTxt);

        posterViewHolder.cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemListener.recyclerViewListClicked(v, movie);
            }
        });
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

}

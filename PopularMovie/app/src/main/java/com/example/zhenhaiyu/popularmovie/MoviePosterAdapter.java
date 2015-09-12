package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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

    public MoviePosterAdapter(Activity context, RecyclerViewClickListener itemListener, List<Movie> movies) {
        mMovies = movies;
        mContext = context;
        mItemListener = itemListener;
    }

    public void updateAllData(List<Movie> movies){
        mMovies.clear();
        mMovies.addAll(movies);
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

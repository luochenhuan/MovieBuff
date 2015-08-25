package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhenhaiyu on 2015-08-24.
 */
public class MoviePosterAdapter extends BaseAdapter {
    private List<Movie> mMovies;
    private final Activity mContext;


    public MoviePosterAdapter(Activity context, List<Movie> movies) {
        mMovies = movies;
        mContext = context;

    }

    @Override
    public int getCount(){
        return mMovies.size();
    }

    @Override
    public Object getItem(int position){
        return mMovies.get(position);
    }

    @Override
    public long getItemId(int position){
        return 0;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = mContext.getLayoutInflater();
        View posterView = inflater.inflate(R.layout.poster_gridlayout, null, true);
        ImageView imageView = (ImageView) posterView.findViewById(R.id.iv_poster);

        if (mMovies.get(position).mPosterPath != null) {
            String baseURL = "http://image.tmdb.org/t/p/w185/";
            String posterURL = baseURL + mMovies.get(position).mPosterPath;
            Picasso.with(mContext)
                    .load(posterURL)
                    .placeholder(R.drawable.ic_launcher)
                    .error(R.drawable.artist_placeholder_error)
//                    .resize(50, 50)
//                    .centerCrop()
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.artist_placeholder_error);
        }
        return posterView;
    }

}

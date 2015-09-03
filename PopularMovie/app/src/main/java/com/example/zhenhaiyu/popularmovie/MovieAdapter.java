package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by zhenhaiyu on 2015-08-27.
 */
public class MovieAdapter extends BaseAdapter {
    private List<Movie> mMovies;
    private final Activity mContext;

    public MovieAdapter(Activity context, List<Movie> movies) {
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
        posterView.setBackgroundColor(mContext.getResources().getColor(R.color.grid_darkbg));

        TextView title = (TextView) posterView.findViewById(R.id.tv_poster_title);
        title.setText(mMovies.get(position).mTitle);
//        title.setTypeface(custom_font);
        RatingBar rbRate = (RatingBar) posterView.findViewById(R.id.rb_rate);
        float rateStar = (float)mMovies.get(position).mAvgVote/2;
        rbRate.setRating(rateStar);

        TextView tvRate = (TextView) posterView.findViewById(R.id.tv_rate);
        String rateTxt = mMovies.get(position).mAvgVote + "/10";
        tvRate.setText(rateTxt);
//        tvRate.setTextColor(mContext.getResources().getColor(R.color.grid_lightrate));

        ImageView imageView = (ImageView) posterView.findViewById(R.id.iv_poster);
        if (mMovies.get(position).mPosterPath != null) {
            String posterURL = mContext.getString(R.string.img_base_url) + mMovies.get(position).mPosterPath;
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
        return posterView;
    }
}

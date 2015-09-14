package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.example.zhenhaiyu.popularmovie.model.Review;
import com.squareup.picasso.Picasso;

import java.util.List;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private final String LOG_TAG = ReviewAdapter.class.getSimpleName();
//    private final Context mContext;
    private List<Review> mReviews;

    public ReviewAdapter(Context context, List<Review> reviews) {
        mReviews = reviews;
//        mContext = context;
    }

    public void updateAllData(List<Review> reviews){
        mReviews.clear();
        mReviews.addAll(reviews);
    }


    /**
     * ViewHolder class
     */
    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        protected View cv;
        protected TextView vAuthor;
        protected TextView vContent;

        public ReviewViewHolder(View v) {
            super(v);
            cv = v.findViewById(R.id.cv_review);
            vAuthor = (TextView) v.findViewById(R.id.tv_author);
            vContent = (TextView) v.findViewById(R.id.tv_content);
        }
    }


    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.review_cardview, viewGroup, false);

        return new ReviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int i) {
        final Review review = mReviews.get(i);
        if (review != null) {
            if (review.author != null)
                holder.vAuthor.setText(review.author);
            if (review.content != null)
            holder.vContent.setText(review.content);
        }
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public Review getItem(int position){
        return mReviews.get(position);
    }
}

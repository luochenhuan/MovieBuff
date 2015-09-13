package com.example.zhenhaiyu.popularmovie;

import android.view.View;

import com.example.zhenhaiyu.popularmovie.model.Movie;

/**
 * ref: http://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
 */
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, Movie m);
}

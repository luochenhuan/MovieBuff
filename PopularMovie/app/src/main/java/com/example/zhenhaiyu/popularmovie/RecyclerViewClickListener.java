package com.example.zhenhaiyu.popularmovie;

import android.view.View;

/**
 * Created by zhenhaiyu on 2015-09-09.
 * ref: http://stackoverflow.com/questions/28296708/get-clicked-item-and-its-position-in-recyclerview
 */
public interface RecyclerViewClickListener {
    public void recyclerViewListClicked(View v, Movie m);
}

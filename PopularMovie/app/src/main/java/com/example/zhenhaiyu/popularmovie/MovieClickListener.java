package com.example.zhenhaiyu.popularmovie;

import android.view.View;

import com.example.zhenhaiyu.popularmovie.model.Movie;

public interface MovieClickListener {
    public void movieSelected(View view, Movie movie);
}

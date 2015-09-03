package com.example.zhenhaiyu.popularmovie;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {
    private final String LOG_TAG = DetailActivityFragment.class.getSimpleName();
    Movie mMovie;
    public DetailActivityFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
//        mMovieAdapter =
//                new MovieAdapter(getActivity(),
//                        R.layout.track_list_item,
//                        new ArrayList<Movie>());

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MovieDetail")) {
            mMovie = (Movie) intent.getParcelableExtra("MovieDetail");
//            TextView t = (TextView) rootView.findViewById(R.id.trackText);
//            t.setText(artistId);
//            new SearchTracksTask().execute(artistId);
//            Log.d(LOG_TAG, "artist.id " + artistId);
//            new SearchTracksTask().execute(artistId);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView t = (TextView) rootView.findViewById(R.id.tv_originalTitle);
        t.setText(mMovie.mTitle);
        return rootView;
    }
}

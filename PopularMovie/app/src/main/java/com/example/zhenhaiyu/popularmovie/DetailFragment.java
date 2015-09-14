package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private final String LOG_TAG = DetailFragment.class.getSimpleName();
    public static final String ARG_MOVIE = "ARG_MOVIE";
    private Movie mMovie;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(Movie movie) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        loadView(rootView);
        return rootView;
    }

    private void loadView(View rootView) {
        TextView release = (TextView) rootView.findViewById(R.id.tv_release);
//        Log.d(LOG_TAG, mMovie.releaseDate);
        if (!mMovie.releaseDate.equals("null"))
            release.setText("Release : " + mMovie.releaseDate);
        else
            release.setText("No Release Date in database");
        // rating
        RatingBar rbRate = (RatingBar) rootView.findViewById(R.id.rb_rate_detail);
        float rateStar = (float)mMovie.avgVote/2;
        rbRate.setRating(rateStar);
        TextView tvRate = (TextView) rootView.findViewById(R.id.tv_rate_detail);
        String rateTxt = mMovie.avgVote + "/10";
        tvRate.setText(rateTxt);

        //overview
        ExpandableTextView expOverviewTv = (ExpandableTextView) rootView.findViewById(R.id.expand_text_view);
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        if (!mMovie.overview.equals("null"))
            expOverviewTv.setText(mMovie.overview);
        else
            expOverviewTv.setText("");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

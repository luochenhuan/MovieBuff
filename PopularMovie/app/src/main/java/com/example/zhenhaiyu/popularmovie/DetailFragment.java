package com.example.zhenhaiyu.popularmovie;

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

import butterknife.Bind;
import butterknife.ButterKnife;


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

    @Bind(R.id.tv_release) TextView release;
    @Bind(R.id.rb_rate_detail) RatingBar rbRate;
    @Bind(R.id.expand_text_view) ExpandableTextView expOverviewTv;
    @Bind(R.id.iv_backdrop) ImageView imageView;
    @Bind(R.id.tv_rate_detail) TextView tvRate;

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

    public DetailFragment() {
        // Required empty public constructor
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
        ButterKnife.bind(getActivity(), rootView);

        return rootView;
    }

    private void loadView() {
        Log.d(LOG_TAG, mMovie.releaseDate);
        if (!mMovie.releaseDate.equals("null"))
            release.setText("Release : " + mMovie.releaseDate);
        else
            release.setText("No Release Date in database");
        // rating
        float rateStar = (float)mMovie.avgVote/2;
        rbRate.setRating(rateStar);

        String rateTxt = mMovie.avgVote + "/10";
        tvRate.setText(rateTxt);

        //overview
        // IMPORTANT - call setText on the ExpandableTextView to set the text content to display
        if (!mMovie.overview.equals("null"))
            expOverviewTv.setText(mMovie.overview);
        else
            expOverviewTv.setText("");

        if (mMovie.backdropPath != null) {
            String posterURL = getString(R.string.img_base_url) +"w780/" + mMovie.backdropPath;
            int backdropSize = (int) getResources().getDimension(R.dimen.detail_backdrop_height);
            Picasso.with(getContext())
                    .load(posterURL)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.movie_placeholder_error)
                    .resize(backdropSize, 0)
                    .into(imageView);
        } else{
            imageView.setImageResource(R.drawable.movie_placeholder_error);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}

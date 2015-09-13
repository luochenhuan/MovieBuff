package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.zhenhaiyu.popularmovie.model.MovieListResponse;
import com.example.zhenhaiyu.popularmovie.api.MoviesAPI;
import com.example.zhenhaiyu.popularmovie.api.MoviesReqService;
import com.example.zhenhaiyu.popularmovie.model.Movie;

import java.util.ArrayList;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewClickListener {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final int SPAN_COUNT = 2;
    private final String KEY_MOVIES = "saved_movies";

    private List<Movie> mMovies;
    private MoviePosterAdapter mPosterAdapter;

    private RecyclerView mRecyclerView;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
    SharedPreferences movieDisplayPreferences;
    private String mNavPref;
    MoviesReqService mMoviesReqService;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//         Retain this fragment across configuration changes.
        setRetainInstance(true);

        movieDisplayPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        // set up listener for sharedpreferences change
        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(getResources().getString(R.string.nav_pref))){
                    Log.d(LOG_TAG, "nav_pref changed!");
                    mNavPref = sharedPreferences.getString(key, null);
                    if (networkConnected() == true) {
//                        FetchMoviesTask moviesTask = new FetchMoviesTask();
//                        moviesTask.execute(mNavPref);
                        fetchMovies(mNavPref);
                    }
                    else {
                        snackbarInform();
                    }
                }
            }
        };
        mPosterAdapter = new MoviePosterAdapter(getContext(), this, new ArrayList<Movie>());
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mNavPref = movieDisplayPreferences.getString(getResources().getString(R.string.nav_pref), null);
        Log.d(LOG_TAG, "onCreateView: " + mNavPref);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mPosterAdapter);

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            mPosterAdapter.updateAllData(mMovies);
        }
        else {
            if (networkConnected() == true) {
                mMovies = new ArrayList<>();
                // get service instance
                mMoviesReqService = MoviesReqService.getInstance(getContext());
                fetchMovies(mNavPref);
//                FetchMoviesTask moviesTask = new FetchMoviesTask();
//                moviesTask.execute(mNavPref);
            }
            else {
                snackbarInform();
            }
        }

        return rootView;

    }

    @Override
    public void onResume() {
        Log.d(LOG_TAG, "onResume");
        movieDisplayPreferences.registerOnSharedPreferenceChangeListener(mPrefListener);
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d(LOG_TAG, "onPause");
        movieDisplayPreferences.unregisterOnSharedPreferenceChangeListener(mPrefListener);
        super.onPause();
    }

    @Override
    public void recyclerViewListClicked(View v, Movie m) {
//        Toast.makeText(getActivity(), "click " + m.mTitle,
//                        Toast.LENGTH_SHORT).show();
        Context context = getActivity();
        Intent detailIntent = new Intent(context, DetailActivity.class);
        detailIntent.putExtra(getResources().getString(R.string.title_activity_detail), m);
        startActivity(detailIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");
        outState.putParcelableArrayList(KEY_MOVIES, (ArrayList<Movie>)mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy fragment");
        super.onDestroy();
    }

    private void fetchMovies(String request){
        // Create an HTTP client that uses a cache on the file system.
//        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(LOG_TAG, "fetchMovies: " + request);
        MoviesAPI api = mMoviesReqService.getMoviesReqService();
        api.discoverMoviesByPref(request, new Callback<MovieListResponse>() {
            @Override
            public void success(MovieListResponse movies, Response response) {
                // handle response here
                mMovies = movies.getMovies();
                mPosterAdapter.updateAllData(mMovies);
                Log.d(LOG_TAG, "mPosterAdapter update");
                mPosterAdapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.e(LOG_TAG, "Failed!", error);
                Toast.makeText(getActivity(), "fetchMovies failed", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean networkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }

    private void snackbarInform(){
        Snackbar.make(getActivity().findViewById(R.id.content_fragment),
                        "Woops! Seems we lost network connection ...", Snackbar.LENGTH_INDEFINITE)
                .setAction("CLOSE", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light ))
                .show();
    }
}


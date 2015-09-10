package com.example.zhenhaiyu.popularmovie;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements RecyclerViewClickListener {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final String MyPREFERENCES = "MyPrefs" ;
    private static final int SPAN_COUNT = 2;
    private final String KEY_MOVIES = "saved_movies";
    private ArrayList<Movie> mMovies;
    private MoviePosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;
    private SharedPreferences.OnSharedPreferenceChangeListener mPrefListener;
    SharedPreferences movieDisplayPreferences;
    private String mNavPref;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
//         Retain this fragment across configuration changes.
        setRetainInstance(true);

        movieDisplayPreferences = getActivity().getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        mPrefListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
                if (key.equals(getResources().getString(R.string.nav_pref))){
                    Log.d(LOG_TAG, "nav_pref changed!");
                    mNavPref = sharedPreferences.getString(key, null);

                    if (networkConnected() == true) {
                        FetchMoviesTask moviesTask = new FetchMoviesTask();
                        moviesTask.execute(mNavPref);
                    }
                    else {
                        snackbarInform();
                    }
                }
            }
        };
        mPosterAdapter = new MoviePosterAdapter(getActivity(), this, new ArrayList<Movie>());

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
        } else {
            if (networkConnected() == true) {
                mMovies = new ArrayList<>();
                FetchMoviesTask moviesTask = new FetchMoviesTask();
                moviesTask.execute(mNavPref);
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
        outState.putParcelableArrayList(KEY_MOVIES, mMovies);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        Log.d(LOG_TAG, "onDestroy fragment");
        super.onDestroy();
    }


    private class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>> {
        @Override
        protected ArrayList<Movie> doInBackground(String... sortPref) {
            ArrayList<Movie> movies = new ArrayList<Movie>();
            String movieJsonStr = null;

            final String SORT_PARAM = "sort_by";
            final String APIKEY_PARAM = "api_key";
            String api_key = getString(R.string.api_key);
            HttpURLConnection urlConnection = null;

            // build url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter(SORT_PARAM, sortPref[0])
                    .appendQueryParameter(APIKEY_PARAM, api_key)
            ;

            String urlStr = builder.build().toString();
            Log.d(LOG_TAG, "tmdb GET request: " + urlStr);

            try {
//                URL url = new URL(urlStr);
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//                // Read the input stream into a String
//                InputStream inputStream = urlConnection.getInputStream();

                // same functionality as the above code block
                movieJsonStr = getJsonString(new URL(urlStr).openStream());
                Log.d(LOG_TAG, "tmdb response JSONstring: " + movieJsonStr);
            } catch(IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }

            try {
                movies = getMoviesFromJson(movieJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return movies;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movies) {
            mMovies = movies;
            mPosterAdapter.updateAllData(mMovies);
            Log.d(LOG_TAG, "mPosterAdapter update");
            mPosterAdapter.notifyDataSetChanged();
        }


        private ArrayList<Movie> getMoviesFromJson(String jsonStr)
                throws JSONException {
            final String RESULT = "results";
            final String BD_PATH = "backdrop_path";
            final String POSTER_PATH = "poster_path";
            final String GENRES = "genre_ids";
            final String ORI_TITLE = "original_title";
            final String TITLE = "title";
            final String RELEASE = "release_date";
            final String AVG_VOTE = "vote_average";
            final String POPULARITY = "popularity";
            final String OVERVIEW = "overview";
            ArrayList<Movie> movies = new ArrayList<Movie>();
            Movie movie;

            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONArray movieArray = jsonObject.getJSONArray(RESULT);

            for(int i = 0; i < movieArray.length(); i++)
            {
                jsonObject = movieArray.getJSONObject(i);

                movie = new Movie(jsonObject.getLong("id"));
                movie.mBackdropPath = jsonObject.getString(BD_PATH);
                movie.mOriginalTitle = jsonObject.getString(ORI_TITLE);
                movie.mTitle = jsonObject.getString(TITLE);
                movie.mOverView = jsonObject.getString(OVERVIEW);
                movie.mReleaseDate = jsonObject.getString(RELEASE);
                movie.mPosterPath = jsonObject.getString(POSTER_PATH);
                movie.mPopularity = jsonObject.getDouble(POPULARITY);
                movie.mAvgVote = jsonObject.getDouble(AVG_VOTE);

                JSONArray genreArr = jsonObject.getJSONArray(GENRES);
                if (genreArr != null){
                    int len = genreArr.length();
                    movie.mGenreArr = new int[len];
                    for (int j=0; j<len; j++){
                        movie.mGenreArr[j] = genreArr.optInt(j);
                    }
                }
                movies.add(movie);
            }
            return movies;
        }

        private String getJsonString(InputStream inputStream) {
            // Read the input stream into a String
            StringBuffer buffer = new StringBuffer();
            BufferedReader reader = null;
            if (inputStream == null) {
                // Nothing to do.
                // forecastJsonStr = null;
                return null;
            }
            try {
                String line;
                reader = new BufferedReader(new InputStreamReader(inputStream)); // InputStreamReader(inputStream): byte stream -> char stream
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                return null;
            }
            finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }
            return buffer.toString();
        }
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


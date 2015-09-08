package com.example.zhenhaiyu.popularmovie;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class MainActivityFragment extends Fragment {
    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();
    private static final int SPAN_COUNT = 2;
    private final String KEY_MOVIES = "saved_movies";
    private ArrayList<Movie> mMovies;
    private MoviePosterAdapter mPosterAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        Log.d(LOG_TAG, "onCreate");
//         Retain this fragment across configuration changes.
        setRetainInstance(true);
        mPosterAdapter = new MoviePosterAdapter(getActivity(), new ArrayList<Movie>());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView");
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.main_recyclerview);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        mRecyclerView.setAdapter(mPosterAdapter);

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            mPosterAdapter.updateAllData(mMovies);
        } else {
            mMovies = new ArrayList<>();
            FetchMoviesTask moviesTask = new FetchMoviesTask();
            String sortPref = "popularity.desc";
//        switch (mPage) {
//            case 0:
//                sortPref = "popularity.desc";
//                break;
//            case 1:
//                sortPref = "vote_average.desc";
//                break;
//
//        }
            moviesTask.execute(sortPref);
        }

//        mRecyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            public void onItemClick(AdapterView<?> parent, View v,
//                                    int position, long id) {
//                Toast.makeText(getActivity(), "" + position,
//                                Toast.LENGTH_SHORT).show();
//
//                Context context = getActivity();
//                Movie movie = mPosterAdapter.getItem(position);
//                Intent detailIntent = new Intent(context, DetailActivity.class);
//                detailIntent.putExtra("MovieDetail", movie);
//                startActivity(detailIntent);
//            }
//        });

        return rootView;

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
//              Log.v(LOG_TAG, String.valueOf(artists.size()));
            mMovies = movies;
            mPosterAdapter.updateAllData(mMovies);
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

}


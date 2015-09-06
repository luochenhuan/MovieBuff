package com.example.zhenhaiyu.popularmovie;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
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
public class MovieUpdate {
    private final String LOG_TAG = MovieUpdate.class.getSimpleName();
    private final Activity mContext;
    private final View mRootView;
    private MoviePosterAdapter mPosterAdapter;
    private GridView mposterGridView;
    public String[] mRequestParams;

    public MovieUpdate(Activity context, View view) {
        this.mContext = context;
        this.mRootView = view;
    }

    public View update(String[] requestParams) {
        mposterGridView = (GridView) mRootView.findViewById(R.id.posterGrid);
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        moviesTask.execute(requestParams);

        mposterGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Toast.makeText(mContext, "" + position,
                                Toast.LENGTH_SHORT).show();

                Movie movie = (Movie) mPosterAdapter.getItem(position);
                Intent detailIntent = new Intent(mContext, DetailActivity.class);
                detailIntent.putExtra("MovieDetail", movie);
                mContext.startActivity(detailIntent);
            }
        });
        return mRootView;
    }

    private class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {
        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        @Override
        protected List<Movie> doInBackground(String... requests) {
            List<Movie> movies = new ArrayList<>();
            String movieJsonStr = null;

            final String SORT_PARAM = "sort_by";
            final String APIKEY_PARAM = "api_key";

            HttpURLConnection urlConnection = null;

            // build url
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("api.themoviedb.org")
                    .appendPath("3")
                    .appendPath("discover")
                    .appendPath("movie")
                    .appendQueryParameter(SORT_PARAM, requests[0])
                    .appendQueryParameter(APIKEY_PARAM, requests[1])
            ;

            String urlStr = builder.build().toString();
            Log.d(LOG_TAG, "tmdb GET request: " + urlStr);

            try {
                URL url = new URL(urlStr);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();

                // same functionality as the above code block BUT not safe
//            movieJsonStr = getJsonString(new URL(urlStr).openStream());
                movieJsonStr = getJsonString(inputStream);

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
        protected void onPostExecute(List<Movie> movies) {
        }


        private List<Movie> getMoviesFromJson(String jsonStr)
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
            List<Movie> movies = new ArrayList<>();
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


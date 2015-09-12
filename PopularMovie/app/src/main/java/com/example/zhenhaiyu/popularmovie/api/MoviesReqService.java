package com.example.zhenhaiyu.popularmovie.api;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.zhenhaiyu.popularmovie.R;
import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import butterknife.BindString;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;


public class MoviesReqService {
    private final String LOG_TAG = MoviesReqService.class.getSimpleName();
    private final Context mContext;
    private final String END_POINT;
    private final String apiKey;

    RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            request.addQueryParam("api_key", apiKey);
        }
    };

    private static MoviesReqService mInstance; // Singleton pattern
    private static MoviesAPI mMovieReqService;

    public MoviesReqService(Context context){
        this.mContext = context;
        this.END_POINT = mContext.getResources().getString(R.string.movie_base_url);
        this.apiKey = mContext.getResources().getString(R.string.api_key);
//        Log.d(LOG_TAG, "END_POINT: " + END_POINT);
//        Type collectionType = new TypeToken<List<Movie>>(){}.getType();

        Gson gson = new GsonBuilder()
//                .registerTypeAdapterFactory(new ItemTypeAdapterFactory()) // ? how to deserialize and get movie objects only
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL) // ?
                .setEndpoint(END_POINT)
                .setRequestInterceptor(requestInterceptor)
                .setConverter(new GsonConverter(gson))
                .build();

        mMovieReqService = restAdapter.create(MoviesAPI.class);
    }

    public static MoviesReqService getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MoviesReqService(context);
        }

        return mInstance;
    }

    public MoviesAPI getMoviesReqService(){
        return mMovieReqService;
    }

}

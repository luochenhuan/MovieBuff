package com.example.zhenhaiyu.popularmovie.api;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.zhenhaiyu.popularmovie.model.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * ref: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
 */
public class MovieListResponse implements Parcelable {
    @SerializedName("results")
    List<Movie> movies;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(movies);
    }

    public MovieListResponse() {
    }

    protected MovieListResponse(Parcel in) {
        this.movies = in.createTypedArrayList(Movie.CREATOR);
    }

    public static final Parcelable.Creator<MovieListResponse> CREATOR = new Parcelable.Creator<MovieListResponse>() {
        public MovieListResponse createFromParcel(Parcel source) {
            return new MovieListResponse(source);
        }

        public MovieListResponse[] newArray(int size) {
            return new MovieListResponse[size];
        }
    };

    public List<Movie> getMovies(){
        return movies;
    }
}

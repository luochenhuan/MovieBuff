package com.example.zhenhaiyu.popularmovie.model;

import android.content.ContentValues;
import android.os.Parcel;
import android.os.Parcelable;

import com.example.zhenhaiyu.popularmovie.data.MovieContract;
import com.google.gson.annotations.SerializedName;

/**
 *
 */
public class Movie implements Parcelable {
    public long id;
    public boolean adult;
    @SerializedName("backdrop_path")
    public String backdropPath;

    @SerializedName("genre_ids")
    public int[] genreIds;

    @SerializedName("original_language")
    public String originalLang;

    @SerializedName("original_title")
    public String originalTitle;

    public String overview;

    @SerializedName("release_date")
    public String releaseDate;

    @SerializedName("poster_path")
    public String posterPath;

    public double popularity;
    public String title;
    public boolean video;

    @SerializedName("vote_average")
    public double avgVote;

    @SerializedName("vote_count")
    public int voteCount;

    public Movie(long id){
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeByte(adult ? (byte) 1 : (byte) 0);
        dest.writeString(this.backdropPath);
        dest.writeIntArray(this.genreIds);
        dest.writeString(this.originalLang);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.releaseDate);
        dest.writeString(this.posterPath);
        dest.writeDouble(this.popularity);
        dest.writeString(this.title);
        dest.writeByte(video ? (byte) 1 : (byte) 0);
        dest.writeDouble(this.avgVote);
        dest.writeInt(this.voteCount);
    }

    protected Movie(Parcel in) {
        this.id = in.readLong();
        this.adult = in.readByte() != 0;
        this.backdropPath = in.readString();
        this.genreIds = in.createIntArray();
        this.originalLang = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.releaseDate = in.readString();
        this.posterPath = in.readString();
        this.popularity = in.readDouble();
        this.title = in.readString();
        this.video = in.readByte() != 0;
        this.avgVote = in.readDouble();
        this.voteCount = in.readInt();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public ContentValues getContentValues(){
        ContentValues movieValues = new ContentValues();
        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, this.id);
        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, this.title);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, this.originalTitle);
        movieValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, this.releaseDate);
        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, this.overview);
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_AVERAGE, this.avgVote);
        movieValues.put(MovieContract.MovieEntry.COLUMN_VOTE_COUNT, this.voteCount);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, this.posterPath);
        movieValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, this.backdropPath);
        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, this.popularity);
        movieValues.put(MovieContract.MovieEntry.COLUMN_LANGUAGE, this.originalLang);
        movieValues.put(MovieContract.MovieEntry.COLUMN_VIDEO, this.video? 1: 0);
        movieValues.put(MovieContract.MovieEntry.COLUMN_ADULT, this.adult? 1: 0);

        return movieValues;
    }
}

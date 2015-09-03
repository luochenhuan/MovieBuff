package com.example.zhenhaiyu.popularmovie;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by zhenhaiyu on 2015-08-24.
 */
public class Movie implements Parcelable {
    long id;
    String mBackdropPath;
    int[] mGenreArr;
    String mOriginalTitle;
    String mTitle;
    String mOverView;
    String mPosterPath;
    String mReleaseDate;
    double mAvgVote;
    double mPopularity;

    public Movie(long id){
        this.id = id;
    }

    private Movie(Parcel in){
        id = in.readLong();
        mBackdropPath = in.readString();
        mGenreArr = in.createIntArray();
        mOriginalTitle = in.readString();
        mTitle = in.readString();
        mOverView = in.readString();
        mPosterPath = in.readString();
        mReleaseDate = in.readString();
        mAvgVote = in.readDouble();
        mPopularity = in.readDouble();
    }

    @Override
    public int describeContents(){return 0;}

    @Override
    public void writeToParcel(Parcel parcel, int i){
        parcel.writeLong(id);
        parcel.writeString(mBackdropPath);
        parcel.writeIntArray(mGenreArr);
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mTitle);
        parcel.writeString(mOverView);
        parcel.writeString(mPosterPath);
        parcel.writeString(mReleaseDate);
        parcel.writeDouble(mAvgVote);
        parcel.writeDouble(mPopularity);

    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>(){
        @Override
        public Movie createFromParcel(Parcel parcel){
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i){
            return new Movie[i];
        }
    };

}

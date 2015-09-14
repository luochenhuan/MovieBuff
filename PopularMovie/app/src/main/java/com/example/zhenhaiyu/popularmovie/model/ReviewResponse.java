package com.example.zhenhaiyu.popularmovie.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponse implements Parcelable {
    @SerializedName("results")
    List<Review> reviews;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(reviews);
    }

    public ReviewResponse() {
    }

    protected ReviewResponse(Parcel in) {
        this.reviews = in.createTypedArrayList(Review.CREATOR);
    }

    public static final Creator<ReviewResponse> CREATOR = new Creator<ReviewResponse>() {
        public ReviewResponse createFromParcel(Parcel source) {
            return new ReviewResponse(source);
        }

        public ReviewResponse[] newArray(int size) {
            return new ReviewResponse[size];
        }
    };

    public List<Review> getReviews(){
        return reviews;
    }
}

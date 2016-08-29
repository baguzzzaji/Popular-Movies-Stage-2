package com.example.bagus.moviedbv2;


import android.os.Parcel;
import android.os.Parcelable;
import android.renderscript.Double2;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {
    @SerializedName("title")
    String movieTitle;
    @SerializedName("overview")
    String movieOverview;
    @SerializedName("release_date")
    String movieReleaseDate;
    @SerializedName("poster_path")
    String moviePoster;
    @SerializedName("backdrop_path")
    String movieBackdropImage;
    @SerializedName("vote_average")
    Double movieRating;

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public Double getMovieRating() {
        return movieRating;
    }

    public String getMovieReleaseDate() {
        return movieReleaseDate;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getMovieBackdropImage() {
        return movieBackdropImage;
    }

    protected Movie(Parcel in) {
        movieTitle = in.readString();
        movieOverview = in.readString();
        movieReleaseDate = in.readString();
        moviePoster = in.readString();
        movieBackdropImage = in.readString();
        movieRating = in.readDouble();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(movieReleaseDate);
        dest.writeString(moviePoster);
        dest.writeString(movieBackdropImage);
        dest.writeDouble(movieRating);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

}
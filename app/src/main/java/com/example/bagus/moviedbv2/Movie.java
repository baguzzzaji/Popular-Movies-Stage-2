package com.example.bagus.moviedbv2;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Movie implements RealmModel, Parcelable {
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
    @PrimaryKey
    @SerializedName("id")
    String movieId;
    RealmList<Review> reviews;
    RealmList<Trailer> trailers;

    public RealmList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(RealmList<Review> reviews) {
        this.reviews = reviews;
    }

    public RealmList<Trailer> getTrailers() {
        return trailers;
    }

    public void setTrailers(RealmList<Trailer> trailers) {
        this.trailers = trailers;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

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

    public Movie() {

    }

    protected Movie(Parcel in) {
        movieTitle = in.readString();
        movieOverview = in.readString();
        movieReleaseDate = in.readString();
        moviePoster = in.readString();
        movieBackdropImage = in.readString();
        movieRating = in.readDouble();
        movieId = in.readString();
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
        dest.writeString(movieId);
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
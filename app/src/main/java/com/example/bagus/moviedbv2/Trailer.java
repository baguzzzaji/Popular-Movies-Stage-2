package com.example.bagus.moviedbv2;

import com.google.gson.annotations.SerializedName;


public class Trailer {
    @SerializedName("key")
    String trailerKey;

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }
}

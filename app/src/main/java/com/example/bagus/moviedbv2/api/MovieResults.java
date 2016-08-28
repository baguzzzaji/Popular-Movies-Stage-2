package com.example.bagus.moviedbv2.api;

import com.example.bagus.moviedbv2.Movie;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by bagus on 24/08/16.
 */
public class MovieResults {

    @SerializedName("results")
    private List<Movie> results;

    public List<Movie> getResults() {
        return results;
    }

    public void setResults(List<Movie> results) {
        this.results = results;
    }


}

package com.example.bagus.moviedbv2.api;

import com.example.bagus.moviedbv2.Trailer;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by baguzzzaji on 31/08/2016.
 */
public class TrailerResults {
    @SerializedName("results")
    private List<Trailer> results;

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }

}

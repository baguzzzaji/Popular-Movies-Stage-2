package com.example.bagus.moviedbv2.api;

import com.example.bagus.moviedbv2.Review;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResults {

    @SerializedName("results")
    private List<Review> results;

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }


}

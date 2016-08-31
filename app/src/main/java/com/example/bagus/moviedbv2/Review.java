package com.example.bagus.moviedbv2;

import com.google.gson.annotations.SerializedName;

/**
 * Created by baguzzzaji on 31/08/2016.
 */
public class Review {
    @SerializedName("author")
    String reviewAuthor;
    @SerializedName("content")
    String reviewContent;

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public void setReviewAuthor(String reviewAuthor) {
        this.reviewAuthor = reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }
}

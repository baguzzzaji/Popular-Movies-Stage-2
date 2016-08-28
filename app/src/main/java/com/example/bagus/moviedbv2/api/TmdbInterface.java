package com.example.bagus.moviedbv2.api;

import com.example.bagus.moviedbv2.Movie;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by bagus on 24/08/16.
 */
public interface TmdbInterface {
    @GET("movie/popular")
    Call<MovieResults> getPopularMovies(@Query("api_key") String api);

    @GET("movie/top_rated")
    Call<MovieResults> getTopRatedMovies(@Query("api_key") String api);

    @GET("movie/{id}")
    Call<MovieResults> getMovieDetails(@Path("id") int id, @Query("api_key") String api);
}

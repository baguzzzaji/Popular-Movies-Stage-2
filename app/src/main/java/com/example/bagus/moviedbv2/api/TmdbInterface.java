package com.example.bagus.moviedbv2.api;

import retrofit2.Call;
import retrofit2.http.GET;
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

    @GET("movie/{id}/videos")
    Call<TrailerResults> getTrailer(@Path("id") String id, @Query("api_key") String api);
}

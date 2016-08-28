package com.example.bagus.moviedbv2;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.bagus.moviedbv2.api.MovieResults;
import com.example.bagus.moviedbv2.api.RetrofitHelper;
import com.example.bagus.moviedbv2.api.TmdbInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularMoviesFragment extends Fragment {
    private static final String TAG = PopularMoviesFragment.class.getSimpleName();

    private final static String API_KEY = "1bb03dcadd1803cf79af629648c59d38";

    private RecyclerView moviesRecyclverView;
    private MovieAdapter movieAdapter;

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        initializeRecyclerView(rootView);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Configure your API key from themoviedb.org!", Toast.LENGTH_SHORT).show();
        }

        if (!isNetworkAvailable()) {
            Toast.makeText(getContext(), "Network not available.", Toast.LENGTH_LONG).show();
        }

        TmdbInterface tmdbInterface = RetrofitHelper.getClient().create(TmdbInterface.class);

        Call<MovieResults> call = tmdbInterface.getPopularMovies(API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                List<Movie> movies = response.body().getResults();
                moviesRecyclverView.setAdapter(new MovieAdapter(movies, R.layout.movie_item, getContext()));
                Log.d(TAG, "The poster path is " + movies.get(1).getMoviePoster());
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initializeRecyclerView(View view) {
        List<Movie> movies = new ArrayList<>();
        moviesRecyclverView = (RecyclerView) view.findViewById(R.id.rv_popular_movies);
        moviesRecyclverView.setHasFixedSize(true);
        moviesRecyclverView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        moviesRecyclverView.setAdapter(new MovieAdapter(movies, R.layout.movie_item, getContext()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}

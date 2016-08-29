package com.example.bagus.moviedbv2;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
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


public class PopularMoviesFragment extends Fragment{
    private static final String TAG = PopularMoviesFragment.class.getSimpleName();

    private RecyclerView moviesRecyclverView;
    List<Movie> movies;

    public PopularMoviesFragment() {
        // Required empty public constructor
    }

    private void getPopularMovies() {
        if (RetrofitHelper.API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Configure your API key from themoviedb.org!", Toast.LENGTH_SHORT).show();
        }

        TmdbInterface tmdbInterface = RetrofitHelper.getClient().create(TmdbInterface.class);

        Call<MovieResults> call = tmdbInterface.getPopularMovies(RetrofitHelper.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                movies = response.body().getResults();
            }

            @Override
            public void onFailure(Call<MovieResults> call, Throwable t) {
                Log.e(TAG, t.toString());
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        initializeRecyclerView(rootView);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            if (isNetworkAvailable()) {
                getPopularMovies();
            } else {
                Toast.makeText(getActivity(), "Could not download movies, network error.", Toast.LENGTH_SHORT).show();
            }
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
            Log.d(TAG, "There's a movies already, no need to get the new one!");

        }
        MovieAdapter adapter = new MovieAdapter(movies, R.layout.movie_item, getContext());
        adapter.setClickListener(new MovieAdapter.ClickListener() {
            @Override
            public void itemClicked(View view, int position) {
            }
        });

        moviesRecyclverView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initializeRecyclerView(View view) {
        int spanSize = 0;
        movies = new ArrayList<>();
        moviesRecyclverView = (RecyclerView) view.findViewById(R.id.rv_popular_movies);
        moviesRecyclverView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanSize = 4;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanSize = 2;
        }
        moviesRecyclverView.setLayoutManager(new GridLayoutManager(getContext(), spanSize));
        moviesRecyclverView.setAdapter(new MovieAdapter(movies, R.layout.movie_item, getContext()));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Movie> arrayList = (ArrayList<Movie>) movies;
        outState.putParcelableArrayList("movies", arrayList);

    }

}

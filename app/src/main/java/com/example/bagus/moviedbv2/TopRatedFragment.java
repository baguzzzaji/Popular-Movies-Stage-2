package com.example.bagus.moviedbv2;


import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
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


public class TopRatedFragment extends Fragment {
    private static final String TAG = PopularMoviesFragment.class.getSimpleName();

    private RecyclerView moviesRecyclerView;
    List<Movie> movies;

    public TopRatedFragment() {
        // Required empty public constructor
    }

    private void getTopRatedMovies() {
        if (RetrofitHelper.API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Configure your API key from themoviedb.org!", Toast.LENGTH_SHORT).show();
        }

        TmdbInterface tmdbInterface = RetrofitHelper.getClient().create(TmdbInterface.class);

        Call<MovieResults> call = tmdbInterface.getTopRatedMovies(RetrofitHelper.API_KEY);
        call.enqueue(new Callback<MovieResults>() {
            @Override
            public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                movies = response.body().getResults();
                MovieAdapter adapter = new MovieAdapter(movies, R.layout.movie_item, getContext());

                adapter.setClickListener(new MovieAdapter.ClickListener() {
                    @Override
                    public void itemClicked(View view, int position) {
                        Intent intent = new Intent(getActivity(), DetailActivity.class);
                        intent.putExtra(MovieAdapter.MovieViewHolder.MOVIE, movies.get(position));
                        startActivity(intent);
                    }
                });

                moviesRecyclerView.setAdapter(adapter);
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

        View rootView = inflater.inflate(R.layout.fragment_top_rated, container, false);
        initializeRecyclerView(rootView);

        if (savedInstanceState == null || !savedInstanceState.containsKey("movies")) {
            if (isNetworkAvailable()) {
                getTopRatedMovies();
            } else {
                Toast.makeText(getActivity(), "Could not download movies, network error.", Toast.LENGTH_SHORT).show();
            }
        } else {
            movies = savedInstanceState.getParcelableArrayList("movies");
        }

        // Inflate the layout for this fragment
        return rootView;
    }

    private void initializeRecyclerView(View view) {
        int spanSize = 0;
        movies = new ArrayList<>();
        moviesRecyclerView = (RecyclerView) view.findViewById(R.id.rv_top_rated_movies);
        moviesRecyclerView.setHasFixedSize(true);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            spanSize = 4;
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            spanSize = 2;
        }
        moviesRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), spanSize));
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList<Movie> arrayList = new ArrayList<>(movies);
        outState.putParcelableArrayList("movies", arrayList);
        super.onSaveInstanceState(outState);
    }
}

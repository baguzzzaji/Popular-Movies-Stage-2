package com.example.bagus.moviedbv2;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;
    private Context context;
    private int rowLayout;

    public MovieAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.context = context;
        this.rowLayout = rowLayout;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);

        String poster_path = "http://image.tmdb.org/t/p/w185" + movie.getMoviePoster();
        Picasso.with(context)
                .load(poster_path)
                .into(holder.posterImageView);
    }

    @Override
    public int getItemCount() {
        return (movies == null) ? 0 : movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView posterImageView;

        public MovieViewHolder(View view) {
            super(view);
            posterImageView = (ImageView) view.findViewById(R.id.movie_poster);
        }
    }

}

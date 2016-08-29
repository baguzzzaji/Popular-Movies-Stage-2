package com.example.bagus.moviedbv2;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private String TAG = MovieAdapter.class.getSimpleName();

    private List<Movie> movies;
    private Context context;
    private int rowLayout;
    private ClickListener clickListener;

    public MovieAdapter(List<Movie> movies, int rowLayout, Context context) {
        this.movies = movies;
        this.context = context;
        this.rowLayout = rowLayout;
    }


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);

        return new MovieViewHolder(view, movies);
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

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private String TAG = MovieViewHolder.class.getSimpleName();

        static final String MOVIE = "movie";
        public ImageView posterImageView;
        List<Movie> movies;

        public MovieViewHolder(View view, List<Movie> movies) {
            super(view);
            this.movies = movies;
            context = view.getContext();
            posterImageView = (ImageView) view.findViewById(R.id.movie_poster);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) {
                Log.d(TAG, "Movie " + movies.get(getAdapterPosition()).getMovieTitle() + " is clicked." );
                clickListener.itemClicked(view, getAdapterPosition());

            }
        }
    }


    public interface ClickListener {
        void itemClicked(View view, int position);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

}

package com.example.bagus.moviedbv2;

import android.content.Intent;
import android.renderscript.Double2;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private String TAG = DetailActivity.class.getSimpleName();

    public DetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_detail, container, false);

        Movie movie = (Movie) getActivity().getIntent().getParcelableExtra(MovieAdapter.MovieViewHolder.MOVIE);

        ImageView backdropImageView = (ImageView) rootView.findViewById(R.id.movie_backdrop);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.movie_poster);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        TextView releasedDateTextView = (TextView) rootView.findViewById(R.id.detail_movie_released_date);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.detail_movie_vote_average);
        TextView overviewTextView = (TextView) rootView.findViewById(R.id.detail_movie_overview);

        String backdrop_url = "http://image.tmdb.org/t/p/w342" + movie.getMovieBackdropImage();
        String poster_url = "http://image.tmdb.org/t/p/w185" + movie.getMoviePoster();

        Picasso.with(getActivity())
                .load(backdrop_url)
                .into(backdropImageView);
        Picasso.with(getActivity())
                .load(poster_url)
                .into(posterImageView);

        titleTextView.setText(movie.getMovieTitle());
        ratingTextView.setText(String.valueOf(movie.getMovieRating()));
        overviewTextView.setText(movie.getMovieOverview());

        // Date format
        try {
            SimpleDateFormat curFormat = new SimpleDateFormat("yyyy-mm-dd");
            Date date = curFormat.parse(movie.getMovieReleaseDate());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy");
            releasedDateTextView.setText(simpleDateFormat.format(date));
            Log.d(TAG, simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getMovieTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        return rootView;
    }
}

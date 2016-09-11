package com.example.bagus.moviedbv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFavoriteFragment extends Fragment {

    private String TAG = DetailActivityFavoriteFragment.class.getSimpleName();

    public DetailActivityFavoriteFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_detail_activity_favorite, container, false);
        String movieId = getActivity().getIntent().getStringExtra("id");

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        final Realm realm = Realm.getDefaultInstance();
        final RealmResults<Movie> movies = realm.where(Movie.class).equalTo("movieId", movieId).findAll();
        final Movie movie = movies.get(0);

        ImageView backdropImageView = (ImageView) rootView.findViewById(R.id.movie_backdrop);
        ImageView posterImageView = (ImageView) rootView.findViewById(R.id.detail_movie_poster);
        TextView titleTextView = (TextView) rootView.findViewById(R.id.detail_movie_title);
        TextView releasedDateTextView = (TextView) rootView.findViewById(R.id.detail_movie_released_date);
        TextView ratingTextView = (TextView) rootView.findViewById(R.id.detail_movie_vote_average);
        TextView overviewTextView = (TextView) rootView.findViewById(R.id.detail_movie_overview);

        String backdrop_url = "http://image.tmdb.org/t/p/w342" + movie.getMovieBackdropImage();
        String poster_url = "http://image.tmdb.org/t/p/w185" + movie.getMoviePoster();

        Picasso.with(getActivity())
                .load(backdrop_url)
                .placeholder(R.drawable.backdrop)
                .into(backdropImageView);
        Picasso.with(getActivity())
                .load(poster_url)
                .placeholder(R.drawable.poster)
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

        // Reviews section
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movie_reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        RealmList<Review> reviews = movie.getReviews();
        final RealmList<Trailer> trailers = movie.getTrailers();
        recyclerView.setAdapter(new ReviewAdapter(reviews, getContext(), R.layout.review_item));

        // Trailers section
        TextView trailerOne = (TextView) rootView.findViewById(R.id.detail_movie_trailer_1);
        TextView trailerTwo = (TextView) rootView.findViewById(R.id.detail_movie_trailer_2);


        if (reviews != null) {
            Log.d(TAG, "Author of " + movie.getMovieTitle() + " review is " + reviews.get(0).getReviewAuthor());
        } else {
            Log.d(TAG, "Reviews is null");
        }

        trailerOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailers.get(0) != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(0).getTrailerKey())));
                } else {
                    Toast.makeText(getContext(), "Trailer not available", Toast.LENGTH_LONG).show();
                }
            }
        });

        trailerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailers.get(1) != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(1).getTrailerKey())));
                } else {
                    Toast.makeText(getContext(), "Trailer not available", Toast.LENGTH_LONG).show();
                }
            }
        });

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator_layout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm1) {
                        movies.deleteFirstFromRealm();
                    }
                });

                Snackbar.make(coordinatorLayout, "Movie removed from favorites!", Snackbar.LENGTH_LONG).show();
            }
        });


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((DetailActivityFavorite) getActivity()).setSupportActionBar(toolbar);

        try {
            ((DetailActivityFavorite) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getMovieTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        return rootView;
    }
}

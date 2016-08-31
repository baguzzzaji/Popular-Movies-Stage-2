package com.example.bagus.moviedbv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
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

import com.example.bagus.moviedbv2.api.RetrofitHelper;
import com.example.bagus.moviedbv2.api.ReviewResults;
import com.example.bagus.moviedbv2.api.TmdbInterface;
import com.example.bagus.moviedbv2.api.TrailerResults;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private String TAG = DetailActivityFragment.class.getSimpleName();

    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private Context context;

    private RecyclerView recyclerView;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        movie = getActivity().getIntent().getParcelableExtra(MovieAdapter.MovieViewHolder.MOVIE);
        context = getContext();
        getTrailers(movie.getMovieId());
        getReviews(movie.getMovieId());
    }

    private void getReviews(String movieId) {
        if (RetrofitHelper.API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Configure your API key from themoviedb.org!", Toast.LENGTH_SHORT).show();
        }

        TmdbInterface tmdbInterface = RetrofitHelper.getClient().create(TmdbInterface.class);

        Call<ReviewResults> call = tmdbInterface.getReviews(movieId, RetrofitHelper.API_KEY);
        call.enqueue(new Callback<ReviewResults>() {
            @Override
            public void onResponse(Call<ReviewResults> call, Response<ReviewResults> response) {
                reviews = response.body().getResults();
                recyclerView.setAdapter(new ReviewAdapter(reviews, context, R.layout.review_item));
                Log.d(TAG, "Author name" + reviews.get(0).getReviewAuthor());
            }

            @Override
            public void onFailure(Call<ReviewResults> call, Throwable t) {
                Log.d(TAG, "Review downloads failed");
            }
        });
    }

    private void getTrailers(String movieId) {
        if (RetrofitHelper.API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "Configure your API key from themoviedb.org!", Toast.LENGTH_SHORT).show();
        }

        TmdbInterface tmdbInterface = RetrofitHelper.getClient().create(TmdbInterface.class);

        Call<TrailerResults> call = tmdbInterface.getTrailers(movieId, RetrofitHelper.API_KEY);
        call.enqueue(new Callback<TrailerResults>() {
            @Override
            public void onResponse(Call<TrailerResults> call, Response<TrailerResults> response) {
                trailers = response.body().getResults();
            }

            @Override
            public void onFailure(Call<TrailerResults> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        // Movie section
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

        // Trailers section
        TextView trailerOne = (TextView) rootView.findViewById(R.id.detail_movie_trailer_1);
        TextView trailerTwo = (TextView) rootView.findViewById(R.id.detail_movie_trailer_2);

        // Reviews section
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movie_reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

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
                    Toast.makeText(context, "Trailer not available", Toast.LENGTH_LONG).show();
                }
            }
        });

        trailerTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailers.get(1) != null){
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + trailers.get(1).getTrailerKey())));
                } else {
                    Toast.makeText(context, "Trailer not available", Toast.LENGTH_LONG).show();
                }
            }
        });


        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        ((DetailActivity) getActivity()).setSupportActionBar(toolbar);

        try {
            ((DetailActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) rootView.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(movie.getMovieTitle());
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        return rootView;
    }
}

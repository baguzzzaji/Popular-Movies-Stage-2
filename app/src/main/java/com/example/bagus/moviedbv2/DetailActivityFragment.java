package com.example.bagus.moviedbv2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.example.bagus.moviedbv2.api.RetrofitHelper;
import com.example.bagus.moviedbv2.api.ReviewResults;
import com.example.bagus.moviedbv2.api.TmdbInterface;
import com.example.bagus.moviedbv2.api.TrailerResults;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DetailActivityFragment extends Fragment {

    private String TAG = DetailActivityFragment.class.getSimpleName();

    private boolean twoPane;
    private Movie movie;
    private List<Trailer> trailers;
    private List<Review> reviews;
    private Context context;

    private RecyclerView recyclerView;

    private Realm realm;
    private RealmConfiguration realmConfiguration;

    public DetailActivityFragment() {
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        twoPane = MainActivity.isTwoPane();

        if (savedInstanceState == null) {
            Log.d(TAG, "savedInstanceState is null");
            if (twoPane){
                try {
                    movie = getArguments().getParcelable(MovieAdapter.MovieViewHolder.MOVIE);
                    Log.d(TAG, "Get movie from arguments");
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            } else {
                movie = getActivity().getIntent().getParcelableExtra(MovieAdapter.MovieViewHolder.MOVIE);
                Log.d(TAG, "Get movie from intent");
            }
        } else {
            Log.d(TAG, "savedInstanceState is not null");
            movie = savedInstanceState.getParcelable(MovieAdapter.MovieViewHolder.MOVIE);
            reviews = savedInstanceState.getParcelableArrayList("reviews");
            trailers = savedInstanceState.getParcelableArrayList("trailers");
            Log.d(TAG, "Get movie from savedInstanceState");
        }

        context = getContext();

        realmConfiguration = new RealmConfiguration
                .Builder(context)
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();

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
                
                RealmList<Review> realmList = new RealmList<Review>();
                for (Review review :
                        reviews) {
                    realmList.add(review);
                }
                movie.setReviews(realmList);
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
                RealmList<Trailer> realmList = new RealmList<Trailer>();
                for (Trailer trailer :
                        trailers) {
                    realmList.add(trailer);
                }
                movie.setTrailers(realmList);
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

        // Reviews section
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_movie_reviews);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));

        if (savedInstanceState == null) {
            getTrailers(movie.getMovieId());
            getReviews(movie.getMovieId());
        }

        recyclerView.setAdapter(new ReviewAdapter(reviews, context, R.layout.review_item));

        // Trailers section
        TextView trailerOne = (TextView) rootView.findViewById(R.id.detail_movie_trailer_1);
        TextView trailerTwo = (TextView) rootView.findViewById(R.id.detail_movie_trailer_2);

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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinator_layout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realm.executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        realm.copyToRealmOrUpdate(movie);
                    }
                });

                Snackbar.make(coordinatorLayout, "Movie saved!", Snackbar.LENGTH_LONG).show();
            }
        });

        FloatingActionButton fabShare = (FloatingActionButton) rootView.findViewById(R.id.fab_share);
        fabShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, movie.getMovieTitle());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, movie.getMovieOverview());
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }
        });

        if (!twoPane) {
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
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Review> reviews = (ArrayList<Review>) this.reviews;
        ArrayList<Trailer> trailers = (ArrayList<Trailer>) this.trailers;

        outState.putParcelable(MovieAdapter.MovieViewHolder.MOVIE, movie);
        outState.putParcelableArrayList("reviews", reviews);
        outState.putParcelableArrayList("trailers", trailers);
    }
}

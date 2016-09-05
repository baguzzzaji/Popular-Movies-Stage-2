package com.example.bagus.moviedbv2;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import co.moonmonkeylabs.realmrecyclerview.RealmRecyclerView;
import io.realm.Realm;
import io.realm.RealmBasedRecyclerViewAdapter;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.RealmViewHolder;


public class FavoritesFragment extends Fragment {
    private String TAG = FavoritesFragment.class.getSimpleName();

    private Realm realm;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(getContext()).build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        RealmResults<Movie> movies = realm.where(Movie.class).findAll();
        MovieRecyclerViewAdapter adapter = new MovieRecyclerViewAdapter(getContext(), movies);
        RealmRecyclerView rvMovies = (RealmRecyclerView) rootView.findViewById(R.id.rv_favourites_movies);
        rvMovies.setAdapter(adapter);
        return rootView;
    }

    public class MovieRecyclerViewAdapter extends RealmBasedRecyclerViewAdapter<Movie, MovieRecyclerViewAdapter.ViewHolder> {

        public MovieRecyclerViewAdapter(Context context, RealmResults<Movie> realmResults) {
            super(context, realmResults, true, false);
        }

        public class ViewHolder extends RealmViewHolder {
            public ImageView posterView;

            public ViewHolder(FrameLayout container) {
                super(container);
                this.posterView = (ImageView) container.findViewById(R.id.movie_poster);
            }
        }

        @Override
        public ViewHolder onCreateRealmViewHolder(ViewGroup viewGroup, int i) {
            View v = inflater.inflate(R.layout.movie_item, viewGroup, false);
            return new ViewHolder((FrameLayout) v);
        }

        @Override
        public void onBindRealmViewHolder(ViewHolder viewHolder, int i) {
            final Movie movie = realmResults.get(i);
            Picasso.with(getContext())
                    .load("http://image.tmdb.org/t/p/w185"+movie.getMoviePoster())
                    .into(viewHolder.posterView);
        }

    }

}

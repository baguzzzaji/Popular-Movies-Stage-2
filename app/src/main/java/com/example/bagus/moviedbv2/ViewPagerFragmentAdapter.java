package com.example.bagus.moviedbv2;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.io.Serializable;

public class ViewPagerFragmentAdapter extends FragmentStatePagerAdapter{

    protected String tabTitles[] = new String[] {"Popular", "Top Rated", "Favourites"};

    public ViewPagerFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PopularMoviesFragment();
            case 1:
                return new TopRatedFragment();
            case 2:
                return new FavoritesFragment();
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return 3;
    }
}

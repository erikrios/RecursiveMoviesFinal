package com.erikriosetiawan.recursivemoviesfinal.fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.astuetz.PagerSlidingTabStrip;
import com.erikriosetiawan.recursivemoviesfinal.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoritesFragment extends Fragment {

    private ViewPager viewPager;

    public FavoritesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_favorites, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewPager = getActivity().findViewById(R.id.pager);
        viewPager.setAdapter(new ViewPagerAdapter(getFragmentManager()));

        PagerSlidingTabStrip tabs = getActivity().findViewById(R.id.tabs);
        tabs.setShouldExpand(true);
        tabs.setIndicatorColor(getResources().getColor(R.color.colorPrimaryDark));
        tabs.setViewPager(viewPager);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return (position == 0) ? getResources().getString(R.string.nav_movies) : getResources().getString(R.string.nav_tv_shows);
        }

        @Override
        public Fragment getItem(int i) {
            return (i == 0) ? new FavoriteMoviesFragment() : new FavoriteTvShowsFragment();
        }


        @Override
        public int getCount() {
            return 2;
        }
    }
}

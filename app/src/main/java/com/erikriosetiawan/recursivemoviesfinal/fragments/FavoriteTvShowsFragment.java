package com.erikriosetiawan.recursivemoviesfinal.fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.adapters.FavoriteTvShowAdapter;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteTvShowsDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteTvShowsFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteTvShowAdapter favoriteTvShowAdapter;
    private ArrayList<TvShow> tvShows = new ArrayList<>();
    private FavoriteTvShowsDatabaseHelper favoriteTvShowsDatabaseHelper;

    public FavoriteTvShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_favorite_tv_shows, container, false);
        favoriteTvShowsDatabaseHelper = new FavoriteTvShowsDatabaseHelper(getActivity());
        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_favorite_tv_shows);
        getAllFavorites();
        setRecyclerList();
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFavorites() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                tvShows.clear();
                tvShows.addAll(favoriteTvShowsDatabaseHelper.getAllFavorite());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                favoriteTvShowAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void setRecyclerList() {
        if (favoriteTvShowAdapter == null) {
            favoriteTvShowAdapter = new FavoriteTvShowAdapter(getActivity(), tvShows);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(favoriteTvShowAdapter);
        } else {
            favoriteTvShowAdapter.notifyDataSetChanged();
        }
    }

}

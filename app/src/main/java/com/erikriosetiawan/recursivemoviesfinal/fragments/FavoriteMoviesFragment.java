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
import com.erikriosetiawan.recursivemoviesfinal.adapters.FavoriteMovieAdapter;
import com.erikriosetiawan.recursivemoviesfinal.db.FavoriteMoviesDatabaseHelper;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteMoviesFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteMovieAdapter favoriteMovieAdapter;
    private ArrayList<Movie> movies = new ArrayList<>();
    private FavoriteMoviesDatabaseHelper favoriteMoviesDatabaseHelper;

    public FavoriteMoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_favorite_movies, container, false);
        favoriteMoviesDatabaseHelper = new FavoriteMoviesDatabaseHelper(getActivity());
        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_favorite_movie);
        getAllFavorite();
        setRecyclerList();
        return rootView;
    }

    @SuppressLint("StaticFieldLeak")
    private void getAllFavorite() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                movies.clear();
                movies.addAll(favoriteMoviesDatabaseHelper.getAllFavorite());
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                favoriteMovieAdapter.notifyDataSetChanged();
            }
        }.execute();
    }

    private void setRecyclerList() {
        if (favoriteMovieAdapter == null) {
            favoriteMovieAdapter = new FavoriteMovieAdapter(getActivity(), movies);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(favoriteMovieAdapter);
        } else {
            favoriteMovieAdapter.notifyDataSetChanged();
        }
    }

}

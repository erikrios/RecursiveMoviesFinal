package com.erikriosetiawan.recursivemoviesfinal.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.erikriosetiawan.recursivemoviesfinal.viewmodel.MovieViewModel;
import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.adapters.MovieAdapter;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;
import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoviesFragment extends Fragment {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private ProgressBar progressBar;
    private ArrayList<Movie> movies = new ArrayList<>();
    private MovieViewModel movieViewModel;

    public MoviesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_movies, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_movie);
        progressBar = rootView.findViewById(R.id.progress_bar_movie);

        movieViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MovieViewModel.class);
        movieViewModel.open();

        movieViewModel.getMovies().observe(this, new Observer<MovieResult>() {
            @Override
            public void onChanged(@Nullable MovieResult movieResult) {
                assert movieResult != null;
                ArrayList<Movie> resultMovie = movieResult.getResults();
                movies.addAll(resultMovie);
                movieAdapter.notifyDataSetChanged();

                movieViewModel.close();
            }
        });
        movieViewModel.getIsFetching().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    progressBarStatus(true);
                } else {
                    progressBarStatus(false);
                }
            }
        });
        setRecyclerGrid();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void progressBarStatus(boolean status) {
        if (status) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void setRecyclerGrid() {
        if (movieAdapter == null) {
            movieAdapter = new MovieAdapter(getActivity(), movies);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
            recyclerView.setAdapter(movieAdapter);
        } else {
            movieAdapter.notifyDataSetChanged();
        }
    }
}

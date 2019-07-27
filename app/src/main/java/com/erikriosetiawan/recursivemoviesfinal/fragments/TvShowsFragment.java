package com.erikriosetiawan.recursivemoviesfinal.fragments;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.erikriosetiawan.recursivemoviesfinal.R;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;
import com.erikriosetiawan.recursivemoviesfinal.viewmodel.TvShowViewModel;
import com.erikriosetiawan.recursivemoviesfinal.adapters.TvShowAdapter;

import java.util.ArrayList;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class TvShowsFragment extends Fragment {

    private RecyclerView recyclerView;
    private TvShowAdapter tvShowAdapter;
    private ProgressBar progressBar;
    private ArrayList<TvShow> tvShows = new ArrayList<>();
    private TvShowViewModel tvShowViewModel;

    public TvShowsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_tv_shows, container, false);

        recyclerView = rootView.findViewById(R.id.recycler_view_fragment_tv_shows);
        progressBar = rootView.findViewById(R.id.progress_bar_tv_shows);

        tvShowViewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(TvShowViewModel.class);
        tvShowViewModel.open();

        tvShowViewModel.getTvShows().observe(this, new Observer<TvShowResult>() {
            @Override
            public void onChanged(@Nullable TvShowResult tvShowResult) {
                ArrayList<TvShow> resultTvShow = tvShowResult.getResults();
                tvShows.addAll(resultTvShow);
                tvShowAdapter.notifyDataSetChanged();

                tvShowViewModel.close();
            }
        });
        tvShowViewModel.getIsFetching().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean aBoolean) {
                if (aBoolean) {
                    progressBarStatus(true);
                } else {
                    progressBarStatus(false);
                }
            }
        });
        setRecyclerList();
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

    private void setRecyclerList() {
        if (tvShowAdapter == null) {
            tvShowAdapter = new TvShowAdapter(getActivity(), tvShows);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(tvShowAdapter);
        } else {
            tvShowAdapter.notifyDataSetChanged();
        }
    }
}

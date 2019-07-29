package com.erikriosetiawan.recursivemoviesfinal;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.erikriosetiawan.recursivemoviesfinal.adapters.SearchAdapter;
import com.erikriosetiawan.recursivemoviesfinal.models.Movie;
import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShow;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;
import com.erikriosetiawan.recursivemoviesfinal.viewmodel.MovieViewModel;
import com.erikriosetiawan.recursivemoviesfinal.viewmodel.TvShowViewModel;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    RecyclerView recyclerViewSearch;
    private MovieViewModel mMovieViewModel;
    private TvShowViewModel mTvShowViewModel;

    ArrayList<Movie> movies = new ArrayList<>();
    ArrayList<TvShow> tvShows = new ArrayList<>();
    SearchAdapter mSearchAdapter;
    String query, navigationSelect;

    final ArrayList<Movie> listMovie = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        query = getIntent().getStringExtra("QUERY");
        navigationSelect = getIntent().getStringExtra("NAVIGATION_SELECT");

        recyclerViewSearch = findViewById(R.id.recycler_view_search_activity);

        if (navigationSelect.equals("Movies")) {
            searchMovie(query);
            setActionBar(getResources().getString(R.string.movie_search_result));
        } else {
            searchTvShow(query);
            setActionBar(getResources().getString(R.string.tv_show_search_result));
        }
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mSearchAdapter.clearList(navigationSelect);
    }

    private void searchMovie(String query) {
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getSearchMovies(query).observe(this, new Observer<MovieResult>() {
            @Override
            public void onChanged(@Nullable MovieResult movieResult) {
                assert movieResult != null;
                ArrayList<Movie> resultMovie = movieResult.getResults();
                movies.clear();
                movies.addAll(resultMovie);
                mSearchAdapter.notifyDataSetChanged();
                mMovieViewModel.closeSearch();
            }
        });

        if (mSearchAdapter == null) {
            mSearchAdapter = new SearchAdapter(this);
            mSearchAdapter.setListMovies(movies);
            recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerViewSearch.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    private void searchTvShow(String query) {
        mTvShowViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        mTvShowViewModel.getSearchTvShows(query).observe(this, new Observer<TvShowResult>() {
            @Override
            public void onChanged(@Nullable TvShowResult tvShowResult) {
                assert tvShowResult != null;
                ArrayList<TvShow> resultTvShow = tvShowResult.getResults();
                tvShows.clear();
                tvShows.addAll(resultTvShow);
                mSearchAdapter.notifyDataSetChanged();
                mTvShowViewModel.closeSearch();
            }
        });

        if (mSearchAdapter == null) {
            mSearchAdapter = new SearchAdapter(this);
            mSearchAdapter.setListTvShows(tvShows);
            recyclerViewSearch.setLayoutManager(new GridLayoutManager(this, 2));
            recyclerViewSearch.setAdapter(mSearchAdapter);
        } else {
            mSearchAdapter.notifyDataSetChanged();
        }
    }

    private void setActionBar(String title) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(title);
        }
    }
}

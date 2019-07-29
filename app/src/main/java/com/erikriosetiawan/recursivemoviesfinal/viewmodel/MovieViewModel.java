package com.erikriosetiawan.recursivemoviesfinal.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.erikriosetiawan.recursivemoviesfinal.BuildConfig;
import com.erikriosetiawan.recursivemoviesfinal.apiservices.MovieRepository;
import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;

public class MovieViewModel extends ViewModel {

    private MutableLiveData<MovieResult> mutableLiveDataMovie;
    private MutableLiveData<MovieResult> mutableLiveDataMovieSearch;
    private MutableLiveData<Boolean> isFetching = new MutableLiveData<>();
    private static final String API_KEY = BuildConfig.API_KEY;

    public LiveData<MovieResult> getMovies() {
        setIsFetching(true);
        if (mutableLiveDataMovie != null) {
            return mutableLiveDataMovie;
        }
        MovieRepository movieRepository = MovieRepository.getInstance();
        mutableLiveDataMovie = movieRepository.getMovie(API_KEY, "en-US");
        return mutableLiveDataMovie;
    }

    public LiveData<MovieResult> getSearchMovies(String query) {
        setIsFetching(true);
        if (mutableLiveDataMovie != null) {
            return mutableLiveDataMovieSearch;
        }
        MovieRepository movieRepository = MovieRepository.getInstance();
        mutableLiveDataMovieSearch = movieRepository.getSearchMovie(API_KEY, "en-US", query);
        return mutableLiveDataMovieSearch;
    }

    public LiveData<Boolean> getIsFetching() {
        return isFetching;
    }

    private void setIsFetching(boolean isFetching) {
        this.isFetching.postValue(isFetching);
    }

    public void open() {
        setIsFetching(true);
        if (mutableLiveDataMovie != null) {
            return;
        }
        MovieRepository movieRepository = MovieRepository.getInstance();
        mutableLiveDataMovie = movieRepository.getMovie(API_KEY, "en-US");
    }

    public void close() {
        if (mutableLiveDataMovie.getValue() != null) {
            setIsFetching(false);
        }
    }

    public void closeSearch() {
        if (mutableLiveDataMovieSearch.getValue() != null) {
            setIsFetching(false);
        }
    }
}
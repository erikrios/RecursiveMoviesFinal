package com.erikriosetiawan.recursivemoviesfinal.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.erikriosetiawan.recursivemoviesfinal.BuildConfig;
import com.erikriosetiawan.recursivemoviesfinal.apiservices.TvShowRepository;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;

public class TvShowViewModel extends ViewModel {

    private MutableLiveData<TvShowResult> mutableLiveDataTvShow;
    private MutableLiveData<TvShowResult> mutableLiveDataTvShowSearch;
    private MutableLiveData<Boolean> isFetching = new MutableLiveData<>();
    private static final String API_KEY = BuildConfig.API_KEY;

    public LiveData<TvShowResult> getTvShows() {
        setIsFetching(true);
        if (mutableLiveDataTvShow != null) {
            return mutableLiveDataTvShow;
        }
        TvShowRepository tvShowRepository = TvShowRepository.getInstance();
        mutableLiveDataTvShow = tvShowRepository.getTvShow(API_KEY, "en-US");
        return mutableLiveDataTvShow;
    }

    public LiveData<TvShowResult> getSearchTvShows(String query) {
        setIsFetching(true);
        if (mutableLiveDataTvShow != null) {
            return mutableLiveDataTvShowSearch;
        }
        TvShowRepository tvShowRepository = TvShowRepository.getInstance();
        mutableLiveDataTvShowSearch = tvShowRepository.getSearchTvShow(API_KEY, "en-US", query);
        return mutableLiveDataTvShowSearch;
    }

    public LiveData<Boolean> getIsFetching() {
        return isFetching;
    }

    private void setIsFetching(boolean isFetching) {
        this.isFetching.postValue(isFetching);
    }

    public void open() {
        setIsFetching(true);
        if (mutableLiveDataTvShow != null) {
            return;
        }
        TvShowRepository tvShowRepository = TvShowRepository.getInstance();
        mutableLiveDataTvShow = tvShowRepository.getTvShow(API_KEY, "en-US");
    }

    public void close() {
        if (mutableLiveDataTvShow.getValue() != null) {
            setIsFetching(false);
        }
    }

    public void closeSearch() {
        if (mutableLiveDataTvShowSearch.getValue() != null) {
            setIsFetching(false);
        }
    }
}
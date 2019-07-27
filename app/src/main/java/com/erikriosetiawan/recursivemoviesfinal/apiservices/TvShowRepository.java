package com.erikriosetiawan.recursivemoviesfinal.apiservices;

import android.arch.lifecycle.MutableLiveData;

import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TvShowRepository {

    MutableLiveData<TvShowResult> dataTvShow = new MutableLiveData<>();
    private static TvShowRepository tvShowRepository;
    private Api tvShowApi;

    public TvShowRepository() {
        tvShowApi = RetrofitServices.createService(Api.class);
    }

    public static TvShowRepository getInstance() {
        if (tvShowRepository == null) {
            tvShowRepository = new TvShowRepository();
        }
        return tvShowRepository;
    }

    public void setDataTvShow(TvShowResult dataTvShow) {
        this.dataTvShow.setValue(dataTvShow);
    }

    public MutableLiveData<TvShowResult> getTvShow(String apiKey, String language) {
        tvShowApi.getTvShowList(apiKey, language).enqueue(new Callback<TvShowResult>() {
            @Override
            public void onResponse(Call<TvShowResult> call, Response<TvShowResult> response) {
                if (response.isSuccessful()) {
                    setDataTvShow(response.body());
                }
            }

            @Override
            public void onFailure(Call<TvShowResult> call, Throwable t) {

            }
        });
        return dataTvShow;
    }
}

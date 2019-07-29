package com.erikriosetiawan.recursivemoviesfinal.apiservices;

import android.arch.lifecycle.MutableLiveData;

import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    private MutableLiveData<MovieResult> dataMovie = new MutableLiveData<>();
    private MutableLiveData<MovieResult> dataSearchMovie = new MutableLiveData<>();
    private static MovieRepository movieRepository;
    private Api movieApi;

    private MovieRepository() {
        movieApi = RetrofitServices.createService(Api.class);
    }

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            movieRepository = new MovieRepository();
        }
        return movieRepository;
    }

    private void setDataMovie(MovieResult dataMovie) {
        this.dataMovie.setValue(dataMovie);
    }

    private void setDataSearchMovie(MovieResult dataMovie) {
        this.dataSearchMovie.setValue(dataMovie);
    }

    public MutableLiveData<MovieResult> getMovie(String apiKey, String language) {
        movieApi.getMovieList(apiKey, language).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (response.isSuccessful()) {
                    setDataMovie(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
        return dataMovie;
    }

    public MutableLiveData<MovieResult> getSearchMovie(String apiKey, String language, String query) {
        movieApi.getSearchMovieList(apiKey, language, query).enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(Call<MovieResult> call, Response<MovieResult> response) {
                if (response.isSuccessful()) {
                    setDataSearchMovie(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieResult> call, Throwable t) {

            }
        });
        return dataSearchMovie;
    }
}
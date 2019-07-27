package com.erikriosetiawan.recursivemoviesfinal.apiservices;

import android.arch.lifecycle.MutableLiveData;

import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieRepository {

    MutableLiveData<MovieResult> dataMovie = new MutableLiveData<>();
    private static MovieRepository movieRepository;
    private Api movieApi;

    public MovieRepository() {
        movieApi = RetrofitServices.createService(Api.class);
    }

    public static MovieRepository getInstance() {
        if (movieRepository == null) {
            movieRepository = new MovieRepository();
        }
        return movieRepository;
    }

    public void setDataMovie(MovieResult dataMovie) {
        this.dataMovie.setValue(dataMovie);
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
}

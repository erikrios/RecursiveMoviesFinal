package com.erikriosetiawan.recursivemoviesfinal.apiservices;

import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("discover/movie")
    Call<MovieResult> getMovieList(@Query("api_key") String apiKey,
                                   @Query("language") String language);

    @GET("discover/tv")
    Call<TvShowResult> getTvShowList(@Query("api_key") String apiKey,
                                     @Query("language") String language);

    @GET("search/movie")
    Call<MovieResult> getSearchMovieList(@Query("api_key") String apiKey,
                                         @Query("language") String language,
                                         @Query("query") String query);

    @GET("search/tv")
    Call<TvShowResult> getSearchSeriesList(@Query("api_key") String apiKey,
                                           @Query("language") String language,
                                           @Query("query") String query);
}

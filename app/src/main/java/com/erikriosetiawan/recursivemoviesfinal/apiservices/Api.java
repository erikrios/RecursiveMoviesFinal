package com.erikriosetiawan.recursivemoviesfinal.apiservices;

import com.erikriosetiawan.recursivemoviesfinal.models.MovieResult;
import com.erikriosetiawan.recursivemoviesfinal.models.TvShowResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface Api {
    @GET("movie")
    Call<MovieResult> getMovieList(@Query("api_key") String apiKey,
                                   @Query("language") String language);

    @GET("tv")
    Call<TvShowResult> getTvShowList(@Query("api_key") String apiKey,
                                     @Query("language") String language);
}

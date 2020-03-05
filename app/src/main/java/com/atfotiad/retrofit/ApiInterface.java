package com.atfotiad.retrofit;

import com.atfotiad.retrofit.model.MoviesResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    /*@GET(".")
    Call<MoviesResponse> getSampleMovies(@Query("apikey") String apiKey
            ,@Query("s") String sample);
*/
    @GET(".")
    Observable<MoviesResponse> getSampleMovies(@Query("apikey") String apiKey
            , @Query("s") String sample);

}

package com.veygard.movies_recycler.data.remote.api

import com.veygard.movies_recycler.data.remote.model.GetMoviesResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MoviesApi {
    @Headers(
        "Content-Type: application/json",
        "Prefer: code=200, example=success"
    )

    @GET("/svc/movies/v2/reviews/all.json")
    suspend fun getMovies(@Query("offset") pageOffset:Int = 0): Response<GetMoviesResponse>

    @GET("/svc/movies/v2/reviews/search.json")
    suspend fun searchMovies(@Query("query") name:String): Response<GetMoviesResponse>

}
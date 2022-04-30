package com.veygard.movies_recycler.domain.repository

import com.veygard.movies_recycler.data.remote.api.MoviesApi
import com.veygard.movies_recycler.data.remote.supports.MovieApiTimeoutControl
import javax.inject.Inject

class MoviesRepositoryImpl @Inject constructor(private val moviesApi:MoviesApi, private val timeout: MovieApiTimeoutControl):MoviesRepository {

    override suspend fun getMovies(pageOffset:Int): GetMoviesResult {
        var result: GetMoviesResult = GetMoviesResult.EnqueueError("MoviesRepositoryImpl getMovies not working")
        timeout.checkCounter()
        try {
            val call =  moviesApi.getMovies(pageOffset)
            when{
                call.isSuccessful->{
                    call.body()?.let {
                        result = GetMoviesResult.Success(it)
                    } ?: run {
                        result = GetMoviesResult.ServerError(
                            error = call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                        )
                    }
                }
                call.code() in 400..499 ->{
                    result = GetMoviesResult.ServerError(
                        error = "Client Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )
                }
                call.code() in 500..599 ->{
                    result = GetMoviesResult.ServerError(
                        error = "Server Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )

                }
                else -> {
                    result = GetMoviesResult.ServerError(
                        error = "Unknown Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )

                }
            }
        } catch (e: Exception) {
            result = GetMoviesResult.ConnectionError()
        }
        return result

    }

    override suspend fun searchMovies(name: String): GetMoviesResult {
        var result: GetMoviesResult = GetMoviesResult.EnqueueError("MoviesRepositoryImpl getMovies not working")
        try {
            val call =  moviesApi.searchMovies(name)
            when{
                call.isSuccessful->{
                    call.body()?.let {
                        result = GetMoviesResult.Success(it)
                    } ?: run {
                        result = GetMoviesResult.ServerError(
                            error = call.errorBody()?.source()?.buffer?.snapshot()?.utf8()
                        )
                    }
                }
                call.code() in 400..499 ->{
                    result = GetMoviesResult.ServerError(
                        error = "Client Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )
                }
                call.code() in 500..599 ->{
                    result = GetMoviesResult.ServerError(
                        error = "Server Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )

                }
                else -> {
                    result = GetMoviesResult.ServerError(
                        error = "Unknown Error: ${call.errorBody()?.source()?.buffer?.snapshot()?.utf8()}"
                    )

                }
            }
        } catch (e: Exception) {
            result = GetMoviesResult.ConnectionError()
        }
        return result
    }

}
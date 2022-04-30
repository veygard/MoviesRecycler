package com.veygard.movies_recycler.domain.repository

interface MoviesRepository {
    suspend fun getMovies(pageOffset:Int): GetMoviesResult
    suspend fun searchMovies(name:String, pageOffset:Int): GetMoviesResult
}
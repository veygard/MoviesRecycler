package com.veygard.movies_recycler.domain.repository

interface MoviesRepository {
    suspend fun getMovies(): GetMoviesResult
}
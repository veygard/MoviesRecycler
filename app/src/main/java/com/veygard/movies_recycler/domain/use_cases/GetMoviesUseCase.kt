package com.veygard.movies_recycler.domain.use_cases

import com.veygard.movies_recycler.domain.repository.MoviesRepository

class GetMoviesUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun start() = moviesRepository.getMovies()
}
package com.veygard.movies_recycler.domain.use_cases

import com.veygard.movies_recycler.domain.repository.MoviesRepository

class SearchMovieUseCase(private val moviesRepository: MoviesRepository) {
    suspend fun start(name:String, pageOffset:Int) = moviesRepository.searchMovies(name,pageOffset)
}
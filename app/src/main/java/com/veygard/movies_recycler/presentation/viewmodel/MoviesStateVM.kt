package com.veygard.movies_recycler.presentation.viewmodel

import com.veygard.movies_recycler.data.remote.model.GetMoviesResponse
import com.veygard.movies_recycler.domain.repository.GetMoviesResult

sealed class MoviesStateVM {
    data class GotMovies(val result: GetMoviesResponse): MoviesStateVM()
    data class Error(val result: GetMoviesResult, val msg:String?): MoviesStateVM()
    object Loading: MoviesStateVM()

}
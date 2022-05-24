package com.veygard.movies_recycler.presentation.viewmodel

import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.repository.GetMoviesResult

sealed class MoviesStateVM(open val movieList: List<MovieWithShimmer>? = null) {
    data class GotMovies(val list: List<MovieWithShimmer>): MoviesStateVM(movieList = list)
    data class SearchMovies(val list: List<MovieWithShimmer>): MoviesStateVM(movieList = list)
    data class Loading(val list: List<MovieWithShimmer>): MoviesStateVM(movieList = list)
    object MoreMoviesError: MoviesStateVM()
    object ClearMovieList: MoviesStateVM()
    data class Error(val result: GetMoviesResult, val msg:String?): MoviesStateVM()
    object FirstMovieListRdy: MoviesStateVM()

}
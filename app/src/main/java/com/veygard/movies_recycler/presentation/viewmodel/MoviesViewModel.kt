package com.veygard.movies_recycler.presentation.viewmodel

import android.util.Log
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.model.toMovieShimmerList
import com.veygard.movies_recycler.domain.repository.GetMoviesResult
import com.veygard.movies_recycler.domain.use_cases.MoviesUseCases
import com.veygard.movies_recycler.util.SnackbarTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private var pageOffset = 0
    private var searchPageOffset = 0
    private var searchValue = ""
    private var irSearchOn = false

    private val movieListStorage = mutableListOf<MovieWithShimmer>()
    private var movieSearchListStorage = mutableListOf<MovieWithShimmer>()

    private val _getMoviesResponse: MutableLiveData<MoviesStateVM?> = MutableLiveData(null)
    val getMoviesResponse: LiveData<MoviesStateVM?> = _getMoviesResponse

    private val _showSnackbar: MutableLiveData<SnackbarTypes?> = MutableLiveData(null)
    val showSnackbar: LiveData<SnackbarTypes?> = _showSnackbar

    private val _requestIsRdy = MutableLiveData(true)
    val requestIsRdy: LiveData<Boolean> = _requestIsRdy

    private val _showLoadingBar = MutableLiveData(false)
    val showLoadingBar: LiveData<Boolean> = _showLoadingBar


    private val _hasMoreMovies = MutableLiveData(false)
    val hasMoreMovies: LiveData<Boolean> = _hasMoreMovies


    fun getMovies() {
        viewModelScope.launch {
            when (val result = moviesUseCases.getMoviesUseCase.start(pageOffset)) {
                is GetMoviesResult.Success -> {
                    pageOffset += result.response.num_results ?: 0
                    _hasMoreMovies.value = result.response.has_more ?: false
                    _requestIsRdy.value = true
                    removeShimmers()
                    result.response.results?.let { movieListStorage.addAll(it.toMovieShimmerList()) }
                    _getMoviesResponse.value = MoviesStateVM.GotMovies(movieListStorage)
                }
                else -> {
                    if (movieListStorage.isEmpty()) _getMoviesResponse.value =
                        MoviesStateVM.Error(result, result.error)
                    else {
                        _requestIsRdy.value = true
                        _showSnackbar.value = SnackbarTypes.ConnectionError
                        _getMoviesResponse.value = MoviesStateVM.MoreMoviesError
                    }
                }
            }
        }
    }

    fun loadMore() {
        addShimmers()
        _getMoviesResponse.value = MoviesStateVM.Loading(
            if(irSearchOn) movieSearchListStorage else movieListStorage
        )

        if (irSearchOn) {
            if (_hasMoreMovies.value) {
                searchMovie(searchValue, true)
            }
        } else {
            if (_hasMoreMovies.value) {
                _requestIsRdy.value = false
                getMovies()
            } else {
                _showSnackbar.value = SnackbarTypes.NoMoviesLeft
            }
        }
    }

    fun searchMovie(name: String, isAdditionalLoading: Boolean = false) {
        Log.d("search_test", "searchMovie Viewmodel: $name")
        viewModelScope.launch {
            _requestIsRdy.value = false
            if (name != searchValue) searchPageOffset = 0
            searchValue = name
            irSearchOn = true

            if (!isAdditionalLoading) {
                searchPageOffset = 0
                _showLoadingBar.value = true
                _getMoviesResponse.value = MoviesStateVM.ClearMovieList
            }

            when (val result = moviesUseCases.searchMovieUseCase.start(name, searchPageOffset)) {
                is GetMoviesResult.Success -> {
                    _requestIsRdy.value = true
                    _hasMoreMovies.value = result.response.has_more ?: false
                    searchPageOffset += result.response.num_results ?: 0
                    _showLoadingBar.value = false
                    when (isAdditionalLoading) {
                        true -> {
                            removeShimmers()
                            movieSearchListStorage.addAll(
                                result.response.results?.toMovieShimmerList()
                                    ?: emptyList()
                            )
                            _getMoviesResponse.value =
                                MoviesStateVM.SearchMovies(movieSearchListStorage)

                        }
                        false -> {
                            removeShimmers()
                            movieSearchListStorage.addAll(
                                result.response.results?.toMovieShimmerList() ?: emptyList()
                            )
                            _getMoviesResponse.value =
                                MoviesStateVM.SearchMovies(movieSearchListStorage)
                        }
                    }
                }
                else -> {
                    _requestIsRdy.value = false
                    _showLoadingBar.value = false
                    _showSnackbar.value = SnackbarTypes.ConnectionError
                    _getMoviesResponse.value = MoviesStateVM.SearchMovies(emptyList())
                }
            }
        }
    }

    private fun removeShimmers() {
        when (irSearchOn) {
            true -> movieSearchListStorage.removeAll { it is MovieWithShimmer.Shimmer }
            false -> movieListStorage.removeAll { it is MovieWithShimmer.Shimmer }
        }
    }

    private fun addShimmers() {
        for (i in 0..20) {
            when (irSearchOn) {
                true -> movieSearchListStorage.add(MovieWithShimmer.Shimmer)
                false -> movieListStorage.add(MovieWithShimmer.Shimmer)
            }
        }
    }

    fun turnOffSearch() {
        irSearchOn = false
        _hasMoreMovies.value = true
        searchPageOffset = 0
        movieSearchListStorage = mutableListOf()
        if (movieListStorage.isNotEmpty()) _getMoviesResponse.value =
            MoviesStateVM.GotMovies(movieListStorage)
        else getMovies()
    }
}
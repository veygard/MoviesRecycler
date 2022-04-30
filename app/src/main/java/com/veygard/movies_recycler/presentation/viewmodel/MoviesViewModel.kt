package com.veygard.movies_recycler.presentation.viewmodel

import android.util.Log
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.model.RowType
import com.veygard.movies_recycler.domain.repository.GetMoviesResult
import com.veygard.movies_recycler.domain.use_cases.MoviesUseCases
import com.veygard.movies_recycler.util.SnackbarTypes
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel() {

    private var pageOffset = 0
    private var searchPageOffset = 0
    private var movieList = mutableListOf<Movie>()
    private var searchValue= ""


    private val _getMoviesResponse: MutableLiveData<MoviesStateVM?> = MutableLiveData(null)
    val getMoviesResponse: LiveData<MoviesStateVM?> = _getMoviesResponse

    private val _showSnackbar: MutableLiveData<SnackbarTypes?> = MutableLiveData(null)
    val showSnackbar: LiveData<SnackbarTypes?> = _showSnackbar

    private val _loadingIsNotBusy = MutableLiveData(true)
    val loadingIsNotBusy: LiveData<Boolean> = _loadingIsNotBusy

    private val _showLoadingBar = MutableLiveData(false)
    val showLoadingBar: LiveData<Boolean> =  _showLoadingBar

    private val _irSearchOn = MutableLiveData(false)
    val irSearchOn: LiveData<Boolean> =  _irSearchOn

    private val _hasMoreMovies = MutableLiveData(false)
    val hasMoreMovies: LiveData<Boolean> =  _hasMoreMovies


    fun getMovies(isAdditionalLoading: Boolean = false) {
        viewModelScope.launch {
            when (val result = moviesUseCases.getMoviesUseCase.start(pageOffset)) {
                is GetMoviesResult.Success -> {
                    result.response.results?.forEach {
                        if (!movieList.contains(it)) movieList.add(it)
                    }
                    pageOffset += result.response.num_results ?: 0
                    _hasMoreMovies.value = result.response.has_more ?: false
                    _loadingIsNotBusy.value = true
                    when (isAdditionalLoading) {
                        true -> _getMoviesResponse.value =
                            MoviesStateVM.MoreMovies(result.response.results)
                        false -> _getMoviesResponse.value = MoviesStateVM.GotMovies(movieList)
                    }
                }
                else -> {
                    if (movieList.isEmpty()) _getMoviesResponse.value =
                        MoviesStateVM.Error(result, result.error)
                    else {
                        _loadingIsNotBusy.value = true
                        _showSnackbar.value = SnackbarTypes.ConnectionError
                        _getMoviesResponse.value = MoviesStateVM.MoreMoviesError
                    }
                }
            }
        }
    }

    fun loadMore() {
        if(_irSearchOn.value){
            if (_hasMoreMovies.value) {
                searchMovie(searchValue)
            }
        }else {
            if (_hasMoreMovies.value) {
                _loadingIsNotBusy.value = false
                getMovies(true)
            } else {
                _showSnackbar.value = SnackbarTypes.NoMoviesLeft
            }
        }
    }

    fun searchMovie(name: String) {
        viewModelScope.launch {
            searchValue= name
            _irSearchOn.value= true

            _showLoadingBar.value = true
            when (val result = moviesUseCases.searchMovieUseCase.start(name)) {
                is GetMoviesResult.Success -> {
                    _hasMoreMovies.value = result.response.has_more ?: false
                    searchPageOffset += result.response.num_results ?: 0
                    _getMoviesResponse.value =
                        MoviesStateVM.SearchMovies(result.response.results ?: emptyList())
                    _showLoadingBar.value = false
                }
                else -> {
                    _showLoadingBar.value = false
                    _showSnackbar.value = SnackbarTypes.ConnectionError
                    _getMoviesResponse.value = MoviesStateVM.SearchMovies(emptyList())
                }
            }
        }
    }

    fun turnOffSearch(){
        _irSearchOn.value= false
        _hasMoreMovies.value = true
        if(movieList.isNotEmpty()) _getMoviesResponse.value = MoviesStateVM.GotMovies(movieList)
        else getMovies()
    }
}
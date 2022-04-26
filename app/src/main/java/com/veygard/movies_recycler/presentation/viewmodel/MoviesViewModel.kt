package com.veygard.movies_recycler.presentation.viewmodel

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
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel(){

    private var hasMoreMovies = true
    private var pageOffset = 0
    private var movieList = mutableListOf<Movie>()
    private var movieListShimmer = mutableListOf<MovieWithShimmer>()

    private val _getMoviesResponse: MutableLiveData<MoviesStateVM?> = MutableLiveData(null)
    val getMoviesResponse: LiveData<MoviesStateVM?> = _getMoviesResponse

    private val _loadingIsNotBusy = MutableLiveData(true)
    val loadingIsNotBusy: LiveData<Boolean> = _loadingIsNotBusy

    private val _showSnackbar: MutableLiveData<SnackbarTypes?> = MutableLiveData(null)
    val showSnackbar: LiveData<SnackbarTypes?>  = _showSnackbar

    fun getMovies(){
        viewModelScope.launch {
            //для показа что есть сплеш скрин и шиммер, в "релизе" удалить
            delay(700)

            when(val result= moviesUseCases.getMoviesUseCase.start(pageOffset)){
                is GetMoviesResult.Success -> {
                    removeShimmer()
                    result.response.results?.forEach {
                        movieListShimmer.add(MovieWithShimmer.Movies(it))
                    }


                    pageOffset += result.response.num_results
                    hasMoreMovies= result.response.has_more
                    _loadingIsNotBusy.value = true

                    _getMoviesResponse.value= MoviesStateVM.GotMoviesShimmer((movieListShimmer))
                }
                else -> {
                    if(movieListShimmer.isEmpty()) _getMoviesResponse.value= MoviesStateVM.Error(result, result.error)
                    else{
                        _loadingIsNotBusy.value = true
                        removeShimmer()
                        _showSnackbar.value = SnackbarTypes.ConnectionError
                        _getMoviesResponse.value = MoviesStateVM.GotMoviesShimmer(movieListShimmer)
                    }

                }
            }
        }
    }

    private fun removeShimmer() {
        movieListShimmer = movieListShimmer.filter { it.rowType == RowType.Item }.toMutableList()
    }

    fun setLoading(){
        _getMoviesResponse.value = MoviesStateVM.Loading
    }

    fun loadMore(){
        if(hasMoreMovies){
            _loadingIsNotBusy.value = false
            for(i in 0..10){
                movieListShimmer.add(MovieWithShimmer.Shimmer)
            }
            _getMoviesResponse.value = MoviesStateVM.GotMoviesShimmer(movieListShimmer)
            getMovies()
        }
    }

    fun checkListReadyState(){
        when(_getMoviesResponse.value){
            is MoviesStateVM.GotMovies, is MoviesStateVM.Loading -> {}
            else ->{
                setLoading()
                getMovies()
            }
        }
    }
}
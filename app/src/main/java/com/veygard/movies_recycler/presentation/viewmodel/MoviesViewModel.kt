package com.veygard.movies_recycler.presentation.viewmodel

import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.domain.repository.GetMoviesResult
import com.veygard.movies_recycler.domain.use_cases.MoviesUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(
    private val moviesUseCases: MoviesUseCases
) : ViewModel(){

    private var hasMoreMovies = true
    private var pageOffset = 0
    private var movieList = mutableListOf<Movie>()

    private val _getMoviesResponse: MutableLiveData<MoviesStateVM?> = MutableLiveData(null)
    val getMoviesResponse: LiveData<MoviesStateVM?> = _getMoviesResponse

    fun getMovies(){
        viewModelScope.launch {
            //для показа что есть сплеш скрин
            delay(500)

            when(val result= moviesUseCases.getMoviesUseCase.start(pageOffset)){
                is GetMoviesResult.Success -> {
                    result.response.results?.forEach {
                        if (!movieList.contains(it)) movieList.add(it)
                    }
                    pageOffset += result.response.num_results

                    _getMoviesResponse.value= MoviesStateVM.GotMovies((movieList))
                    hasMoreMovies= result.response.has_more
                }
                else -> {
                    if(movieList.isEmpty()) _getMoviesResponse.value= MoviesStateVM.Error(result, result.error)
                }
            }
        }
    }

    fun setLoading(){
        _getMoviesResponse.value = MoviesStateVM.Loading
    }

    fun loadMore(){
        if(hasMoreMovies){
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
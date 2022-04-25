package com.veygard.movies_recycler.presentation.viewmodel

import com.veygard.movies_recycler.data.remote.model.GetMoviesResponse
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


    private val _getMoviesResponse: MutableLiveData<MoviesStateVM?> = MutableLiveData(null)
    val getMoviesResponse: LiveData<MoviesStateVM?> = _getMoviesResponse

    fun getMovies(){
        viewModelScope.launch {
            //для показа что есть сплеш скрин
            delay(500)

            when(val result= moviesUseCases.getMoviesUseCase.start()){
                is GetMoviesResult.Success -> {
                    _getMoviesResponse.value= MoviesStateVM.GotMovies(result.response)
                }
                else -> _getMoviesResponse.value= MoviesStateVM.Error(result, result.error)
            }
        }
    }

    fun setLoading(){
        _getMoviesResponse.value = MoviesStateVM.Loading
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
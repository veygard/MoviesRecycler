package com.veygard.movies_recycler.util

sealed class SnackbarTypes{
    object ConnectionError: SnackbarTypes()
    object ServerError: SnackbarTypes()
    data class RequestTimeOut(val pauseTime: Long): SnackbarTypes()
    object Loading: SnackbarTypes()
    object NoMoviesLeft: SnackbarTypes()
}

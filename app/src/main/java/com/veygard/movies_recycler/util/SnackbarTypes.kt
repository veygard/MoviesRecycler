package com.veygard.movies_recycler.util

sealed class SnackbarTypes{
    object ConnectionError: SnackbarTypes()
    object ServerError: SnackbarTypes()
    object Loading: SnackbarTypes()
}

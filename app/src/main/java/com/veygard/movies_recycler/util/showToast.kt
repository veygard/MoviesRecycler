package com.veygard.movies_recycler.util

import android.content.Context
import android.graphics.Color
import android.util.Log
import com.veygard.movies_recycler.R

fun showToast(snackbarState: SnackbarTypes?, context: Context?) {
    context?.let {
        var text = ""
        var background = Color.LTGRAY
        var textColor = Color.BLACK

        when (snackbarState) {
            SnackbarTypes.ConnectionError, SnackbarTypes.ServerError -> {
                text = context.getString(R.string.snackbar_error_message)
                background = context.getColor(R.color.error)
                textColor = context.getColor(R.color.white)
            }
            is SnackbarTypes.RequestTimeOut -> {
                text= context.getString(R.string.snackbar_time_message, snackbarState.pauseTime)
                background = context.getColor(R.color.blue)
                textColor = context.getColor(R.color.white)
            }
        }
        Log.d("toast", "show toast")
        CustomToast(context, text, background, textColor = textColor).show()
    }
}
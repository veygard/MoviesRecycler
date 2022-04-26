package com.veygard.movies_recycler.util

import android.content.Context
import android.net.ConnectivityManager

fun isInternetConnected(getApplicationContext: Context?): Boolean {
    var status = false
    getApplicationContext?.let {
        val cm =
            getApplicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (cm.activeNetwork != null && cm.getNetworkCapabilities(cm.activeNetwork) != null) {
            // connected to the internet
            status = true
        }
    }
    return status
}
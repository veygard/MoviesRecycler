package com.veygard.movies_recycler.data.remote.supports

import android.util.Log

/*У Аpi стоит ограничение 10 запросов в минуту. Можно было сделать таймаут для всех запросов - 6 секунд.
* Но, решил сделать так, что для первых 5 запросов - таймаут короткий, потом 5 запросов - длинный. И по кругу*/

class MovieApiTimeoutControl {
    private var movieRequestLimitCounter= 0
    private val shortRequestTimeOut= 1000L
    private val midRequestTimeOut= 5000L
    private val longRequestTimeOut= 10000L

    var requestTimeOut = shortRequestTimeOut

    private fun riseCounter(){
        movieRequestLimitCounter +=1
    }

    fun checkCounter(){
        riseCounter()
        Log.d("pagination_test","timeout counter: $movieRequestLimitCounter, $requestTimeOut")
        when(movieRequestLimitCounter){
            in 0..4 -> {
                requestTimeOut = shortRequestTimeOut
            }
            in 5..7-> {
                requestTimeOut = midRequestTimeOut
            }
            in 7..10 -> {
                requestTimeOut = longRequestTimeOut
            }

            else -> {
                movieRequestLimitCounter = 0
                requestTimeOut = shortRequestTimeOut
            }
        }
    }
}
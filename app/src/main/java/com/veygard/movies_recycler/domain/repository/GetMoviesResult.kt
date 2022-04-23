package com.veygard.movies_recycler.domain.repository

import com.veygard.movies_recycler.data.remote.model.GetMoviesResponse
import com.veygard.movies_recycler.data.remote.model.MovieDTO


sealed class GetMoviesResult(open val error: String? = null) {
    data class Success(val response: GetMoviesResponse) : GetMoviesResult()
    data class ConnectionError(override val error: String? = null) : GetMoviesResult(error = error)
    data class ServerError(override val error: String? = null) : GetMoviesResult(error = error)
    data class EnqueueError(override val error: String? = null) : GetMoviesResult(error = error)
}

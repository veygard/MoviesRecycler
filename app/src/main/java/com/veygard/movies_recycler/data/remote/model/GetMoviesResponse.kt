package com.veygard.movies_recycler.data.remote.model

data class GetMoviesResponse(
    val copyright: String? = null,
    val has_more: Boolean? = null,
    val num_results: Int? = null,
    val results: List<Movie>? = null,
    val status: String? = null
)
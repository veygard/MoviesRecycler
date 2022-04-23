package com.veygard.movies_recycler.data.remote.model

data class GetMoviesResponse(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: List<MovieDTO>,
    val status: String
)
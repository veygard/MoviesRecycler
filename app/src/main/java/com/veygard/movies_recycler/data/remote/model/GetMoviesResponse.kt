package com.veygard.movies_recycler.data.remote.model

data class GetMoviesResponse(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val movieDTOS: List<MovieDTO>,
    val status: String
)
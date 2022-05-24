package com.veygard.movies_recycler.presentation.adapters

import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding

class MovieShimmerViewHolder (
    private val binding: MovieItemShimmerBinding,
): MovieTypesViewHolder(binding){

    fun bind(){
        binding.movieShimmerLayout.startShimmer()
    }
}

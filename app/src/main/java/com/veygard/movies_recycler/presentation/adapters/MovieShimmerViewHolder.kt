package com.veygard.movies_recycler.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding
import com.veygard.movies_recycler.domain.model.MovieWithShimmer

class MovieShimmerViewHolder(
    private val binding: MovieItemShimmerBinding,
) : MovieTypesViewHolder(binding) {

    companion object{
        fun from(parent: ViewGroup): MovieShimmerViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                MovieItemShimmerBinding.inflate(layoutInflater, parent, false)
            return MovieShimmerViewHolder(binding)
        }
    }

    override fun bind(item: MovieWithShimmer) {
        binding.movieShimmerLayout.startShimmer()
    }

}

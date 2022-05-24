package com.veygard.movies_recycler.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding
import com.veygard.movies_recycler.domain.model.MovieWithShimmer

class MovieViewHolder(
    private val binding: MovieItemBinding,
): MovieTypesViewHolder(binding) {
    companion object{
         var movieItemHeight = 0

        fun from(parent: ViewGroup): MovieViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding =
                MovieItemBinding.inflate(layoutInflater, parent, false)
            return MovieViewHolder(binding)
        }
    }
    init {
        binding.movieCardContainer.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
        movieItemHeight= binding.movieCardContainer.measuredHeight
    }

    override fun bind(item: MovieWithShimmer){
        val movie = (item as MovieWithShimmer.Movies).movie

        binding.movieItemText.text = movie.summary_short
        binding.movieItemTitle.text= movie.display_title

        binding.movieItemPoster.load(movie.multimedia?.src ?: "") {
            crossfade(true)
            placeholder(R.drawable.ic_movie_placeholder)
            transformations(CircleCropTransformation())
            error(R.drawable.ic_movie_error)
        }
    }

}

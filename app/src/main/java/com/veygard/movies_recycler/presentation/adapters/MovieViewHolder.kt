package com.veygard.movies_recycler.presentation.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding

class MovieViewHolder(
    private val binding: MovieItemBinding,
): RecyclerView.ViewHolder(binding.root) {
    companion object{
         var movieItemHeight = 0
    }
    init {
        binding.movieCardContainer.measure(View.MeasureSpec.UNSPECIFIED,View.MeasureSpec.UNSPECIFIED)
        movieItemHeight= binding.movieCardContainer.measuredHeight
    }

    fun bind(movie: Movie){
        binding.movieItemText.text = movie.summary_short
        binding.movieItemTitle.text= movie.display_title

        binding.movieItemPoster.load(movie.multimedia.src ?: "") {
            crossfade(true)
            placeholder(R.drawable.ic_movie_placeholder)
            transformations(CircleCropTransformation())
            error(R.drawable.ic_movie_error)
        }
    }
}

package com.veygard.movies_recycler.presentation.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding

class MovieShimmerViewHolder(
    private val binding: MovieItemShimmerBinding,
): RecyclerView.ViewHolder(binding.root) {

    fun bind(){
        binding.movieShimmerLayout.startShimmer()
    }
}

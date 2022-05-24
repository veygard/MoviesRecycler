package com.veygard.movies_recycler.presentation.supports

import androidx.recyclerview.widget.DiffUtil
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.domain.model.MovieWithShimmer

class MovieDiffUtilCallback(
) : DiffUtil.ItemCallback<MovieWithShimmer>() {
    override fun areItemsTheSame(oldItem: MovieWithShimmer, newItem: MovieWithShimmer): Boolean {
        var isSame = false
        when {
            oldItem is MovieWithShimmer.Movies && newItem is MovieWithShimmer.Movies -> {
                isSame = oldItem.movie.display_title == newItem.movie.display_title
            }
            oldItem is MovieWithShimmer.Shimmer && newItem is MovieWithShimmer.Shimmer -> isSame =
                true
        }
        return isSame
    }

    override fun areContentsTheSame(oldItem: MovieWithShimmer, newItem: MovieWithShimmer): Boolean =
        oldItem == newItem
}
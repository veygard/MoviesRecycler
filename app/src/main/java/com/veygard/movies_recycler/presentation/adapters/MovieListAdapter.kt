package com.veygard.movies_recycler.presentation.adapters

import android.view.ViewGroup
import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.model.RowType
import androidx.recyclerview.widget.ListAdapter
import com.veygard.movies_recycler.presentation.supports.MovieDiffUtilCallback

class MovieListAdapter:
    ListAdapter<MovieWithShimmer, MovieTypesViewHolder>(MovieDiffUtilCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTypesViewHolder {
        return when (RowType.values()[viewType]) {
            RowType.Movie -> {
                MovieViewHolder.from(parent)
                }
            RowType.Shimmer -> {
                MovieShimmerViewHolder.from(parent)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieTypesViewHolder, position: Int) = holder.bind(getItem(position))

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is MovieWithShimmer.Movies -> 0
            MovieWithShimmer.Shimmer -> 1
        }
    }

}
package com.veygard.movies_recycler.presentation.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding
import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.model.RowType
import com.veygard.movies_recycler.domain.model.toMovieShimmerList
import androidx.recyclerview.widget.ListAdapter
import com.veygard.movies_recycler.presentation.supports.MovieDiffUtilCallback

class MovieListAdapter(private val movieList: List<Movie>) :
    ListAdapter<MovieWithShimmer, MovieTypesViewHolder>(MovieDiffUtilCallback()) {
    var shimmerList = mutableListOf<MovieWithShimmer>()

    init {
        Log.d("pagination_test", "adapter init")
        shimmerList.addAll(movieList.toMovieShimmerList())
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieTypesViewHolder {
        return when (RowType.values()[viewType]) {
            RowType.Item -> {
                val binding =
                    MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }
            RowType.Shimmer -> {
                val binding =
                    MovieItemShimmerBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                MovieShimmerViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: MovieTypesViewHolder, position: Int) =
        when (val groups = shimmerList[position]) {
            is MovieWithShimmer.Movies -> (holder as MovieViewHolder).bind(groups.movie)
            is MovieWithShimmer.Shimmer -> (holder as MovieShimmerViewHolder).bind()
        }

    override fun getItemViewType(position: Int): Int =
        shimmerList[position].rowType.ordinal


    fun addNewItems(items: List<Movie>) {
        removeShimmers()
        shimmerList.addAll(items.toMovieShimmerList())
    }



    fun removeShimmers() {
        shimmerList.removeAll { it is MovieWithShimmer.Shimmer }
    }


}
package com.veygard.movies_recycler.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding
import com.veygard.movies_recycler.domain.model.MovieWithShimmer
import com.veygard.movies_recycler.domain.model.RowType

class MovieListAdapter(private val movieList: List<MovieWithShimmer>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (RowType.values()[viewType]) {
            RowType.Item -> {
                val binding =
                    MovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieViewHolder(binding)
            }
            RowType.Shimmer -> {
                val binding =
                    MovieItemShimmerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                MovieShimmerViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) =
        when (val groups = movieList[position]) {
            is MovieWithShimmer.Movies -> (holder as MovieViewHolder).bind(groups.movie)
            is MovieWithShimmer.Shimmer -> (holder as MovieShimmerViewHolder).bind()
        }

    override fun getItemViewType(position: Int): Int =
        movieList[position].rowType.ordinal

    override fun getItemCount(): Int = movieList.size
}
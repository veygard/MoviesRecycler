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

class MovieListAdapter(private val movieList: List<Movie>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var list = mutableListOf<MovieWithShimmer>()

    init {
        Log.d("pagination_test","adapter init")
        list.addAll(movieList.toMovieShimmerList())
    }

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
        when (val groups = list[position]) {
            is MovieWithShimmer.Movies -> (holder as MovieViewHolder).bind(groups.movie)
            is MovieWithShimmer.Shimmer -> (holder as MovieShimmerViewHolder).bind()
        }

    override fun getItemViewType(position: Int): Int =
        list[position].rowType.ordinal

    override fun getItemCount(): Int =  list.size

    fun addNewItems(items: List<Movie>){
        removeShimmers()
        list.addAll(items.toMovieShimmerList())
        notifyDataSetChanged()
    }

    fun addShimmers(){
        for(i in 0..20){
            list.add(MovieWithShimmer.Shimmer)
        }
        notifyDataSetChanged()
    }

    private fun removeShimmers(){
//        val iterator= list.iterator()
//        while (iterator.hasNext()){
//            val item = iterator.next()
//            if (item.rowType == RowType.Shimmer) iterator.remove()
//        }
        list.removeAll { it is MovieWithShimmer.Shimmer}
        notifyDataSetChanged()
    }

}
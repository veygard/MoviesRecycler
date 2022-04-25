package com.veygard.movies_recycler.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.veygard.movies_recycler.R
import com.veygard.movies_recycler.data.remote.model.Movie
import com.veygard.movies_recycler.databinding.MovieItemBinding

class MovieListAdapter(private val movieList: List<Movie>): RecyclerView.Adapter<MovieListAdapter.MovieViewHolder>() {

    class MovieViewHolder(
        private val binding: MovieItemBinding,
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie){
            binding.movieItemText.text = movie.summary_short
            binding.movieItemTitle.text= movie.display_title

            binding.movieItemPoster.load(movie.multimedia.src) {
                crossfade(true)
                placeholder(R.drawable.ic_movie_placeholder)
                transformations(CircleCropTransformation())
                error(R.drawable.ic_movie_error)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = MovieItemBinding.inflate(layoutInflater, parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    private fun getItem(position: Int): Movie = movieList[position]

    override fun getItemCount(): Int = movieList.size
}
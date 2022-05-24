package com.veygard.movies_recycler.presentation.adapters

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.veygard.movies_recycler.databinding.MovieItemShimmerBinding
import com.veygard.movies_recycler.domain.model.MovieWithShimmer

abstract class MovieTypesViewHolder(binding: androidx.viewbinding.ViewBinding) : RecyclerView.ViewHolder(binding.root){
    abstract fun bind(item: MovieWithShimmer)
}
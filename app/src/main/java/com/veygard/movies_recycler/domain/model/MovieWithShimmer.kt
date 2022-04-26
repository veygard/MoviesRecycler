package com.veygard.movies_recycler.domain.model

import com.veygard.movies_recycler.data.remote.model.Movie

sealed class MovieWithShimmer(
    val rowType: RowType
){
    data class Movies(val movie: Movie) :
        MovieWithShimmer(RowType.Item)

     object Shimmer : MovieWithShimmer(RowType.Shimmer)
}

enum class RowType {
    Item,
    Shimmer
}


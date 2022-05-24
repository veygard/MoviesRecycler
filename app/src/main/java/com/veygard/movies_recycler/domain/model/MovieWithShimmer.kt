package com.veygard.movies_recycler.domain.model

import com.veygard.movies_recycler.data.remote.model.Movie

sealed class MovieWithShimmer(
    val rowType: RowType,
    open val movie: Movie? = null
){
    data class Movies(override val movie: Movie) :
        MovieWithShimmer(RowType.Movie, movie = movie)

     object Shimmer : MovieWithShimmer(RowType.Shimmer)
}

enum class RowType {
    Movie,
    Shimmer
}

fun Movie.toMovieShimmerType() : MovieWithShimmer.Movies = MovieWithShimmer.Movies(this)
fun List<Movie>.toMovieShimmerList() : List<MovieWithShimmer> = this.map { it.toMovieShimmerType() }


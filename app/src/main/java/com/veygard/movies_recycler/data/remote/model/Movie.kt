package com.veygard.movies_recycler.data.remote.model

data class Movie(
    val byline: String? = null,
    val critics_pick: Int? = null,
    val date_updated: String? = null,
    val display_title: String? = null,
    val headline: String? = null,
    val link: Link? = null,
    val mpaa_rating: String? = null,
    val multimedia: Multimedia? = null,
    val opening_date: String? = null,
    val publication_date: String? = null,
    val summary_short: String? = null
)
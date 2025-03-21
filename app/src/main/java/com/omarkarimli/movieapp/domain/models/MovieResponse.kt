package com.omarkarimli.movieapp.domain.models

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    @SerializedName("movies")
    val movies: List<Movie?>?,
    @SerializedName("totalPages")
    val totalPages: Int
)
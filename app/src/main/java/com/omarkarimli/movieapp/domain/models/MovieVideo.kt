package com.omarkarimli.movieapp.domain.models

data class MovieVideo(
    val id: String,
    val key: String,  // YouTube video key
    val site: String,
    val type: String
)

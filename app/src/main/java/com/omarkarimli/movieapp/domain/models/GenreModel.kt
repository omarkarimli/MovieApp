package com.omarkarimli.movieapp.domain.models

data class GenreModel(
    val id: Int,
    val name: String,

    val isSelected: Boolean = false
)

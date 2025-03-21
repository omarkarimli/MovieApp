package com.omarkarimli.movieapp.data.source.local

import com.omarkarimli.movieapp.R
import com.omarkarimli.movieapp.domain.models.GenreModel

val categoryList = mutableListOf(
    GenreModel(id = 1, image = R.drawable.sports, name = "Sports", desc = "Sports news and live sports coverage including scores"),
    GenreModel(id = 2, image = R.drawable.business, name = "Business", desc = "The latest breaking financial news in the world"),
    GenreModel(id = 3, image = R.drawable.health, name = "Health", desc = "View the latest health news and explore articles"),
    GenreModel(id = 4, image = R.drawable.science, name = "Science", desc = "Science and discoveries"),
    GenreModel(id = 5, image = R.drawable.entertainment, name = "Entertainment", desc = "Movies, music, and more"),
    GenreModel(id = 6, image = R.drawable.general, name = "General", desc = "General news and updates"),
    GenreModel(id = 7, image = R.drawable.tech, name = "Technology", desc = "The latest tech news about the world's best hardware")
)
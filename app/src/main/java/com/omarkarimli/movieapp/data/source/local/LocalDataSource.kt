package com.omarkarimli.movieapp.data.source.local

import com.omarkarimli.movieapp.domain.models.Movie
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getAllMoviesLocally(): Flow<List<Movie>>

    suspend fun deleteMovieLocally(movie: Movie)

    suspend fun deleteMovieByIdLocally(id: Int)

    suspend fun getMovieByIdLocally(id: Int): Movie?

    suspend fun addMovieLocally(movie: Movie)

    suspend fun updateMovieLocally(movie: Movie)

    suspend fun deleteAllMoviesLocally()
}
package com.omarkarimli.movieapp.domain.repository

import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.UserData
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    // Remote

    suspend fun getMovieById(id: Int): Movie

    suspend fun fetchAllMovies(page: Int): List<Movie>

    suspend fun fetchMoviesByGenre(genreId: Int, page: Int): List<Movie>

    suspend fun fetchAllGenres(): List<GenreModel>




    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): UserData?

    // Local
    suspend fun getAllMoviesLocally(): Flow<List<Movie>>

    suspend fun deleteMovieLocally(movie: Movie)

    suspend fun getMovieByIdLocally(id: Int): Movie?

    suspend fun addMovieLocally(movie: Movie)

    suspend fun updateMovieLocally(movie: Movie)

    suspend fun deleteAllMoviesLocally()
}
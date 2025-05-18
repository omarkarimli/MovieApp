package com.omarkarimli.movieapp.domain.repository

import com.omarkarimli.movieapp.domain.models.CreditResponse
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.MovieResponse
import com.omarkarimli.movieapp.domain.models.MovieVideo
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    // Remote
    suspend fun getMovieById(id: Int): Movie

    suspend fun fetchAllMovies(page: Int): MovieResponse

    suspend fun fetchMoviesByGenre(genreId: Int, page: Int): MovieResponse

    suspend fun fetchAllGenres(): List<GenreModel>

    suspend fun getMovieVideos(id: Int): List<MovieVideo>

    suspend fun searchMovies(query: String, page: Int): MovieResponse

    suspend fun getMovieCredits(movieId: Int): CreditResponse

    // Local
    suspend fun getAllMoviesLocally(): Flow<List<Movie>>

    suspend fun deleteMovieLocally(movie: Movie)

    suspend fun deleteMovieByIdLocally(id: Int)

    suspend fun getMovieByIdLocally(id: Int): Movie?

    suspend fun addMovieLocally(movie: Movie)

    suspend fun updateMovieLocally(movie: Movie)

    suspend fun deleteAllMoviesLocally()
}
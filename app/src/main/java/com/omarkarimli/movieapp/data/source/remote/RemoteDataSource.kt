package com.omarkarimli.movieapp.data.source.remote

import com.omarkarimli.movieapp.domain.models.CreditResponse
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.MovieResponse
import com.omarkarimli.movieapp.domain.models.MovieVideo

interface RemoteDataSource {

    suspend fun getMovieById(id: Int): Movie

    suspend fun fetchAllMovies(page: Int): MovieResponse

    suspend fun fetchMoviesByGenre(genreId: Int, page: Int): MovieResponse

    suspend fun fetchAllGenres(): List<GenreModel>

    suspend fun getMovieVideos(id: Int): List<MovieVideo>

    suspend fun searchMovies(query: String, page: Int): MovieResponse

    suspend fun getMovieCredits(movieId: Int): CreditResponse
}
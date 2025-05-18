package com.omarkarimli.movieapp.data.repository

import com.omarkarimli.movieapp.data.source.local.LocalDataSource
import com.omarkarimli.movieapp.data.source.remote.RemoteDataSource
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : MovieRepository {

    // Remote
    override suspend fun getMovieById(id: Int) = remoteDataSource.getMovieById(id)

    override suspend fun fetchAllMovies(page: Int) = remoteDataSource.fetchAllMovies(page)

    override suspend fun fetchMoviesByGenre(genreId: Int, page: Int) = remoteDataSource.fetchMoviesByGenre(genreId, page)

    override suspend fun fetchAllGenres() = remoteDataSource.fetchAllGenres()

    override suspend fun getMovieVideos(id: Int) = remoteDataSource.getMovieVideos(id)

    override suspend fun searchMovies(query: String, page: Int) = remoteDataSource.searchMovies(query, page)

    override suspend fun getMovieCredits(movieId: Int) = remoteDataSource.getMovieCredits(movieId)

    // Local
    override suspend fun getAllMoviesLocally() = localDataSource.getAllMoviesLocally()

    override suspend fun deleteMovieLocally(movie: Movie) = localDataSource.deleteMovieLocally(movie)

    override suspend fun deleteMovieByIdLocally(id: Int) = localDataSource.deleteMovieByIdLocally(id)

    override suspend fun getMovieByIdLocally(id: Int) = localDataSource.getMovieByIdLocally(id)

    override suspend fun addMovieLocally(movie: Movie) = localDataSource.addMovieLocally(movie)

    override suspend fun updateMovieLocally(movie: Movie) = localDataSource.updateMovieLocally(movie)

    override suspend fun deleteAllMoviesLocally() = localDataSource.deleteAllMoviesLocally()
}
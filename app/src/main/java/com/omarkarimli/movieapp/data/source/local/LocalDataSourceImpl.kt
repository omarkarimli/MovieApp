package com.omarkarimli.movieapp.data.source.local

import com.omarkarimli.movieapp.domain.models.Movie
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val movieDao: MovieDao
) : LocalDataSource {

    override suspend fun getAllMoviesLocally() = movieDao.getAll()

    override suspend fun deleteMovieByIdLocally(id: Int) = movieDao.deleteById(id)

    override suspend fun getMovieByIdLocally(id: Int) = movieDao.getMovieById(id)

    override suspend fun addMovieLocally(movie: Movie) = movieDao.addMovie(movie)

    override suspend fun updateMovieLocally(movie: Movie) = movieDao.updateMovie(movie)

    override suspend fun deleteAllMoviesLocally() = movieDao.deleteAll()
}
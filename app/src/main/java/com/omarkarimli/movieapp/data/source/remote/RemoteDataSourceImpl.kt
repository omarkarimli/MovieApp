package com.omarkarimli.movieapp.data.source.remote

import com.omarkarimli.movieapp.BuildConfig
import com.omarkarimli.movieapp.data.api.ApiService
import com.omarkarimli.movieapp.domain.models.CreditResponse
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.MovieResponse
import com.omarkarimli.movieapp.domain.models.MovieVideo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {
    private val apiKey = BuildConfig.API_KEY

    override suspend fun getMovieById(id: Int): Movie {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovieById(id, apiKey).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("getMovieById " + e.message.toString())
            }
        }
    }

    override suspend fun searchMovies(query: String, page: Int): MovieResponse {
        return try {
            val response = apiService.searchMovies(query, apiKey, page)
            response
        } catch (e: Exception) {
            throw Exception("Error in searchMovies: ${e.message}")
        }
    }

    override suspend fun fetchAllMovies(page: Int): MovieResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovies(apiKey, page).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("fetchAllMovies " + e.message.toString())
            }
        }
    }

    override suspend fun fetchMoviesByGenre(genreId: Int, page: Int): MovieResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMoviesByGenre(apiKey, genreId, page).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("fetchMoviesByGenre " + e.message.toString())
            }
        }
    }

    override suspend fun fetchAllGenres(): List<GenreModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getGenres(apiKey).awaitResponse()
                response.body()?.genres ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieVideos(id: Int): List<MovieVideo> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovieVideos(id, apiKey)
                response.results
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieCredits(movieId: Int): CreditResponse {
        return try {
            apiService.getMovieCredits(movieId, apiKey)
        } catch (e: Exception) {
            throw Exception("getMovieCredits failed: ${e.message}")
        }
    }
}
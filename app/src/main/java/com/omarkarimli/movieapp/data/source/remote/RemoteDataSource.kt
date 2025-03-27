package com.omarkarimli.movieapp.data.source.remote

import com.google.firebase.auth.AuthResult
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.MovieResponse
import com.omarkarimli.movieapp.domain.models.MovieVideo
import com.omarkarimli.movieapp.domain.models.UserData

interface RemoteDataSource {

    suspend fun getMovieById(id: Int): Movie

    suspend fun fetchAllMovies(page: Int): MovieResponse

    suspend fun fetchMoviesByGenre(genreId: Int, page: Int): MovieResponse

    suspend fun fetchAllGenres(): List<GenreModel>

    suspend fun getMovieVideos(id: Int): List<MovieVideo>

    suspend fun searchMovies(query: String, page: Int): MovieResponse



    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): UserData?

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: UserData)

    suspend fun updateUserInFirestore(userData: UserData)
}
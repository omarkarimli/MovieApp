package com.omarkarimli.movieapp.data.source.remote

import android.util.Log
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.omarkarimli.movieapp.data.api.ApiService
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.MovieResponse
import com.omarkarimli.movieapp.domain.models.MovieVideo
import com.omarkarimli.movieapp.domain.models.UserData
import com.omarkarimli.movieapp.utils.Constants
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.awaitResponse
import javax.inject.Inject

class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService,
    private val provideAuth: FirebaseAuth,
    private val provideFirestore: FirebaseFirestore,
) : RemoteDataSource {

    override suspend fun getMovieById(id: Int): Movie {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovieById(id, Constants.API_KEY).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("getMovieById " + e.message.toString())
            }
        }
    }

    override suspend fun searchMovies(query: String, page: Int): MovieResponse {
        return try {
            val response = apiService.searchMovies(query, Constants.API_KEY, page)
            response
        } catch (e: Exception) {
            throw Exception("Error in searchMovies: ${e.message}")
        }
    }

    override suspend fun fetchAllMovies(page: Int): MovieResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovies(Constants.API_KEY, page).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("fetchAllMovies " + e.message.toString())
            }
        }
    }

    override suspend fun fetchMoviesByGenre(genreId: Int, page: Int): MovieResponse {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMoviesByGenre(Constants.API_KEY, genreId, page).awaitResponse()
                response.body() ?: throw Exception("Response body is null")
            } catch (e: Exception) {
                throw Exception("fetchMoviesByGenre " + e.message.toString())
            }
        }
    }

    override suspend fun fetchAllGenres(): List<GenreModel> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getGenres(Constants.API_KEY).awaitResponse()
                response.body()?.genres ?: emptyList()
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun getMovieVideos(id: Int): List<MovieVideo> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apiService.getMovieVideos(id, Constants.API_KEY)
                response.results
            } catch (e: Exception) {
                emptyList()
            }
        }
    }

    override suspend fun changePassword(email: String, currentPassword: String, newPassword: String) {
        val user = provideAuth.currentUser ?: FirebaseAuth.getInstance().currentUser
        val credential = EmailAuthProvider.getCredential(email, currentPassword)

        try {
            if (user != null) {
                user.reauthenticate(credential).await() // Re-authenticate user
                user.updatePassword(newPassword).await() // Update password
            } else {
                throw Exception("User is not authenticated")
            }
        } catch (e: Exception) {
            throw e // Handle exception appropriately
        }
    }

    override suspend fun fetchUserData(): UserData? {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val snapshot = provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .get()
            .await()

        return snapshot.toObject(UserData::class.java)
    }

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult =
        provideAuth
            .signInWithEmailAndPassword(email, password)
            .await()

    override suspend fun registerNewUser(email: String, password: String): AuthResult =
        provideAuth
            .createUserWithEmailAndPassword(email, password)
            .await()

    override suspend fun addUserToFirestore(userData: UserData) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val userMap = mapOf(
            Constants.NAME to userData.name,
            Constants.SURNAME to userData.surname,
            Constants.BIO to userData.bio,
            Constants.WEBSITE to userData.website
        )
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userMap)
            .await()
    }

    override suspend fun updateUserInFirestore(userData: UserData) {
        val uid = provideAuth.currentUser?.uid ?: "error"
        val userMap = mapOf(
            Constants.NAME to userData.name,
            Constants.SURNAME to userData.surname,
            Constants.BIO to userData.bio,
            Constants.WEBSITE to userData.website
        )
        provideFirestore
            .collection(Constants.USERS)
            .document(uid)
            .set(userMap, SetOptions.merge())
            .await()
    }
}
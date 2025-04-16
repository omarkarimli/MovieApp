package com.omarkarimli.movieapp.domain.repository

import com.google.firebase.auth.AuthResult
import com.omarkarimli.movieapp.domain.models.UserData

interface AuthRepository {

    suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String): AuthResult

    suspend fun registerNewUser(email: String, password: String): AuthResult

    suspend fun addUserToFirestore(userData: UserData)

    suspend fun updateUserInFirestore(userData: UserData)

    suspend fun changePassword(email: String, currentPassword: String, newPassword: String)

    suspend fun fetchUserData(): UserData?
}
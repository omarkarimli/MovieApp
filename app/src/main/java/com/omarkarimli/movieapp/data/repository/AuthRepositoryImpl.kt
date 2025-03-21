package com.omarkarimli.movieapp.data.repository

import com.omarkarimli.movieapp.data.source.remote.RemoteDataSource
import com.omarkarimli.movieapp.domain.models.UserData
import com.omarkarimli.movieapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
): AuthRepository {

    override suspend fun loginUserAccount(isChecked: Boolean, email: String, password: String) = remoteDataSource.loginUserAccount(isChecked, email, password)

    override suspend fun registerNewUser(email: String, password: String) = remoteDataSource.registerNewUser(email, password)

    override suspend fun addUserToFirestore(userData: UserData) = remoteDataSource.addUserToFirestore(userData)

    override suspend fun updateUserInFirestore(userData: UserData) = remoteDataSource.updateUserInFirestore(userData)
}
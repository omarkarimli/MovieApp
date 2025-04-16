package com.omarkarimli.movieapp.di

import com.omarkarimli.movieapp.data.repository.AuthRepositoryImpl
import com.omarkarimli.movieapp.data.repository.MovieRepositoryImpl
import com.omarkarimli.movieapp.domain.repository.AuthRepository
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRepositoryModule(
        repositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepositoryModule(
        repositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
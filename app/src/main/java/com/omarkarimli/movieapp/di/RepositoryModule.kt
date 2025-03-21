package com.omarkarimli.movieapp.di

import com.omarkarimli.movieapp.data.repository.NewsRepositoryImpl
import com.omarkarimli.movieapp.domain.repository.NewsRepository
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
        repositoryImpl: NewsRepositoryImpl
    ): NewsRepository
}
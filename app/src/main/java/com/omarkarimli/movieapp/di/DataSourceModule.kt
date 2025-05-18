package com.omarkarimli.movieapp.di

import com.omarkarimli.movieapp.data.source.local.LocalDataSource
import com.omarkarimli.movieapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.movieapp.data.source.remote.RemoteDataSource
import com.omarkarimli.movieapp.data.source.remote.RemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindRemoteDataSource(
        remoteDataSourceImpl: RemoteDataSourceImpl
    ): RemoteDataSource

    @Binds
    @Singleton
    abstract fun bindLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource
}
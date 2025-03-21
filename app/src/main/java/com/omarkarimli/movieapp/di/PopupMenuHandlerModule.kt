package com.omarkarimli.movieapp.di

import com.omarkarimli.movieapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object PopupMenuHandlerModule {

    @Singleton
    @Provides
    fun provideMorePopupMenuHandler(localDataSource: LocalDataSourceImpl) = MorePopupMenuHandler(localDataSource)
}
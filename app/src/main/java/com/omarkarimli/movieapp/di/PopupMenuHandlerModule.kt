package com.omarkarimli.movieapp.di

import com.omarkarimli.movieapp.domain.repository.MovieRepository
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
    fun provideMorePopupMenuHandler(repo: MovieRepository) = MorePopupMenuHandler(repo)
}
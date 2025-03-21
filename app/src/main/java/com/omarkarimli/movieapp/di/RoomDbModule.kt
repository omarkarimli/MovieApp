package com.omarkarimli.movieapp.di

import android.content.Context
import androidx.room.Room
import com.omarkarimli.movieapp.data.source.local.MovieDao
import com.omarkarimli.movieapp.data.source.local.MovieDatabase
import com.omarkarimli.movieapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomDbModule {

    @Provides
    @Singleton
    fun provideRoom(@ApplicationContext context: Context): MovieDatabase {
        return Room.databaseBuilder(context, MovieDatabase::class.java, Constants.MOVIE_DB).build()
    }

    @Provides
    @Singleton
    fun provideArticleDao(movieDatabase: MovieDatabase): MovieDao {
        return movieDatabase.movieDao()
    }
}
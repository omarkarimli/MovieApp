package com.omarkarimli.movieapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omarkarimli.movieapp.domain.models.Movie

@Database(
    entities = [Movie::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
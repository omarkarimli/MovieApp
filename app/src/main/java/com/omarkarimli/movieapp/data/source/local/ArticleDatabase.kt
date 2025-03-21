package com.omarkarimli.movieapp.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.omarkarimli.movieapp.domain.models.Article

@Database(
    entities = [Article::class],
    version = 1,
    exportSchema = false
)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}
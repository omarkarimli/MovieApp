package com.omarkarimli.movieapp.data.source.local

import com.omarkarimli.movieapp.domain.models.Article
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun getAllArticlesLocally(): Flow<List<Article>>

    suspend fun deleteArticleLocally(article: Article)

    suspend fun getArticleByUrlLocally(url: String): Article?

    suspend fun addArticleLocally(article: Article)

    suspend fun updateArticleLocally(article: Article)

    suspend fun deleteAllArticlesLocally()
}
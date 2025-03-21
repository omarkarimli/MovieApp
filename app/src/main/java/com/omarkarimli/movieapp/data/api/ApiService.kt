package com.omarkarimli.movieapp.data.api

import com.omarkarimli.movieapp.domain.models.NewsResponse
import com.omarkarimli.movieapp.domain.models.SourceResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface ApiService {
    @GET("everything")
    fun getNews(
        @Query("q") query: String,
        @Query("apiKey") apiKey: String
    ): Call<NewsResponse>

    @GET("top-headlines")
    suspend fun getNewsByCategory(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String
    ): NewsResponse

    @GET("top-headlines/sources")
    fun getSources(
        @Query("apiKey") apiKey: String
    ): Call<SourceResponse>
}

package com.omarkarimli.movieapp.presentation.ui.movie

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val movie = MutableLiveData<Movie>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchArticle(url: String, query: String) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.getMovieById(url, query)
                movie.postValue(response)
            } catch (e: Exception) {
                Log.e("ArticleViewModel", "Error: ${e.message}")
                error.postValue("Failed to load article")
            } finally {
                loading.value = false
            }
        }
    }
}

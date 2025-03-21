package com.omarkarimli.movieapp.presentation.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val movies = MutableLiveData<List<Movie>>()

    var currentPage = 1
    var totalPages = MutableLiveData<Int>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    init {
        fetchMovies(currentPage)
    }

    fun fetchMovies(page: Int) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllMovies(page)
                movies.postValue(response)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
                error.postValue("Failed to load movies")
            } finally {
                loading.value = false
            }
        }
    }

    fun nextPage() {
        if (currentPage < (totalPages.value ?: 1)) {
            fetchMovies(currentPage + 1)
        }
    }

    fun prevPage() {
        if (currentPage > 1) {
            fetchMovies(currentPage - 1)
        }
    }
}

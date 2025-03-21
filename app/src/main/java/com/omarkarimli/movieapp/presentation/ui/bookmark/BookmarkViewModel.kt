package com.omarkarimli.movieapp.presentation.ui.bookmark

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    private val _filteredMovies = MutableStateFlow<List<Movie>>(emptyList())
    val filteredMovies: StateFlow<List<Movie>> = _filteredMovies

    private val _movies = MutableStateFlow<List<Movie>>(emptyList())
    val movies: StateFlow<List<Movie>> = _movies

    private val _empty = MutableStateFlow(true)
    val empty: StateFlow<Boolean> = _empty

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun fetchMovies() {
        viewModelScope.launch {
            _loading.value = true
            try {
                repo.getAllMoviesLocally().collectLatest { moviesList ->
                    Log.d("BookmarkViewModel", "Received movies: $moviesList")
                    _movies.value = moviesList
                    _filteredMovies.value = moviesList
                    updateEmptyState()
                    _loading.value = false
                }
            } catch (e: Exception) {
                Log.e("BookmarkViewModel", "Error: ${e.message}")
                _error.value = "Failed to load movies"
                _loading.value = false
            }
        }
    }

    fun updateFilteredMovies(query: String) {
        val filteredList = if (query.isNotEmpty()) {
            _movies.value.filter { it.title?.contains(query, ignoreCase = true) == true }
        } else {
            _movies.value
        }

        _filteredMovies.value = filteredList
        updateEmptyState()
    }

    private fun updateEmptyState() {
        _empty.value = _filteredMovies.value.isEmpty()
    }

    fun clearError() {
        _error.value = null
    }
}

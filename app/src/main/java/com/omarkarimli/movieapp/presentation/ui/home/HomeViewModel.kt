package com.omarkarimli.movieapp.presentation.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import com.omarkarimli.movieapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val genres = MutableStateFlow<List<GenreModel>>(emptyList())
    val movies = MutableStateFlow<List<Movie>?>(emptyList())

    var currentPage = MutableStateFlow(1)
    var totalPages = MutableStateFlow(1)

    val selectedGenreId = MutableStateFlow(-1)

    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)

    fun fetchGenres() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllGenres()
                val modifiedList = listOf(
                    GenreModel(id = -1, name = Constants.ALL, isSelected = true)
                ) + response

                genres.value = modifiedList
                Log.d("HomeViewModel", "Genres loaded: ${genres.value}")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading genres: ${e.message}")
                error.value = e.localizedMessage ?: "Failed to load genres"
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchMovies(page: Int = 1) {
        selectedGenreId.value = -1
        currentPage.value = page
        loadMovies(page = page)
    }

    fun fetchMoviesByGenre(genreId: Int, page: Int = 1) {
        selectedGenreId.value = genreId
        currentPage.value = page
        loadMovies(genreId = genreId, page = page)
    }

    private fun loadMovies(genreId: Int = -1, page: Int) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = if (genreId == -1) {
                    repo.fetchAllMovies(page)
                } else {
                    repo.fetchMoviesByGenre(genreId, page)
                }

                movies.value = response.movies
                totalPages.value = response.totalPages
                Log.d("HomeViewModel", "Movies loaded - Total pages: ${totalPages.value}")
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error loading movies: ${e.message}")
                error.value = e.localizedMessage ?: "Failed to load movies"
            } finally {
                loading.value = false
            }
        }
    }

    fun changePage(isNext: Boolean) {
        val newPage = if (isNext) currentPage.value + 1 else currentPage.value - 1
        if (newPage in 1..totalPages.value) {
            currentPage.value = newPage
            loadMovies(genreId = selectedGenreId.value, page = newPage)
        } else {
            Log.w("HomeViewModel", "Page out of bounds: $newPage")
        }
    }
}

package com.omarkarimli.movieapp.presentation.ui.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val genres = MutableLiveData<List<GenreModel>>()
    val movies = MutableLiveData<List<Movie>?>()

    var currentPage = MutableLiveData(1)
    var totalPages = MutableLiveData<Int>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchMovies(page: Int) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllMovies(page)
                movies.value = response.movies
                totalPages.value = response.totalPages

                Log.d("555", "${totalPages.value}")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
                error.postValue("Failed to load movies")
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchGenres() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllGenres()

                val modifiedList = listOf(GenreModel(id = -1, name = "All", isSelected = true)) + response
                genres.value = modifiedList

                Log.d("555", "${genres.value}")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
                error.postValue("Failed to load genres")
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchMoviesByGenre(genreId: Int, page: Int) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchMoviesByGenre(genreId, page)
                movies.value = response.movies
                totalPages.value = response.totalPages

                Log.d("555", "${totalPages.value}")

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error: ${e.message}")
                error.postValue("Failed to load movies")
            } finally {
                loading.value = false
            }
        }
    }

    fun changePage(isNext: Boolean) {
        val newPage = if (isNext) {
            (currentPage.value ?: 1) + 1
        } else {
            (currentPage.value ?: 1) - 1
        }

        if (newPage > 0) { // Ensure page doesn't go below 1
            currentPage.value = newPage
            fetchMovies(newPage)
        } else {
            Log.e("HomeViewModel", "Cannot go below page 1")
        }
    }
}

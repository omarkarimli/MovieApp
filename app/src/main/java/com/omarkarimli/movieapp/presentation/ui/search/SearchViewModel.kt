package com.omarkarimli.movieapp.presentation.ui.search

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.MutableLiveData
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import com.omarkarimli.movieapp.data.source.local.categoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val filteredCategories = MutableLiveData<List<GenreModel>>()
    val filteredAuthors = MutableLiveData<List<SourceX>>()

    private val categories = MutableLiveData<List<GenreModel>>()
    val articles = MutableLiveData<List<Movie>>()
    val authors = MutableLiveData<List<SourceX>>()

    val empty = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchArticles(query: String) {
        viewModelScope.launch {
            empty.value = true
            loading.value = true
            try {
                val response = repo.fetchAllMovies(query)
                articles.value = response

                empty.value = response.isEmpty()
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error: ${e.message}")
                error.postValue("Failed to load articles")
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchAuthors() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchAllSources()
                authors.value = response
                filteredAuthors.value = response
            } catch (e: Exception) {
                Log.e("SearchViewModel", "Error: ${e.message}")
                error.value = "Failed to load sources"
            } finally {
                loading.value = false
            }
        }
    }

    fun fetchCategories() {
        categories.value = categoryList
        filteredCategories.value = categories.value
    }
}

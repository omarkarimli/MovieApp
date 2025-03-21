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

    val videoKey = MutableLiveData<String?>()
    val movie = MutableLiveData<Movie>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun getMovieById(id: Int) {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.getMovieById(id)
                movie.value = response

                fetchMovieVideos(id)

                Log.e("555", "Movie Id: ${movie.value?.id}")
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error: ${e.message}")
                error.postValue("Failed to load movie")
            } finally {
                loading.value = false
            }
        }
    }


    private fun fetchMovieVideos(id: Int) {
        viewModelScope.launch {
            try {
                val videos = repo.getMovieVideos(id)

                Log.e("MOVIE_VIDEO_API_RESPONSE", "Videos: $videos")

                val trailer = videos.firstOrNull {
                    it.site == "YouTube" &&
                            (it.type == "Trailer" || it.type == "Teaser")
                }

                Log.d("API_RESPONSE", "Trailer Key: ${trailer?.key}")

                videoKey.postValue(trailer?.key)  // Set video key
            } catch (e: Exception) {
                Log.e("MovieViewModel", "Error fetching videos: ${e.message}")
                videoKey.postValue(null)  // Prevent crashes
            }
        }
    }
}

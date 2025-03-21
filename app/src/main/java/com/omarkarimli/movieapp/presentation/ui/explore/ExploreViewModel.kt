package com.omarkarimli.movieapp.presentation.ui.explore

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.models.GenreModel
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import com.omarkarimli.movieapp.data.source.local.categoryList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()
}
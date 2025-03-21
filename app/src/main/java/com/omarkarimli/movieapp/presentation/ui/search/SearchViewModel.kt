package com.omarkarimli.movieapp.presentation.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val empty = MutableLiveData(false)
    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

}

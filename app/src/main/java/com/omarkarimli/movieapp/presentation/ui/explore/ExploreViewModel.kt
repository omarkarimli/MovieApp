package com.omarkarimli.movieapp.presentation.ui.explore

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()
}
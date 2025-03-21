package com.omarkarimli.movieapp.presentation.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omarkarimli.movieapp.domain.models.UserData
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repo: MovieRepository
) : ViewModel() {

    val userData = MutableLiveData<UserData?>()

    val loading = MutableLiveData(false)
    val error = MutableLiveData<String>()

    fun fetchUserData() {
        viewModelScope.launch {
            loading.value = true
            try {
                val response = repo.fetchUserData()
                if (response != null) {
                    userData.postValue(response)
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error: ${e.message}")
                error.postValue("Failed to load user data")
            } finally {
                loading.value = false
            }
        }
    }
}
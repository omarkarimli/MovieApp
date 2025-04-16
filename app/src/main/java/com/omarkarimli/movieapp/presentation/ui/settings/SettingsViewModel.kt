package com.omarkarimli.movieapp.presentation.ui.settings

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import com.omarkarimli.movieapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.core.content.edit

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val provideAuth: FirebaseAuth
): ViewModel() {

    val isDarkMode: MutableLiveData<Boolean> = MutableLiveData(false)
    val isNavigating: MutableLiveData<Boolean> = MutableLiveData(false)
    val loading: MutableLiveData<Boolean> = MutableLiveData()
    val error: MutableLiveData<String> = MutableLiveData()

    fun initializeDarkModeState() {
        isDarkMode.value = sharedPreferences.getBoolean(Constants.DARK_MODE, false)
    }

    fun changeDarkModeState(isChecked: Boolean) {
        isDarkMode.value = isChecked

        sharedPreferences
            .edit {
                putBoolean(Constants.DARK_MODE, isChecked)
            }
    }

    fun signOutAndRedirect() {
        sharedPreferences.edit { clear() }

        provideAuth.signOut()
        error.value = "Signing out..."
        isNavigating.value = true
    }
}

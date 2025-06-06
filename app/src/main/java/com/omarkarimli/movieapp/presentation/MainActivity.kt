package com.omarkarimli.movieapp.presentation

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.omarkarimli.movieapp.R
import com.omarkarimli.movieapp.databinding.ActivityMainBinding
import com.omarkarimli.movieapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setTheme()

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setBottomNavigation()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private val hiddenBottomNavDestinations = setOf(
        R.id.splashFragment,
        R.id.onBoardingFragment,
        R.id.loginFragment,
        R.id.registerFragment,
        R.id.searchFragment,
        R.id.settingsFragment,
        R.id.editProfileFragment,
        R.id.movieFragment
    )

    private fun setBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        NavigationUI.setupWithNavController(binding.bottomNav, navHostFragment.navController)

        navHostFragment.navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNav.visibility =
                if (destination.id in hiddenBottomNavDestinations) View.GONE
                else View.VISIBLE
        }
    }

    private fun setTheme() {
        val isDarkMode = sharedPreferences.getBoolean(Constants.DARK_MODE, false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}
package com.omarkarimli.movieapp.menu

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.omarkarimli.movieapp.R
import com.omarkarimli.movieapp.domain.models.Movie
import com.omarkarimli.movieapp.domain.repository.MovieRepository
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class MorePopupMenuHandler @Inject constructor(
    private val repo: MovieRepository
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun showPopupMenu(
        context: Context,
        anchoredView: View,
        movie: Movie,
    ) {
        val popupMenu = PopupMenu(context, anchoredView)
        popupMenu.menuInflater.inflate(R.menu.more_popup_menu, popupMenu.menu)

        coroutineScope.launch {
            val isBookmarked = try {
                val result = movie.id?.let { repo.getMovieByIdLocally(it) }
                result != null
            } catch (e: Exception) {
                Log.e("MorePopupMenuHandler", "Error checking bookmark status", e)
                false
            }

            withContext(Dispatchers.Main) {
                popupMenu.menu.findItem(R.id.action_bookmark).title =
                    if (isBookmarked) "Unbookmark" else "Bookmark"

                popupMenu.setOnMenuItemClickListener { item ->
                    handleMenuClick(context, item, movie, isBookmarked)
                }

                popupMenu.show()
            }
        }
    }

    private fun handleMenuClick(
        context: Context,
        item: MenuItem,
        movie: Movie,
        isBookmarked: Boolean
    ): Boolean {
        return when (item.itemId) {
            R.id.action_bookmark -> {
                toggleBookmark(context, movie, isBookmarked)
                true
            }
            R.id.action_share -> {
                shareMovie(context, movie)
                true
            }
            else -> false
        }
    }

    private fun toggleBookmark(
        context: Context,
        movie: Movie,
        isBookmarked: Boolean
    ) {
        coroutineScope.launch {
            try {
                if (isBookmarked) {
                    movie.id?.let {
                        repo.deleteMovieByIdLocally(it)
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Unbookmarked!", Toast.LENGTH_SHORT).show()
                        }
                    } ?: run {
                        Log.e("MorePopupMenuHandler", "Movie ID is null while unbookmarking")
                    }
                } else {
                    repo.addMovieLocally(movie)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("MorePopupMenuHandler", "Bookmark toggle failed", e)
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "An error occurred while toggling bookmark", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun shareMovie(context: Context, movie: Movie) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Check out this movie: ${movie.title}")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}

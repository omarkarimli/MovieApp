package com.omarkarimli.movieapp.menu

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.Toast
import com.omarkarimli.movieapp.R
import com.omarkarimli.movieapp.data.source.local.LocalDataSourceImpl
import com.omarkarimli.movieapp.domain.models.Movie
import dagger.hilt.android.scopes.ActivityScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityScoped
class MorePopupMenuHandler @Inject constructor(
    private val localDataSource: LocalDataSourceImpl
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    fun showPopupMenu(context: Context, anchoredView: View, movie: Movie) {
        val popupMenu = PopupMenu(context, anchoredView)
        popupMenu.menuInflater.inflate(R.menu.more_popup_menu, popupMenu.menu)

        coroutineScope.launch {
            val isBookmarked = movie.id?.let { localDataSource.getMovieByIdLocally(it) } != null

            withContext(Dispatchers.Main) {
                popupMenu.menu.findItem(R.id.action_bookmark).title =
                    if (isBookmarked) "Unbookmark" else "Bookmark"

                popupMenu.setOnMenuItemClickListener { item ->
                    val result = handleMenuClick(context, item, movie, isBookmarked)
                    result
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
                shareArticle(context, movie)
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
            if (isBookmarked) {
                localDataSource.deleteMovieLocally(movie)
                Toast.makeText(context, "Unbookmarked!", Toast.LENGTH_SHORT).show()
            } else {
                localDataSource.addMovieLocally(movie)
                Toast.makeText(context, "Bookmarked!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun shareArticle(context: Context, movie: Movie) {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "Check this Movie\n${movie.title}")
            type = "text/plain"
        }
        context.startActivity(Intent.createChooser(shareIntent, "Share via"))
    }
}

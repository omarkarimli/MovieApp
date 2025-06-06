package com.omarkarimli.movieapp.presentation.ui.bookmark

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.omarkarimli.movieapp.adapters.MovieAdapter
import com.omarkarimli.movieapp.databinding.FragmentBookmarkBinding
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class BookmarkFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val movieAdapter = MovieAdapter()
    private val viewModel by viewModels<BookmarkViewModel>()

    private var _binding: FragmentBookmarkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookmarkBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchMovies()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieAdapter.onMoreClick = { context, anchoredView, movie ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, movie)
        }
        movieAdapter.onItemClick = {
            if (it.id != null) {
                val action = BookmarkFragmentDirections.actionBookmarkFragmentToMovieFragment(it.id)
                findNavController().navigate(action)
            }
        }

        binding.rvMovies.adapter = movieAdapter

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            val searchQuery = inputText.toString().trim()
            viewModel.updateFilteredMovies(searchQuery)
        }

        observeData()
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.filteredMovies.collect { filteredArticles ->
                    movieAdapter.updateList(filteredArticles)
                    updateEmptyState(filteredArticles.isEmpty())
                }
            }

            launch {
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.apply {
                        if (isLoading) visibleItem() else goneItem()
                    }
                }
            }

            launch {
                viewModel.error.collect { errorMessage ->
                    errorMessage?.let {
                        Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                        viewModel.clearError() // Clear error after showing
                    }
                }
            }

            launch {
                viewModel.empty.collect { isEmpty ->
                    updateEmptyState(isEmpty)
                }
            }
        }
    }

    private fun updateEmptyState(isEmpty: Boolean) {
        if (isEmpty) {
            binding.empty.visibleItem()
            binding.rvMovies.goneItem()
        } else {
            binding.empty.goneItem()
            binding.rvMovies.visibleItem()
        }
    }
}

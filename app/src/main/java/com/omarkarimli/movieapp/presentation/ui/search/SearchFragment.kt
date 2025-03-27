package com.omarkarimli.movieapp.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.movieapp.adapters.MovieAdapter
import com.omarkarimli.movieapp.databinding.FragmentSearchBinding
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val movieAdapter = MovieAdapter()

    private val viewModel by viewModels<SearchViewModel>()
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchMovies(viewModel.currentPage.value ?: 1)
        binding.editTextSearch.setText("")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        movieAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        movieAdapter.onItemClick = {
            if (it.id != null) {
                val action = SearchFragmentDirections.actionSearchFragmentToMovieFragment(it.id)
                findNavController().navigate(action)
            }
        }

        binding.rvMovies.adapter = movieAdapter

        binding.btnNext.setOnClickListener {
            viewModel.changePage(true) // Move to next page
        }

        binding.btnPrev.setOnClickListener {
            viewModel.changePage(false) // Move to previous page
        }

        binding.editTextSearch.doOnTextChanged { inputText, _, _, _ ->
            val searchQuery = inputText.toString().trim()

            if (searchQuery.isNotEmpty()) {
                // Search in articles
                viewModel.searchMovies(searchQuery, viewModel.currentPage.value ?: 1)
            } else {
                // Reset
                viewModel.fetchMovies(viewModel.currentPage.value ?: 1)
            }
        }

        observeData()
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) visibleItem() else goneItem()
            }

            binding.layoutPagination.apply {
                if (isLoading) goneItem() else visibleItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies != null) {
                movieAdapter.updateList(movies)
            }

            // Check if the result is empty
            binding.empty.apply {
                if (movies.isNullOrEmpty()) visibleItem() else goneItem()
            }
        }

        viewModel.currentPage.observe(viewLifecycleOwner) { page ->
            val totalPages = viewModel.totalPages.value ?: 1 // Ensure it's at least 1

            binding.btnPrev.visibility = if (page > 1) View.VISIBLE else View.INVISIBLE
            binding.btnNext.visibility = if (page < totalPages) View.VISIBLE else View.INVISIBLE

            binding.tvPageNumber.text = "$page"
        }

        viewModel.totalPages.observe(viewLifecycleOwner) { totalPages ->
            binding.tvTotalPageNumber.text = "$totalPages"

            binding.btnPrev.visibility = if (viewModel.currentPage.value!! > 1) View.VISIBLE else View.INVISIBLE
            binding.btnNext.visibility = if (viewModel.currentPage.value!! < totalPages) View.VISIBLE else View.INVISIBLE
        }
    }

}

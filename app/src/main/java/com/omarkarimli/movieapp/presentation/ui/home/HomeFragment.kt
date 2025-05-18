package com.omarkarimli.movieapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.omarkarimli.movieapp.adapters.GenreAdapter
import com.omarkarimli.movieapp.adapters.MovieAdapter
import com.omarkarimli.movieapp.adapters.TrendingAdapter
import com.omarkarimli.movieapp.databinding.FragmentHomeBinding
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.Constants
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.loadFromUrlToImage
import com.omarkarimli.movieapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val trendingAdapter = TrendingAdapter()
    private val genreAdapter = GenreAdapter()
    private val movieAdapter = MovieAdapter()

    private val viewModel by viewModels<HomeViewModel>()
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupAdapters()
        setupListeners()
        observeData()

        // Initial data load
        viewModel.fetchMovies()
        viewModel.fetchGenres()
    }

    private fun setupAdapters() {
        binding.rvTrending.adapter = trendingAdapter
        binding.rvGenres.adapter = genreAdapter
        binding.rvMovies.adapter = movieAdapter
    }

    private fun setupListeners() {
        binding.imageViewSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        binding.btnNext.setOnClickListener {
            viewModel.changePage(true)
        }

        binding.btnPrev.setOnClickListener {
            viewModel.changePage(false)
        }

        genreAdapter.onItemClick = { genre ->
            if (genre.id == -1) {
                viewModel.fetchMovies()
            } else {
                viewModel.fetchMoviesByGenre(genre.id)
            }

            genreAdapter.updateList(
                viewModel.genres.value.map {
                    it.copy(isSelected = it.id == genre.id)
                }
            )
        }

        movieAdapter.onMoreClick = { context, anchor, movie ->
            morePopupMenuHandler.showPopupMenu(context, anchor, movie)
        }

        trendingAdapter.onMoreClick = { context, anchor, movie ->
            morePopupMenuHandler.showPopupMenu(context, anchor, movie)
        }

        movieAdapter.onItemClick = { movie ->
            movie.id?.let {
                val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(it)
                findNavController().navigate(action)
            }
        }

        trendingAdapter.onItemClick = { movie ->
            movie.id?.let {
                val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(it)
                findNavController().navigate(action)
            }
        }
    }

    private fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            launch {
                viewModel.loading.collect { isLoading ->
                    binding.progressBar.apply {
                        if (isLoading) visibleItem() else goneItem()
                    }

                    binding.layoutPagination.apply {
                        if (isLoading) goneItem() else visibleItem()
                    }
                }
            }

            launch {
                viewModel.error.collect { errorMessage ->
                    if (!errorMessage.isNullOrEmpty()) {
                        Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
                    }
                }
            }

            launch {
                viewModel.movies.collect { movies ->
                    if (!movies.isNullOrEmpty()) {
                        movieAdapter.updateList(movies)
                        trendingAdapter.updateList(movies.take(Constants.TRENDING_VALUE))
                        updateTopMovieSection(movies.first())
                    } else {
                        clearTopMovieSection()
                    }
                }
            }

            launch {
                viewModel.currentPage.collect { page ->
                    binding.tvPageNumber.text = "$page"
                    binding.btnPrev.visibility = if (page > 1) View.VISIBLE else View.INVISIBLE
                }
            }

            launch {
                viewModel.totalPages.collect { totalPages ->
                    binding.tvTotalPageNumber.text = "$totalPages"
                    binding.btnNext.visibility = if (viewModel.currentPage.value < totalPages) View.VISIBLE else View.INVISIBLE
                }
            }

            launch {
                viewModel.genres.collect { genreList ->
                    genreAdapter.updateList(genreList)
                }
            }
        }
    }

    private fun updateTopMovieSection(movie: com.omarkarimli.movieapp.domain.models.Movie) {
        binding.imageViewMovie.loadFromUrlToImage(movie.posterPath)
        binding.textViewTopMovie.text = movie.title

        binding.buttonNavigate.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(movie.id!!)
            findNavController().navigate(action)
        }

        binding.buttonMore.setOnClickListener {
            morePopupMenuHandler.showPopupMenu(it.context, it, movie)
        }
    }

    private fun clearTopMovieSection() {
        binding.imageViewMovie.setImageDrawable(null)
        binding.textViewTopMovie.text = ""
        binding.buttonNavigate.setOnClickListener(null)
        binding.buttonMore.setOnClickListener(null)
    }
}

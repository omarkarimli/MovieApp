package com.omarkarimli.movieapp.presentation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.movieapp.adapters.GenreAdapter
import com.omarkarimli.movieapp.adapters.MovieAdapter
import com.omarkarimli.movieapp.databinding.FragmentHomeBinding
import com.omarkarimli.movieapp.utils.Constants
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.visibleItem
import com.omarkarimli.movieapp.adapters.TrendingAdapter
import com.omarkarimli.movieapp.domain.models.GenreModel
import dagger.hilt.android.AndroidEntryPoint
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

    override fun onResume() {
        super.onResume()

        viewModel.fetchMovies(viewModel.currentPage.value ?: 1)
        viewModel.fetchGenres()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.editTextSearch.setOnClickListener {
            val action = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
            findNavController().navigate(action)
        }

        trendingAdapter.onMoreClick = { context, anchoredView, movie ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, movie)
        }
        movieAdapter.onMoreClick = { context, anchoredView, article ->
            morePopupMenuHandler.showPopupMenu(context, anchoredView, article)
        }
        movieAdapter.onItemClick = {
            if (it.id != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(it.id)
                findNavController().navigate(action)
            }
        }
        trendingAdapter.onItemClick = {
            if (it.id != null) {
                val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(it.id)
                findNavController().navigate(action)
            }
        }

        genreAdapter.onItemClick = {
            viewModel.fetchMoviesByGenre(it.id, viewModel.currentPage.value!!)

            genreAdapter.updateList(
                viewModel.genres.value?.map { genre ->
                    genre.copy(isSelected = genre.id == it.id)
                } ?: emptyList()
            )
        }

        binding.rvTrending.adapter = trendingAdapter
        binding.rvGenres.adapter = genreAdapter
        binding.rvMovies.adapter = movieAdapter

        binding.btnNext.setOnClickListener {
            viewModel.changePage(true) // Move to next page
        }

        binding.btnPrev.setOnClickListener {
            viewModel.changePage(false) // Move to previous page
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

        viewModel.movies.observe(viewLifecycleOwner) {
            if (it != null) {
                movieAdapter.updateList(it)
                trendingAdapter.updateList(it.take(Constants.TRENDING_VALUE))
            }
        }

        viewModel.currentPage.observe(viewLifecycleOwner) { page ->
            binding.btnPrev.isEnabled = page > 1
            binding.btnNext.isEnabled = page < binding.tvTotalPageNumber.text.toString().toInt()

            binding.tvPageNumber.text = "$page"
        }

        viewModel.totalPages.observe(viewLifecycleOwner) { totalPages ->
            binding.tvTotalPageNumber.text = "$totalPages"
        }

        viewModel.genres.observe(viewLifecycleOwner) {
            genreAdapter.updateList(it)
        }
    }
}

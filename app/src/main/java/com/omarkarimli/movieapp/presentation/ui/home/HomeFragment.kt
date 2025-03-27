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
import com.omarkarimli.movieapp.adapters.TrendingAdapter
import com.omarkarimli.movieapp.databinding.FragmentHomeBinding
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.Constants
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.loadFromUrlToImage
import com.omarkarimli.movieapp.utils.visibleItem
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

        binding.imageViewSearch.setOnClickListener {
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
            if (it.id == -1) {
                viewModel.fetchMovies(viewModel.currentPage.value!!)
            } else {
                viewModel.fetchMoviesByGenre(it.id, viewModel.currentPage.value!!)
            }

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

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies != null) {
                movieAdapter.updateList(movies)
                trendingAdapter.updateList(movies.take(Constants.TRENDING_VALUE))

                // apply top movie
                val topMovie = movies[0]
                binding.imageViewMovie.loadFromUrlToImage(topMovie.posterPath)
                binding.textViewTopMovie.text = topMovie.title
                binding.buttonNavigate.setOnClickListener {
                    val action = HomeFragmentDirections.actionHomeFragmentToMovieFragment(topMovie.id!!)
                    findNavController().navigate(action)
                }

                binding.buttonMore.setOnClickListener {
                    morePopupMenuHandler.showPopupMenu(it.context, it, topMovie)
                }
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

        viewModel.genres.observe(viewLifecycleOwner) {
            genreAdapter.updateList(it)
        }
    }
}

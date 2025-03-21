package com.omarkarimli.movieapp.presentation.ui.movie

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.omarkarimli.movieapp.databinding.FragmentMovieBinding
import com.omarkarimli.movieapp.menu.MorePopupMenuHandler
import com.omarkarimli.movieapp.utils.getFormattedDate
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.loadFromUrlToImage
import com.omarkarimli.movieapp.utils.visibleItem
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MovieFragment : Fragment() {

    private val args by navArgs<MovieFragmentArgs>()

    @Inject
    lateinit var morePopupMenuHandler: MorePopupMenuHandler

    private val viewModel by viewModels<MovieViewModel>()
    private var _binding: FragmentMovieBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.getMovieById(args.id)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonMore.setOnClickListener {
            if (viewModel.movie.value != null) {
                morePopupMenuHandler.showPopupMenu(it.context, it, viewModel.movie.value!!)
            }
        }

        observeData()
    }

    private fun observeData() {
        viewModel.loading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.apply {
                if (isLoading) visibleItem() else goneItem()
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            if (!errorMessage.isNullOrEmpty()) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_LONG).show()
            }
        }

        viewModel.movie.observe(viewLifecycleOwner) {
            binding.apply {
                imageViewMovie.loadFromUrlToImage(it.posterPath)
                imageViewMovieCopy.loadFromUrlToImage(it.posterPath)

                textViewTitle.text = it.title
                textViewOverview.text = it.overview ?: "No overview found"
                textViewReleaseDate.text = it.releaseDate?.getFormattedDate()

                textViewVoteAverage.text = it.voteAverage.toString()
                textViewVoteCount.text = "(${it.voteCount} votes)"

                textViewAdult.visibility = if (it.adult == true) View.VISIBLE else View.GONE
            }
        }

        viewModel.videoKey.observe(viewLifecycleOwner) { key ->
            if (!key.isNullOrEmpty()) {
                setupYouTubePlayer(key)
                binding.youtubePlayerView.visibleItem()
            } else {
                Log.e("YouTubePlayer", "No valid video key found!")
                binding.youtubePlayerView.goneItem()

                // Show "No Trailer Available" message
                viewModel.error.value = "No Trailer Available"
                binding.youtubePlayerView.goneItem()
            }
        }
    }

    private fun setupYouTubePlayer(videoKey: String) {
        lifecycle.addObserver(binding.youtubePlayerView)
        binding.youtubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                youTubePlayer.loadVideo(videoKey, 0f)
            }
        })
    }
}

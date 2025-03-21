package com.omarkarimli.movieapp.presentation.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener {
            findNavController().navigateUp()
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
        viewModel.empty.observe(viewLifecycleOwner) { isEmpty ->
            if (isEmpty) {
                binding.empty.visibleItem()
                binding.rvCustom.goneItem()
            } else {
                binding.empty.goneItem()
                binding.rvCustom.visibleItem()
            }
        }
    }
}

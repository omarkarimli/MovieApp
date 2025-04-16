package com.omarkarimli.movieapp.presentation.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.omarkarimli.movieapp.databinding.FragmentProfileBinding
import com.omarkarimli.movieapp.utils.goneItem
import com.omarkarimli.movieapp.utils.visibleItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private val viewModel by viewModels<ProfileViewModel>()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()

        viewModel.fetchUserData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutEditProfile.setOnClickListener {
            val userData = viewModel.userData.value
            if (userData == null) {
                Toast.makeText(requireContext(), "User data is not available", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else {
                val action = ProfileFragmentDirections.actionProfileFragmentToEditProfileFragment(userData)
                findNavController().navigate(action)
            }
        }

        binding.layoutSettings.setOnClickListener {
            val action = ProfileFragmentDirections.actionProfileFragmentToSettingsFragment()
            findNavController().navigate(action)
        }

        binding.layoutWebsite.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = viewModel.userData.value?.website?.toUri()
            startActivity(intent)
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

        viewModel.userData.observe(viewLifecycleOwner) {
            binding.apply {
                textViewNameSurname.text = "${it?.name} ${it?.surname}"
                textViewBio.text = it?.bio
            }
        }
    }
}
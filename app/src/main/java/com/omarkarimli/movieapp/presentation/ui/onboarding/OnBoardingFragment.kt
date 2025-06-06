package com.omarkarimli.movieapp.presentation.ui.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.omarkarimli.movieapp.R
import com.omarkarimli.movieapp.adapters.OnBoardingAdapter
import com.omarkarimli.movieapp.databinding.FragmentOnBoardingBinding
import com.omarkarimli.movieapp.domain.models.OnBoardingModel

class OnBoardingFragment : Fragment() {

    private val adapter = OnBoardingAdapter()

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val onBoardingList = listOf(
            OnBoardingModel(
                R.drawable.onboarding1,
                "Welcome to Mova",
                "The best place to watch movies."
            ),
            OnBoardingModel(
                R.drawable.onboarding2,
                "Discover New Movies",
                "Find and explore the latest and\ntrending movies with ease."
            ),
            OnBoardingModel(
                R.drawable.onboarding3,
                "Enjoy Streaming",
                "Watch your favorite movies anytime,\nanywhere without interruptions."
            ),
        )

        adapter.updateList(onBoardingList)
        binding.viewPager2.adapter = adapter
        binding.dotsIndicator.attachTo(binding.viewPager2)

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == onBoardingList.size - 1) {
                    binding.buttonNext.text = "Get Started"
                } else {
                    binding.buttonNext.text = "Next"
                }
            }
        })

        binding.buttonNext.setOnClickListener {
            if (binding.viewPager2.currentItem < onBoardingList.size - 1) {
                binding.viewPager2.currentItem += 1
            } else {
                val action = OnBoardingFragmentDirections.actionOnBoardingFragmentToLoginFragment()
                findNavController().navigate(action)
            }
        }
    }
}
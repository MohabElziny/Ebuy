package com.iti.android.team1.ebuy.ui.onbording.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentOnBoardingBinding
import com.iti.android.team1.ebuy.data.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.data.repository.Repository
import com.iti.android.team1.ebuy.ui.onbording.viewmodel.OnBindingFactory
import com.iti.android.team1.ebuy.ui.onbording.viewmodel.OnBoardingViewModel
import kotlinx.android.synthetic.main.fragment_on_boarding.*

class OnBoardingFragment : Fragment() {

    private var _binding: FragmentOnBoardingBinding? = null
    private val binding get() = _binding!!

    private var _adapter: OnBoardingAdapter? = null
    private val adapter get() = _adapter!!

    val viewModel: OnBoardingViewModel by viewModels {
        OnBindingFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _adapter = OnBoardingAdapter()
        binding.viewPager2.adapter = adapter
        binding.wormDotsIndicator.attachTo(binding.viewPager2)
        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == 0) binding.btnPrev.visibility = View.GONE
                else binding.btnPrev.visibility = View.VISIBLE
            }
        })
        binding.btnNext.setOnClickListener {
            if (viewPager2.currentItem + 1 < adapter.list.size)
                viewPager2.currentItem += 1
            else {
                navigateToLoginScreen()
            }
        }
        binding.btnPrev.setOnClickListener {
            if (viewPager2.currentItem - 1 >= 0)
                viewPager2.currentItem -= 1
        }
        binding.btnSkip.setOnClickListener {
            navigateToLoginScreen()
        }
    }

    private fun navigateToLoginScreen() {
        viewModel.setRunFirstTime()
        findNavController().popBackStack()
        findNavController().navigate(R.id.loginScreen2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _binding = null
    }

}
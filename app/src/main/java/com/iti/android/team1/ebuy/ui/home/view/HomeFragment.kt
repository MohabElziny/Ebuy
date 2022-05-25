package com.iti.android.team1.ebuy.ui.home.view


import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentHomeBinding
import com.iti.android.team1.ebuy.ui.home.adapters.HomeViewPagerAdapter
import com.iti.android.team1.ebuy.ui.home.viewmodel.HomeViewModel
import com.iti.android.team1.ebuy.util.ZoomOutPageTransformer

private const val TAG = "HomeFragment"
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        
        binding.btnViewAll.setOnClickListener {
            Log.d(TAG, "onViewCreated: ")
            findNavController().navigate(HomeFragmentDirections.actionNavigationHomeToNavigationProducts())
        }

        val adapter = HomeViewPagerAdapter()
        binding.viewPager2.adapter = adapter
        binding.wormDotsIndicator.attachTo(binding.viewPager2)
        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> {//TODO: Navigate to settings screen
            }
            R.id.action_about -> {//TODO: Navigate to about screen
                Log.d(TAG, "onOptionsItemSelected: ")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
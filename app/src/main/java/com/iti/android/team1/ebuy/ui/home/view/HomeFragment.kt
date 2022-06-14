package com.iti.android.team1.ebuy.ui.home.view

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context.CLIPBOARD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentHomeBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Brands
import com.iti.android.team1.ebuy.model.pojo.DiscountCodes
import com.iti.android.team1.ebuy.ui.home.adapters.HomeRecyclerAdapter
import com.iti.android.team1.ebuy.ui.home.adapters.HomeViewPagerAdapter
import com.iti.android.team1.ebuy.ui.home.viewmodel.HomeViewModel
import com.iti.android.team1.ebuy.ui.home.viewmodel.HomeViewModelFactory
import com.iti.android.team1.ebuy.util.ZoomOutPageTransformer
import kotlinx.coroutines.flow.buffer

class HomeFragment : Fragment() {
    private val homeViewModel: HomeViewModel by viewModels {
        HomeViewModelFactory(Repository(LocalSource(requireContext())))
    }
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var brandsAdapter: HomeRecyclerAdapter
    private lateinit var discountAdapter: HomeViewPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        initAdsViewPager()
        initBrandsRecyclerView()
        handleDiscountCodes()
        homeViewModel.getAllBrands()
        lifecycleScope.launchWhenStarted {
            homeViewModel.brandsResult.buffer().collect { brandsResult ->
                handleResultStates(brandsResult)
            }
        }
    }

    private fun handleDiscountCodes() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.getAllDiscount()
            homeViewModel.discountCodeList.buffer().collect { result ->
                when (result) {
                    ResultState.EmptyResult -> discountAdapter.discountCodeList = emptyList()
//                    is ResultState.Error -> TODO()
//                    ResultState.Loading -> TODO()
                    is ResultState.Success -> {
                        result.data.forEach {
                            Log.i("AHMED", ": ${it.code}")
                        }
                        discountAdapter.discountCodeList = result.data
                    }
                }
            }
        }
    }

    private fun handleResultStates(brandsResult: ResultState<Brands>) {
        when (brandsResult) {
            ResultState.EmptyResult -> {
                hideShimmer()
                binding.homeRecyclerview.visibility = View.GONE
                binding.emptyLayout.root.visibility = View.VISIBLE
                binding.emptyLayout.txt.text = getString(R.string.no_brands_found)
            }
            is ResultState.Error -> {
                hideShimmer()
                Toast.makeText(requireContext(), brandsResult.errorString, Toast.LENGTH_LONG).show()
            }
            ResultState.Loading -> {
                showShimmer()
            }
            is ResultState.Success -> {
                hideShimmer()
                binding.homeRecyclerview.visibility = View.VISIBLE
                binding.emptyLayout.root.visibility = View.GONE
                brandsAdapter.setBrandsList(brandsResult.data)
            }
        }
    }

    private fun initBrandsRecyclerView() {
        brandsAdapter = HomeRecyclerAdapter(
            onBrandClick
        )
        binding.homeRecyclerview.apply {
            layoutManager = GridLayoutManager(
                requireContext(),
                2,
                RecyclerView.VERTICAL,
                false
            )
            adapter = brandsAdapter
        }
    }

    private fun initAdsViewPager() {
        discountAdapter = HomeViewPagerAdapter(showDiscountDialog)
        binding.viewPager2.adapter = discountAdapter
        binding.wormDotsIndicator.attachTo(binding.viewPager2)
        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }
    }

    private fun copyTextToClipboard(text: String) {
        val clipboardManager =
            requireContext().getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("text", text)
        clipboardManager.setPrimaryClip(clipData)
        Toast.makeText(requireContext(), "Code Copied", Toast.LENGTH_LONG).show()
    }

    private val showDiscountDialog: (DiscountCodes) -> Unit = {
        MaterialAlertDialogBuilder(this.requireContext())
            .setTitle(getString(R.string.discount))
            .setMessage("${it.code}")
            .setNegativeButton(getString(R.string.copy_code)) { dialog, which ->
                copyTextToClipboard(it.code ?: "0")
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.ok)) { dialog, which ->
                // Respond to positive button press
                dialog.dismiss()
            }
            .show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.home_menu, menu)
        val searchView = menu.findItem(R.id.action_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.brands_search)
        onQueryTextListener(searchView)
        onCloseSearch(searchView)
    }

    private fun onCloseSearch(searchView: SearchView) {
        searchView.setOnCloseListener {
            homeViewModel.getBrandsAgain()
            return@setOnCloseListener false
        }
    }

    private fun onQueryTextListener(searchView: SearchView) {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    homeViewModel.setSearchQuery(it)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    homeViewModel.setSearchQuery(query)
                }
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_settings -> findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToSettingsFragment())
            R.id.action_about -> {//TODO: Navigate to about screen
            }
            R.id.action_favorite -> findNavController().navigate(
                HomeFragmentDirections.actionNavigationHomeToNavigationFavorites())
        }
        return super.onOptionsItemSelected(item)
    }

    private var onBrandClick: (Long, String) -> Unit = { collectionID, brandTitle ->
        findNavController().navigate(
            HomeFragmentDirections.actionNavigationHomeToNavigationProducts(
                brandTitle,
                collectionID
            )
        )
    }

    private fun showShimmer() {
        binding.brandsShimmer.root.apply {
            showShimmer(true)
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.brandsShimmer.root.apply {
            hideShimmer()
            stopShimmer()
            visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
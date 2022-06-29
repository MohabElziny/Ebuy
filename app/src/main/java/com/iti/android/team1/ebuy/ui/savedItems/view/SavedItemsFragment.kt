package com.iti.android.team1.ebuy.ui.savedItems.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentSavedItemsBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.ui.savedItems.adapter.SavedRecyclerAdapter
import com.iti.android.team1.ebuy.ui.savedItems.viewmodel.SavedItemsViewModel
import com.iti.android.team1.ebuy.ui.savedItems.viewmodel.SavedItemsViewModelFactory
import com.iti.android.team1.ebuy.util.showSnackBar
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class SavedItemsFragment : Fragment() {

    private var _binding: FragmentSavedItemsBinding? = null
    private val binding get() = _binding!!
    private var _favoritesAdapter: SavedRecyclerAdapter? = null
    private val favoritesAdapter get() = _favoritesAdapter!!
    private var position: Int = -1

    private val viewModel: SavedItemsViewModel by viewModels {
        SavedItemsViewModelFactory(
            Repository(
                LocalSource(requireContext())
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSavedItemsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavoritesList()
        initRecyclerView()
        observeOnViewModel()
    }

    private fun observeOnViewModel() {
        lifecycleScope.launchWhenStarted {
            viewModel.allFavorites.buffer().collect { response ->
                when (response) {
                    ResultState.EmptyResult -> {
                        hideShimmer()
                        setEmptyLayout()
                    }
                    is ResultState.Error -> {
                        hideShimmer()
                        showSnackBar(response.errorString)
                    }
                    ResultState.Loading -> {
                        showShimmer()
                    }
                    is ResultState.Success -> {
                        binding.emptyLayout.root.visibility = View.GONE
                        hideShimmer()
                        binding.recyclerView.visibility = View.VISIBLE
                        favoritesAdapter.setAdapterList(response.data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteState.observe(viewLifecycleOwner) { response ->
                binding.unLikeProgressBar.visibility = View.GONE
                binding.invisableImage.visibility = View.GONE
                when (response) {
                    is ResultState.Error -> showSnackBar(response.errorString)
                    ResultState.EmptyResult -> setEmptyLayout()
                    is ResultState.Success -> favoritesAdapter.removeItemFromList(position)
                }
            }
        }
    }

    private fun setEmptyLayout() {
        binding.emptyLayout.root.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.GONE
        binding.emptyLayout.txt.text = getString(R.string.favorite_empty_list_title)
    }

    private fun initRecyclerView() {
        _favoritesAdapter = SavedRecyclerAdapter(
            onItemClick, onUnlike
        )

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = favoritesAdapter
        }
    }

    private var onItemClick: (Long) -> Unit = { productId ->
        findNavController().navigate(
            SavedItemsFragmentDirections.actionNavigationFavoritesToProductsDetailsFragment(
                productId)
        )
    }

    private var onUnlike: (Long, Int) -> Unit = { productId, position ->
        binding.unLikeProgressBar.visibility = View.VISIBLE
        binding.invisableImage.visibility = View.VISIBLE
        this.position = position
        viewModel.deleteFavoriteProduct(productId)
    }

    private fun showShimmer() {
        binding.recyclerView.visibility = View.GONE
        binding.shimmer.root.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.recyclerView.visibility = View.VISIBLE
        binding.shimmer.root.apply {
            startShimmer()
            visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        position = -1
        _favoritesAdapter = null
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        viewModel.reloadStates()
    }
}
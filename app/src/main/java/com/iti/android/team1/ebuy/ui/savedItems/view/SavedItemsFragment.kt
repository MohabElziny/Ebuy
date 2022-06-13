package com.iti.android.team1.ebuy.ui.savedItems.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentSavedItemsBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.ui.savedItems.adapter.SavedRecyclerAdapter
import com.iti.android.team1.ebuy.ui.savedItems.viewmodel.SavedItemsViewModel
import com.iti.android.team1.ebuy.ui.savedItems.viewmodel.SavedItemsViewModelFactory
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class SavedItemsFragment : Fragment() {

    private lateinit var binding: FragmentSavedItemsBinding
    private lateinit var favoritesAdapter: SavedRecyclerAdapter
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
        binding = FragmentSavedItemsBinding.inflate(inflater, container, false)
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
                    ResultState.EmptyResult -> setEmptyLayout()
                    is ResultState.Error -> Toast.makeText(requireContext(),
                        response.errorString, Toast.LENGTH_SHORT).show()
                    ResultState.Loading -> {
                        showShimmer()
                    }
                    is ResultState.Success -> {
                        binding.emptyLayout.root.visibility = View.GONE
                        binding.recyclerView.visibility = View.VISIBLE
                        favoritesAdapter.setAdapterList(response.data)
                    }
                }
            }
        }

        lifecycleScope.launch {
            viewModel.deleteState.observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ResultState.Error -> Toast.makeText(requireContext(),
                        response.errorString, Toast.LENGTH_SHORT).show()
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
        favoritesAdapter = SavedRecyclerAdapter(
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

}
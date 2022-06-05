package com.iti.android.team1.ebuy.ui.profile_screen.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.MainActivity
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentProfileBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.OrdersAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.ProfileFavoritesAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileVMFactory
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileViewModel
import com.iti.android.team1.ebuy.ui.savedItems.view.SavedItemsFragmentDirections
import kotlinx.coroutines.flow.buffer

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels {
        ProfileVMFactory(Repository(LocalSource(requireContext().applicationContext)))
    }
    private val binding get() = _binding!!
    private lateinit var ordersAdapter: OrdersAdapter
    private lateinit var profileFavoritesAdapter: ProfileFavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrdersRecyclerView()
        initFavoritesRecyclerView()
        handleCustomerInfo()
        handleCustomerOrders()
        handleCustomerFavProducts()
        binding.btnMoreFavorites.setOnClickListener {
            (activity as MainActivity).profileNavigation()
            findNavController().navigate(R.id.action_navigation_profile_to_navigation_favorites)
        }
        binding.btnMoreOrders.setOnClickListener {

        }


    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setDefault()
    }


    private var onItemClick: (Long) -> Unit = { productId ->
        findNavController().navigate(
            SavedItemsFragmentDirections.actionNavigationFavoritesToProductsDetailsFragment(
                productId)
        )
    }

    private var onUnlike: (Long) -> Unit = { productId ->
        viewModel.deleteFavoriteProduct(productId)
        lifecycleScope.launchWhenStarted {
            viewModel.deleteState.buffer().collect { response ->
                when (response) {
                    is ResultState.Success -> {
                        Toast.makeText(requireContext(),
                            "Delete Done",
                            Toast.LENGTH_SHORT).show()
                    }
                    is ResultState.Error -> {
                        Toast.makeText(requireContext(),
                            response.errorString,
                            Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun handleCustomerFavProducts() {
        viewModel.getFavouriteProducts()
        lifecycleScope.launchWhenStarted {
            viewModel.favoriteProducts.buffer().collect { result ->
                when (result) {
                    ResultState.EmptyResult -> profileFavoritesAdapter.setFavouriteList(emptyList())
//                    is ResultState.Error -> TODO()
//                    ResultState.Loading -> TODO()
                    is ResultState.Success -> {
                        profileFavoritesAdapter.setFavouriteList(result.data)
                    }
                }
            }
        }
    }

    private fun handleCustomerOrders() {
        viewModel.getCustomerOrders()
        lifecycleScope.launchWhenStarted {
            viewModel.orderList.buffer().collect { result ->
                when (result) {
                    ResultState.EmptyResult -> ordersAdapter.setOrderList(emptyList())
//                    is ResultState.Error -> TODO()
//                    ResultState.Loading -> TODO()
                    is ResultState.Success -> {
                        ordersAdapter.setOrderList(result.data)
                    }
                }
            }
        }
    }

    private fun handleCustomerInfo() {
        viewModel.getCustomerInfo()
        lifecycleScope.launchWhenStarted {
            viewModel.customer.buffer().collect { result ->
                when (result) {
//                    ResultState.EmptyResult -> TODO()
                    is ResultState.Error -> {
                        binding.txtWelcomeMessage.text = getString(R.string.notExist)
                    }
//                    ResultState.Loading -> TODO()
                    is ResultState.Success -> {
                        binding.txtWelcomeMessage.text =
                            result.data.firstName.plus(" ").plus(result.data.lastName)
                    }
                }
            }
        }
    }

    private fun initFavoritesRecyclerView() {
        profileFavoritesAdapter = ProfileFavoritesAdapter(onItemClick, onUnlike)
        binding.favoritesRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false)
            adapter = profileFavoritesAdapter
        }
    }

    private fun initOrdersRecyclerView() {
        ordersAdapter = OrdersAdapter()
        binding.ordersRecyclerView.apply {
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.VERTICAL,
                false)
            adapter = ordersAdapter
        }
    }

}
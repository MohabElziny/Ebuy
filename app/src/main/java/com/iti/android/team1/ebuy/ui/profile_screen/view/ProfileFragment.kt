package com.iti.android.team1.ebuy.ui.profile_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentProfileBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.OrdersAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.ProfileFavoritesAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileVMFactory
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileViewModel
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect

class ProfileFragment : Fragment() {
    val customer_ID = 6432303317221
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


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrdersRecyclerView()
        initFavoritesRecyclerView()
        handleCustomerInfo()
        handleCustomerOrders()
        handleCustomerFavProducts()

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
        viewModel.getCustomerOrders(customer_ID)
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
        viewModel.getCustomerInfo(customer_ID)
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
        profileFavoritesAdapter = ProfileFavoritesAdapter()
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
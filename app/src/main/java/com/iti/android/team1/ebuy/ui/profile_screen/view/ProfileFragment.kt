package com.iti.android.team1.ebuy.ui.profile_screen.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentProfileBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.OrdersAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.ProfileFavoritesAdapter
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileVMFactory
import com.iti.android.team1.ebuy.ui.profile_screen.viewmodel.ProfileViewModel
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.profile_top_app_bar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favIcon -> findNavController().navigate(
                ProfileFragmentDirections.actionNavigationProfileToNavigationFavorites()
            )
        }
        return super.onOptionsItemSelected(item)
    }

    private var onItemClick: (Long) -> Unit = { productId ->
        findNavController().navigate(
            ProfileFragmentDirections.actionNavigationProfileToProductsDetailsFragment(productId)
        )
    }

    private var onUnlike: (Long, Int) -> Unit = { productId, index ->
        viewModel.deleteFavoriteProduct(productId)
        lifecycleScope.launchWhenStarted {
            viewModel.deleteState.buffer().collect { response ->
                when (response) {
                    is ResultState.Success -> profileFavoritesAdapter.removeItemFromList(index)
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
                    is ResultState.Error -> Toast.makeText(requireContext(),
                        result.errorString, Toast.LENGTH_SHORT).show()
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
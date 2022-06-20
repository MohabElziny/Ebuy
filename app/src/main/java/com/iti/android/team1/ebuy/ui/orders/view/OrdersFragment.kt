package com.iti.android.team1.ebuy.ui.orders.view

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
import com.iti.android.team1.ebuy.databinding.FragmentOrdersBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.model.pojo.Order
import com.iti.android.team1.ebuy.ui.orders.viewModel.OrdersViewModel
import com.iti.android.team1.ebuy.ui.orders.viewModel.OrdersViewModelFactory
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.OrdersAdapter
import kotlinx.coroutines.flow.buffer


class OrdersFragment : Fragment() {


    val viewModel: OrdersViewModel by viewModels {
        OrdersViewModelFactory(Repository(LocalSource(requireContext())))
    }

    private var _binding: FragmentOrdersBinding? = null
    private val binding get() = _binding!!
    private var _ordersAdapter: OrdersAdapter? = null
    private val ordersAdapter get() = _ordersAdapter!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrdersRecycler()
        handleCustomerOrders()
    }

    private fun handleCustomerOrders() {
        viewModel.getCustomerOrders()
        lifecycleScope.launchWhenStarted {
            viewModel.customerOrders.buffer().collect { result ->
                when (result) {
                    ResultState.EmptyResult -> handleEmptyResult()
                    is ResultState.Error -> Toast.makeText(requireContext(),
                        result.errorString, Toast.LENGTH_SHORT).show()
                    ResultState.Loading -> handleLoading()
                    is ResultState.Success -> handleSuccessResult(result.data)
                }
            }
        }
    }

    private fun handleLoading() {
        showShimmer()
        binding.recycler.visibility = View.INVISIBLE
        binding.emptyLayout.root.visibility = View.INVISIBLE
    }

    private fun showShimmer() {
        binding.ordersShimmer.root.apply {
            visibility = View.VISIBLE
            showShimmer(true)
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.ordersShimmer.root.apply {
            stopShimmer()
            showShimmer(false)
            visibility = View.GONE
        }
    }

    private fun handleEmptyResult() {
        hideShimmer()
        binding.emptyLayout.root.visibility = View.VISIBLE
        binding.emptyLayout.txt.text = getString(R.string.no_orders_yet)
        binding.recycler.visibility = View.INVISIBLE
    }

    private fun handleSuccessResult(data: List<Order>) {
        hideShimmer()
        binding.emptyLayout.root.visibility = View.INVISIBLE
        binding.recycler.visibility = View.VISIBLE
        ordersAdapter.setOrderList(data)
    }

    private fun initOrdersRecycler() {
        _ordersAdapter = OrdersAdapter(onClickOrderItem)
        binding.recycler.apply {
            adapter = ordersAdapter
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }

    private val onClickOrderItem: (orderName: String, orderFinancialStatus: String, orderStatus: String)
    -> Unit = { orderName, orderFinancialStatus, orderStatus ->
        findNavController().navigate(OrdersFragmentDirections.actionOrdersFragmentToTrackOrder(
            orderFinancialStatus,
            orderName,
            orderStatus))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _ordersAdapter = null
        _binding = null
    }
}
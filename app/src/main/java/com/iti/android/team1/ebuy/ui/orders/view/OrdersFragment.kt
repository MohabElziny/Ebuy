package com.iti.android.team1.ebuy.ui.orders.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.iti.android.team1.ebuy.databinding.FragmentOrdersBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.ui.orders.viewModel.OrdersViewModel
import com.iti.android.team1.ebuy.ui.orders.viewModel.OrdersViewModelFactory
import com.iti.android.team1.ebuy.ui.profile_screen.adapters.OrdersAdapter


class OrdersFragment : Fragment() {


    val viewModel: OrdersViewModel by viewModels {
        OrdersViewModelFactory(Repository(LocalSource(requireContext())))
    }

    private lateinit var _binding: FragmentOrdersBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initOrdersRecycler()
    }

    private fun initOrdersRecycler() {
        binding.recycler.apply {
            adapter = OrdersAdapter()
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
        }
    }


}
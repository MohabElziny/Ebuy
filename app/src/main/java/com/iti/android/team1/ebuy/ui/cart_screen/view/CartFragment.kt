package com.iti.android.team1.ebuy.ui.cart_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.FragmentCartBinding
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.ui.cart_screen.viewmodel.CartVMFactory
import com.iti.android.team1.ebuy.ui.cart_screen.viewmodel.CartViewModel
import kotlinx.coroutines.flow.buffer

class CartFragment : Fragment() {
    private var _binding: FragmentCartBinding? = null
    private  val viewModel: CartViewModel by viewModels<CartViewModel> {
        CartVMFactory(Repository(LocalSource(requireContext().applicationContext)))
    }
    private lateinit var cartProductAdapter: CartProductAdapter
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCartRecycler()
        handleAllCartItems()
    }

    private fun handleAllCartItems() {
        viewModel.getAllCartItems()
        lifecycleScope.launchWhenStarted {
            viewModel.allCartItems.buffer().collect { result ->
                when (result) {
                    ResultState.EmptyResult -> cartProductAdapter.setCartProducts(emptyList())
//                    is ResultState.Error -> TODO()
//                    ResultState.Loading -> TODO()
                    is ResultState.Success -> {
                        cartProductAdapter.setCartProducts(result.data)
                    }
                }
            }
        }
    }

    // function
    private fun initCartRecycler() {
        cartProductAdapter = CartProductAdapter()
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            adapter = cartProductAdapter
        }

    }
}
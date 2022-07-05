package com.iti.android.team1.ebuy.ui.cart_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentCartBinding
import com.iti.android.team1.ebuy.data.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.data.repository.Repository
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.ui.cart_screen.adapter.CartProductAdapter
import com.iti.android.team1.ebuy.ui.cart_screen.viewmodel.CartVMFactory
import com.iti.android.team1.ebuy.ui.cart_screen.viewmodel.CartViewModel
import com.iti.android.team1.ebuy.util.showSnackBar
import kotlinx.coroutines.launch

class CartFragment : Fragment() {

    private var _binding: FragmentCartBinding? = null
    private val binding get() = _binding!!

    private var _cartProductAdapter: CartProductAdapter? = null
    private val cartProductAdapter get() = _cartProductAdapter!!

    private val viewModel: CartViewModel by viewModels<CartViewModel> {
        CartVMFactory(Repository(LocalSource(requireContext().applicationContext)))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCartBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCartRecycler()
        handleAllCartItems()
        handleCheckoutButton()
        handleOverFlow()
        handleDeleteState()
        handleCost()
        handleDiscountResult()
        observeToPriceAfterDiscount()

        binding.btnDiscount.setOnClickListener {
            viewModel.getDiscountValue(binding.etCoupon.text.trim().toString())
        }

    }

    override fun onResume() {
        super.onResume()
        clearDiscount()
    }

    private fun handleDeleteState() {
        viewModel.deleteState.observe(viewLifecycleOwner) { result ->
            when (result) {
                is ResultState.Error -> showSnackBar(result.errorString)
                ResultState.Loading -> {}
                is ResultState.Success -> {}
            }

        }
    }

    private fun handleEmptyData() {
        binding.cardLayoutSum.visibility = View.INVISIBLE
        binding.recyclerCart.visibility = View.INVISIBLE
        binding.emptyLayout.root.visibility = View.VISIBLE
        binding.emptyLayout.txt.text = getString(R.string.no_item_in_cart)
    }

    private fun handleExistData() {
        binding.cardLayoutSum.visibility = View.VISIBLE
        binding.recyclerCart.visibility = View.VISIBLE
        binding.emptyLayout.root.visibility = View.INVISIBLE
    }


    private fun handleCheckoutButton() {
        binding.btnAddCard.setOnClickListener {
            viewModel.updateToDB()
            viewModel.makeOrder(binding.etCoupon.text.trim().toString())
            viewModel.order.observe(viewLifecycleOwner) { order ->
                showSnackBar(getString(R.string.choose_address))
                val action = CartFragmentDirections.actionCartFragmentToAddressesFragment(1,
                    order,
                    dToCatr = true)
                findNavController().navigate(action)
            }
        }
    }

    private fun handleAllCartItems() {
        viewModel.getAllCartItems()
        lifecycleScope.launch {
            viewModel.allCartItems.observe(viewLifecycleOwner) { result ->
                when (result) {
                    ResultState.EmptyResult -> {
                        hideShimmer()
                        handleEmptyData()
                        cartProductAdapter.setCartItems(emptyList())
                    }
                    is ResultState.Error -> {
                        hideShimmer()
                        handleEmptyData()
                        showSnackBar(result.errorString)
                    }
                    ResultState.Loading -> {
                        showShimmer()
                    }
                    is ResultState.Success -> {
                        hideShimmer()
                        handleExistData()
                        cartProductAdapter.setCartItems(result.data)
                    }
                }
            }
        }
    }

    private fun handleCost() {
        lifecycleScope.launchWhenStarted {
            var res: Long? = null

            launch {
                viewModel.subTotal.collect { subtotal ->
                    res = subtotal
                }
            }
            launch {
                viewModel.total.collect { total ->
                    bindCostText(res ?: 0, total)
                }
            }

        }
    }


    private fun bindCostText(subTotal: Long, total: Long) {
        binding.textProductSubTotal.text = "$subTotal".plus(" EGP")
        binding.textProductLastSubTotal.text =
            "$total".plus(" EGP")
        binding.textPriceAfterDiscount.text = "$total".plus(" EGP")
    }

    // function
    private fun initCartRecycler() {
        _cartProductAdapter = CartProductAdapter(deleteQuantity, increaseQuantity, decreaseQuantity)
        binding.recyclerCart.apply {
            layoutManager = LinearLayoutManager(
                requireParentFragment().requireContext(),
                RecyclerView.VERTICAL,
                false
            )
            adapter = cartProductAdapter
        }

    }

    private val increaseQuantity: (Int) -> Unit = { it ->
        viewModel.manipulateCartItem(it, CartViewModel.CartItemOperation.INCREASE)
        clearDiscount()
    }

    private fun handleOverFlow() {
        viewModel.isOverFlow.observe(viewLifecycleOwner) { overFlow ->
            if (overFlow) showSnackBar(getString(R.string.no_more_in_stock))
        }
    }

    private val decreaseQuantity: (Int) -> Unit = {
        viewModel.manipulateCartItem(it, CartViewModel.CartItemOperation.DECREASE)
        clearDiscount()
    }

    private val deleteQuantity: (Int) -> Unit = {
        showDialog(it)
    }

    private fun showDialog(index: Int) {
        MaterialAlertDialogBuilder(binding.root.context).setTitle("Removing Cart Item")
            .setMessage("Are you sure to remove this item ?")
            .setNegativeButton("No") { dialog, _ -> dialog.dismiss() }
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.manipulateCartItem(index, CartViewModel.CartItemOperation.DELETE)
                clearDiscount()
                dialog.dismiss()
            }
            .show()
    }

    private fun handleDiscountResult() {
        viewModel.discountValue.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultState.Loading -> {
                    onDiscountResult(View.VISIBLE, false)
                    enableDiscountButton(false)
                }
                is ResultState.Success -> {
                    viewModel.calculatePriceAfterDiscount(it.data, viewModel.total.value)
                    onDiscountResult(View.GONE, true)
                    enableDiscountButton(false)
                }
                is ResultState.Error -> {
                    showSnackBar(it.errorString)
                    onDiscountResult(View.GONE, true)
                    enableDiscountButton(true)
                }
            }
        })
    }

    private fun observeToPriceAfterDiscount() {
        viewModel.totalAfterDiscount.observe(viewLifecycleOwner, Observer {
            binding.textPriceAfterDiscount.text = it.toString().plus(" EGP")
        })
    }

    private fun onDiscountResult(visibility: Int, isEnabled: Boolean) {
        binding.progress.visibility = visibility
        binding.btnAddCard.isEnabled = isEnabled
    }

    private fun enableDiscountButton(isEnabled: Boolean) {
        binding.btnDiscount.isEnabled = isEnabled
        binding.etCoupon.isEnabled = isEnabled
    }

    private fun clearDiscount() {
        enableDiscountButton(true)
        binding.etCoupon.setText("")
    }


    override fun onStop() {
        super.onStop()
        viewModel.resetIsFlow()
        viewModel.updateToDB()
        viewModel.reloadStates()
    }

    private fun showShimmer() {
        binding.scrollBottom.visibility = View.GONE
        binding.shimmer.root.apply {
            visibility = View.VISIBLE
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.shimmer.root.apply {
            stopShimmer()
            visibility = View.GONE
        }
        binding.scrollBottom.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _cartProductAdapter = null
    }
}
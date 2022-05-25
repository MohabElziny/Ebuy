package com.iti.android.team1.ebuy.ui.productsScreen.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.FragmentProductsBinding
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products
import com.iti.android.team1.ebuy.ui.productsScreen.ProductsRecyclerAdapter
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductViewModelFactory
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductsViewModel

private const val TAG = "ProductsFragment"

class ProductsFragment : Fragment() {

    private lateinit var binding: FragmentProductsBinding
    private val args: ProductsFragmentArgs by navArgs()
    private val viewModel by viewModels<ProductsViewModel> {
        ProductViewModelFactory(Repository())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductsById(args.brandId)
        viewModel.productsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Loading -> {
                    binding.shimmer.root.showShimmer(true)
                    binding.shimmer.root.startShimmer()
                }
                is ResultState.Success -> {
                    binding.shimmer.root.hideShimmer()
                    binding.shimmer.root.stopShimmer()
                    binding.shimmer.root.visibility = View.GONE
                    binding.productsRecycler.visibility = View.VISIBLE
                    setUpProductRecycler(it.data)
                }
                is ResultState.EmptyResult -> {
                    //TODO: Show Empty layout
                    Log.d(TAG, "onViewCreated: EmptyResult")
                }
                is ResultState.Error -> {
                    //TODO: Show Error layout
                    Log.d(TAG, "onViewCreated: Error")

                }
            }
        }
    }

    private var onItemClick: () -> Unit = {
        //TODO: navigate to details_screen
    }

    private var onLike: (Product) -> Unit = { product ->
        //TODO: add product to database
    }

    private var onUnLike: (Product) -> Unit = { product ->
        //TODO: remote product from database
    }

    private fun setUpProductRecycler(products: Products) {
        val adapter = ProductsRecyclerAdapter(
            onItemClick = onItemClick,
            onLike = onLike,
            onUnLike = onUnLike
        )
        adapter.setProducts(products)
        binding.productsRecycler.layoutManager =
            GridLayoutManager(
                requireContext(),
                2,
                RecyclerView.VERTICAL,
                false
            )
        binding.productsRecycler.adapter = adapter
    }
}
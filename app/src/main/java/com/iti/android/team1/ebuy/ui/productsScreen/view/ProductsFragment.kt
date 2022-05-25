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
                    binding.progressBar.visibility = View.VISIBLE
                }
                is ResultState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val adapter = ProductsRecyclerAdapter(
                        onItemClick = onItemClick,
                        onLike = onLike,
                        onUnLike = onUnLike
                    )
                    adapter.setProducts(it.data)
                    binding.productsRecycler.layoutManager =
                        GridLayoutManager(
                            requireContext(),
                            2,
                            RecyclerView.VERTICAL,
                            false
                        )
                    binding.productsRecycler.adapter = adapter
                }
                is ResultState.EmptyResult -> {
                    Log.d(TAG, "onViewCreated: EmptyResult")
                }
                is ResultState.Error -> {
                    Log.d(TAG, "onViewCreated: Error")

                }
            }
        }
    }

    private var onItemClick: () -> Unit = {

    }

    private var onLike: (Product) -> Unit = { product ->
        //TODO: add product to database
    }

    private var onUnLike: (Product) -> Unit = { product ->
        //TODO: remote product from database
    }
}
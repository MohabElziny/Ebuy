package com.iti.android.team1.ebuy.ui.product_details_screen.view

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.iti.android.team1.ebuy.databinding.FragmentProductsDetailsBinding
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.adapter.ProductPagerAdapter
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductDetailsVMFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductsDetailsViewModel
import com.iti.android.team1.ebuy.util.ZoomOutPageTransformer
import kotlinx.coroutines.flow.buffer

class ProductsDetailsFragment : Fragment() {
    private var _binding: FragmentProductsDetailsBinding? = null
    private lateinit var viewModel: ProductsDetailsViewModel
    private val binding get() = _binding!!
    private lateinit var adapter: ProductPagerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsDetailsBinding.inflate(inflater, container, false)
        var g  :Int?
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, ProductDetailsVMFactory(Repository())).get(
            ProductsDetailsViewModel::class.java)
        initProductPagerAdapter()
        // will come from other screen

        viewModel.getProductDetails(7782820643045)
        lifecycleScope.launchWhenStarted {
            viewModel.product.buffer().collect { resultState ->
                handleResultStates(resultState)
            }

        }
    }

    override fun onResume() {
        super.onResume()

    }

    private fun handleResultStates(resultState: ResultState<Product>) {
        when (resultState) {
            ResultState.EmptyResult -> {
                Log.i("TAG", "handleResultStates: No Product")
            }
            is ResultState.Error -> Log.i("TAG", "handleResultStates: Error")

            ResultState.Loading -> Log.i("TAG", "handleResultStates: Loading ")

            is ResultState.Success -> {
                bindChanges(resultState.data)
            }
        }

    }

    private fun bindChanges(data: Product) {
        adapter.setProductImagesAdapter(productImages = data.images ?: emptyList())
        binding.txtProductTitle.text = data.productName
        binding.txtProductDescription.text = data.productDescription
        binding.txtProductPrice.text =
            ("${(data.productVariants?.get(0)?.productVariantPrice ?: 0)}").plus("$")

    }



    private fun initProductPagerAdapter() {
        adapter = ProductPagerAdapter()
        binding.productImagesViewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.productImagesViewPager)
        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.productImagesViewPager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
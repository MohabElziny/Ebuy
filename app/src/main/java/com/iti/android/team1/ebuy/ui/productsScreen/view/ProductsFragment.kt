package com.iti.android.team1.ebuy.ui.productsScreen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.iti.android.team1.ebuy.databinding.FragmentProductsBinding
import com.iti.android.team1.ebuy.data.data.localsource.LocalSource
import com.iti.android.team1.ebuy.data.data.repository.Repository
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.Product
import com.iti.android.team1.ebuy.data.pojo.Products
import com.iti.android.team1.ebuy.ui.productsScreen.ProductsRecyclerAdapter
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductViewModelFactory
import com.iti.android.team1.ebuy.ui.productsScreen.viewmodel.ProductsViewModel
import com.iti.android.team1.ebuy.util.showSnackBar

class ProductsFragment : Fragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!
    private var _productAdapter: ProductsRecyclerAdapter? = null
    private val productAdapter get() = _productAdapter!!
    private val args: ProductsFragmentArgs by navArgs()
    private val viewModel by viewModels<ProductsViewModel> {
        ProductViewModelFactory(Repository(LocalSource(requireContext())))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getProductsById(args.brandId)
        viewModel.productsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is ResultState.Loading -> {
                    showShimmer()
                }
                is ResultState.Success -> {
                    removeShimmer()
                    binding.productsRecycler.visibility = View.VISIBLE
                    setUpProductRecycler(it.data)
                }
                is ResultState.EmptyResult -> {
                    showEmptyLayout()
                }
                is ResultState.Error -> {
                    showEmptyLayout()
                    showSnackBar(it.errorString)
                }
            }
        }

        startObserveToAddingFavoriteResult()
        startObserveToDeletingFavoriteResult()
    }

    private fun showEmptyLayout() {
        removeShimmer()
        binding.emptyLayout.root.visibility = View.VISIBLE
    }

    private fun startObserveToAddingFavoriteResult() {
        viewModel.resultOfAddingProductToFavorite.observe(viewLifecycleOwner) {
            when (it) {
                ResultState.EmptyResult -> {}
                is ResultState.Error -> showSnackBar(it.errorString)
                ResultState.Loading -> {}
                is ResultState.Success -> {}
            }
        }
    }

    private fun startObserveToDeletingFavoriteResult() {
        viewModel.resultOfDeletingProductToFavorite.observe(viewLifecycleOwner) {
            when (it) {
                ResultState.EmptyResult -> {}
                is ResultState.Error -> showSnackBar(it.errorString)
                ResultState.Loading -> {}
                is ResultState.Success -> {}
            }
        }
    }


    private var onItemClick: (Long) -> Unit = { productId ->
        findNavController().navigate(
            ProductsFragmentDirections.actionNavigationProductsToProductsDetailsFragment(productId)
        )
    }

    private var onLike: (Product) -> Unit = { product ->
        viewModel.addProductToFavorite(product)
    }

    private var onUnLike: (productId: Long) -> Unit = { productId ->
        viewModel.removeProductFromFavorite(productId)
    }

    private fun setUpProductRecycler(products: Products) {
        _productAdapter = ProductsRecyclerAdapter(
            onItemClick = onItemClick,
            onLike = onLike,
            onUnLike = onUnLike
        ).apply {
            setProducts(products)
        }
        binding.productsRecycler.apply {
            layoutManager =
                GridLayoutManager(
                    requireContext(),
                    2,
                    RecyclerView.VERTICAL,
                    false
                )
            adapter = productAdapter
        }
    }

    private fun showShimmer() {
        binding.shimmer.apply {
            root.showShimmer(true)
            root.startShimmer()
        }
    }

    private fun removeShimmer() {
        binding.shimmer.root.apply {
            hideShimmer()
            stopShimmer()
            visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _productAdapter = null
        _binding = null
    }
}
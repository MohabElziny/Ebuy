package com.iti.android.team1.ebuy.ui.product_details_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.activities.main.view.MainActivity
import com.iti.android.team1.ebuy.databinding.FragmentProductsDetailsBinding
import com.iti.android.team1.ebuy.model.data.localsource.LocalSource
import com.iti.android.team1.ebuy.model.data.repository.Repository
import com.iti.android.team1.ebuy.model.factories.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.adapter.ProductPagerAdapter
import com.iti.android.team1.ebuy.ui.product_details_screen.dialog.AddToCartDialog
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductDetailsVMFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductsDetailsViewModel
import com.iti.android.team1.ebuy.util.ZoomOutPageTransformer
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class ProductsDetailsFragment : Fragment() {
    private var _binding: FragmentProductsDetailsBinding? = null
    private val viewModel: ProductsDetailsViewModel by viewModels {
        ProductDetailsVMFactory(Repository(LocalSource(requireContext())))
    }
    private val binding get() = _binding!!
    private var _adapter: ProductPagerAdapter? = null
    private val adapter get() = _adapter!!
    private var cartProduct: Product? = null
    val args: ProductsDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentProductsDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initProductPagerAdapter()
        // will come from other screen
        viewModel.getProductDetails(args.product)
        lifecycleScope.launchWhenStarted {
            viewModel.product.buffer().collect { resultState ->
                handleResultStates(resultState)
            }
        }
        handleProductResultCart()
        bindAddToCartButton()
        observeOnFavoriteProgress()
    }

    private fun observeOnFavoriteProgress() {
        lifecycleScope.launchWhenStarted {
            viewModel.favoriteProgress.buffer().collect {
                when (it) {
                    ResultState.EmptyResult -> {}
                    is ResultState.Error -> {
                        Toast.makeText(requireContext(),
                            it.errorString, Toast.LENGTH_LONG).show()
                        binding.likeBtn.isLiked = !binding.likeBtn.isLiked
                    }
                    ResultState.Loading -> {}
                    is ResultState.Success -> {}
                }
            }
        }
    }

    private fun initLikeBtn(product: Product) {
        viewModel.getProductState(productId = args.product)
        fetchProductState()
        binding.likeBtn.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                viewModel.insertProductToFavorites(product)
            }

            override fun unLiked(likeButton: LikeButton?) {
                viewModel.deleteProductFromFavorites(args.product)
            }
        })
    }

    private fun fetchProductState() {
        lifecycleScope.launchWhenStarted {
            viewModel.productState.buffer().collect {
                binding.likeBtn.isLiked = it
            }
        }
    }

    private fun handleResultStates(resultState: ResultState<Product>) {
        when (resultState) {
            ResultState.Loading -> {
                showShimmer()
            }
            ResultState.EmptyResult -> {
                hideShimmer()
            }
            is ResultState.Error -> {
                hideShimmer()
            }
            is ResultState.Success -> {
                hideShimmer()
                bindChanges(resultState.data)
                initLikeBtn(resultState.data)
                cartProduct = resultState.data
            }
        }
    }

    private fun bindAddToCartButton() {
        binding.btnAddToCart.setOnClickListener {
            cartProduct?.let { product -> viewModel.getProductInCartState(product.productID ?: 0) }
        }
    }

    private fun handleProductResultCart() {
        lifecycleScope.launch {
            viewModel.productCartState.observe(viewLifecycleOwner) { isInCart ->
                val quantity =
                    cartProduct?.productVariants?.get(0)?.productVariantInventoryQuantity ?: 0
                isInCart?.let {
                    if (it) {
                        showAlertDialog(cartProduct?.productName ?: "")
                    } else if (quantity <= 0) {
                        Toast.makeText(requireContext(),
                            getString(R.string.product_out_of_stock),
                            Toast.LENGTH_LONG)
                            .show()
                    } else {
                        showAddToCartDialog()
                    }
                }
            }
        }
    }

    private fun showAddToCartDialog() {
        cartProduct?.let { product ->
            AddToCartDialog(product).show(requireActivity().supportFragmentManager,
                getString(R.string.AddToCartDialogTag))
        }
    }

    private fun showAlertDialog(title: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setMessage(getString(R.string.alert_dialogue_message))
            .setNeutralButton(getString(R.string.cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.go_to_cart)) { dialog, _ ->
                dialog.dismiss()
                (activity as MainActivity).profileNavigation()
                findNavController().navigate(
                    ProductsDetailsFragmentDirections.actionProductsDetailsFragmentToCartFragment()
                )
            }
            .show()
    }

    private fun showShimmer() {
        binding.constrainContent.visibility = View.GONE
        binding.productDetailsShimmer.root.apply {
            visibility = View.VISIBLE
            showShimmer(true)
            startShimmer()
        }
    }

    private fun hideShimmer() {
        binding.productDetailsShimmer.root.apply {
            stopShimmer()
            showShimmer(false)
            visibility = View.GONE
        }
        binding.constrainContent.visibility = View.VISIBLE
    }

    private fun bindChanges(data: Product) {
        adapter.setProductImagesAdapter(productImages = data.images ?: emptyList())
        binding.txtProductTitle.text = data.productName
        binding.txtProductDescription.text = data.productDescription
        binding.txtProductPrice.text =
            ("${(data.productVariants?.get(0)?.productVariantPrice ?: 0)}").plus("  EGP")
        binding.txtProductSize.text =
            data.productVariants?.get(0)?.productVariantOption1 ?: getString(
                R.string.messing_details)
    }

    private fun initProductPagerAdapter() {
        _adapter = ProductPagerAdapter()
        binding.productImagesViewPager.adapter = adapter
        binding.dotsIndicator.attachTo(binding.productImagesViewPager)
        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.productImagesViewPager.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        cartProduct = null
        _adapter = null
        _binding = null
    }

    override fun onStop() {
        super.onStop()
        (activity as MainActivity).setDefault()
    }

}
package com.iti.android.team1.ebuy.ui.product_details_screen.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentProductsDetailsBinding
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.ui.product_details_screen.adapter.ProductPagerAdapter
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductDetailsVMFactory
import com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel.ProductsDetailsViewModel
import com.iti.android.team1.ebuy.util.ZoomOutPageTransformer
import com.like.LikeButton
import com.like.OnLikeListener
import kotlinx.coroutines.flow.buffer

class ProductsDetailsFragment : Fragment() {
    private var _binding: FragmentProductsDetailsBinding? = null
    private val viewModel: ProductsDetailsViewModel by viewModels {
        ProductDetailsVMFactory(Repository(LocalSource(requireContext())))
    }
    private val binding get() = _binding!!
    private lateinit var adapter: ProductPagerAdapter

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

    }

    private fun initLikeBtn(product: Product) {
        viewModel.getProductState(productId = args.product)
        fetchProductState()
        binding.likeBtn.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                product.apply {
                    val favoriteProduct =
                        FavoriteProduct(productID = productID!!,
                            productName = productName!!,
                            productImageUrl = productImage!!.imageURL!!,
                            productPrice = productVariants!![0].productVariantPrice!!.toDouble())
                    viewModel.insertProductToFavorites(favoriteProduct)
                    insertEffect()
                }
            }

            override fun unLiked(likeButton: LikeButton?) {
                viewModel.deleteProductFromFavorites(args.product)
                deleteEffect()
            }
        })
    }

    private fun insertEffect() {
        lifecycleScope.launchWhenStarted {
            viewModel.dpInsertProgress.buffer().collect() {
                when (it) {
                    is DatabaseResult.Success -> Toast.makeText(requireContext(),
                        getString(R.string.insert_seccuess),
                        Toast.LENGTH_SHORT).show()
                    is DatabaseResult.Error -> Toast.makeText(requireContext(),
                        getString(R.string.insert_error),
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun deleteEffect() {
        lifecycleScope.launchWhenStarted {
            viewModel.dpDeleteProgress.buffer().collect() {
                when (it) {
                    is DatabaseResult.Success -> Toast.makeText(requireContext(),
                        getString(R.string.delete_success),
                        Toast.LENGTH_SHORT).show()
                    is DatabaseResult.Error -> Toast.makeText(requireContext(),
                        getString(R.string.delete_error),
                        Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun fetchProductState() {
        lifecycleScope.launchWhenStarted {
            viewModel.productState.buffer().collect() {
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
            }
        }

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
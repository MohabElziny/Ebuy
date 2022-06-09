package com.iti.android.team1.ebuy.ui.category.view

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.iti.android.team1.ebuy.R
import com.iti.android.team1.ebuy.databinding.FragmentCategoryBinding
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.localsource.LocalSource
import com.iti.android.team1.ebuy.model.datasource.repository.Repository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products
import com.iti.android.team1.ebuy.ui.category.viewmodel.CategoryViewModel
import com.iti.android.team1.ebuy.ui.category.viewmodel.CategoryViewModelFactory
import kotlinx.coroutines.flow.buffer

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null

    private var defaultCategoryId: Long = 0

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(Repository(LocalSource(requireContext())))
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var categoryProductsAdapter: CategoryProductsAdapter
    private lateinit var searchView: SearchView
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initCategoriesRecyclerView()
        initRecyclerView()

        categoryViewModel.getAllCategories()
        categoryViewModel.getAllProduct()

        lifecycleScope.launchWhenStarted {
            categoryViewModel.allCategories.buffer().collect {
                handleCategoriesResult(it)
            }
        }

        lifecycleScope.launchWhenStarted {
            categoryViewModel.allProducts.buffer().collect {
                handleProductsResult(it)
            }
        }

        binding.fabAccessories.setOnClickListener {
            onFabClickListener("ACCESSORIES")
        }
        binding.fabShoes.setOnClickListener {
            onFabClickListener("SHOES")
        }
        binding.fabTShirt.setOnClickListener {
            onFabClickListener("T-SHIRTS")
        }
        initFavoriteProductsStates()
    }

    private fun initFavoriteProductsStates() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.insertFavoriteProductToDataBase.buffer().collect { response ->
                when (response) {
                    is ResultState.Error -> {
                        Toast.makeText(context, response.errorString, Toast.LENGTH_SHORT).show()
                    }
                    ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Toast.makeText(context,
                            "Successfully Successfully added to Favorites",
                            Toast.LENGTH_SHORT).show()
                    }
                    ResultState.EmptyResult -> {}
                }
            }
            categoryViewModel.deleteFavoriteProductToDataBase.buffer().collect { response ->
                when (response) {
                    is ResultState.Error -> {
                        Toast.makeText(context, response.errorString, Toast.LENGTH_SHORT).show()
                    }
                    ResultState.Loading -> {}
                    is ResultState.Success -> {
                        Toast.makeText(context,
                            "Successfully removed from Favorites",
                            Toast.LENGTH_SHORT).show()
                    }
                    ResultState.EmptyResult -> {}
                }
            }
        }
    }

    private fun onFabClickListener(productType: String) {
        categoryViewModel.getAllProductByType(defaultCategoryId, productType)
    }

    private fun handleCategoriesResult(result: ResultState<Categories>) {
        when (result) {
            is ResultState.Loading -> {}
            is ResultState.Success -> {
                result.data.categoriesList.let {
                    categoriesAdapter.setList(it)
                    binding.catTvName.text = it[0].categoryTitle
                }
            }
            is ResultState.EmptyResult -> {}
            is ResultState.Error -> {}
        }
    }

    private fun handleProductsResult(result: ResultState<Products>) {
        when (result) {
            is ResultState.Loading -> {
                startShimmer()
            }
            is ResultState.Success -> {
                stopShimmer()
                binding.emptyLayout.root.visibility = View.GONE
                binding.productRecycler.visibility = View.VISIBLE
                result.data.products?.let { categoryProductsAdapter.setList(it) }
            }
            is ResultState.EmptyResult -> {
                stopShimmer()
                binding.emptyLayout.root.visibility = View.VISIBLE
                binding.productRecycler.visibility = View.GONE
                binding.emptyLayout.txt.text = getString(R.string.empty_products)
            }
            is ResultState.Error -> {}
        }
    }

    private fun initCategoriesRecyclerView() {

        categoriesAdapter = CategoriesAdapter(onCategoryBtnClick)
        binding.categoryRecycler.apply {
            this.adapter = categoriesAdapter
            this.layoutManager =
                LinearLayoutManager(context).apply { this.orientation = RecyclerView.HORIZONTAL }
            this.setHasFixedSize(true)
        }
    }

    private var onCategoryBtnClick = fun(id: Long, title: String) {
        searchView.onActionViewCollapsed()
        binding.catTvName.text = title
        defaultCategoryId = id
        categoryViewModel.getAllProduct(id)
    }

    private fun initRecyclerView() {
        categoryProductsAdapter = CategoryProductsAdapter(
            onClickLike,
            onClickUnLike,
            onProductClick
        )
        binding.productRecycler.apply {
            this.adapter = categoryProductsAdapter
            this.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            this.setHasFixedSize(true)
        }
    }

    private fun startShimmer() {
        binding.productRecycler.visibility = View.GONE
        binding.shimmer.apply {
            this.visibility = View.VISIBLE
            this.startShimmer()
        }
    }

    private fun stopShimmer() {
        binding.shimmer.apply {
            this.stopShimmer()
            this.visibility = View.GONE
        }
        binding.productRecycler.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.category_menu, menu)
        searchView = menu.findItem(R.id.cat_menu_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.searching_products)
        onQueryTextListener()
        onCloseSearch()
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun onQueryTextListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    categoryViewModel.setSearchQuery(it)
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    categoryViewModel.setSearchQuery(query)
                }
                return false
            }
        })
    }

    private fun onCloseSearch() {
        searchView.setOnCloseListener {
            categoryViewModel.getProductsAgain()
            return@setOnCloseListener false
        }
    }

    private val onClickLike: (product: Product) -> Unit = {
        categoryViewModel.addProductToFavorite(it)
    }

    private val onClickUnLike: (productID: Long) -> Unit = {
        categoryViewModel.removeFavoriteProduct(it)
    }

    private val onProductClick: (productID: Long) -> Unit = {
        findNavController().navigate(
            CategoryFragmentDirections.actionNavigationCategoryToProductsDetailsFragment(it)
        )
    }
}
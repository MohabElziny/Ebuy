package com.iti.android.team1.ebuy.ui.category.view

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
import com.iti.android.team1.ebuy.data.data.localsource.LocalSource
import com.iti.android.team1.ebuy.data.data.repository.Repository
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.Categories
import com.iti.android.team1.ebuy.data.pojo.Product
import com.iti.android.team1.ebuy.data.pojo.Products
import com.iti.android.team1.ebuy.ui.category.viewmodel.CategoryViewModel
import com.iti.android.team1.ebuy.ui.category.viewmodel.CategoryViewModelFactory
import com.iti.android.team1.ebuy.util.showSnackBar
import kotlinx.coroutines.flow.buffer

class CategoryFragment : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private var defaultCategoryId: Long = 0

    var firstTime = true

    private val categoryViewModel: CategoryViewModel by viewModels {
        CategoryViewModelFactory(Repository(LocalSource(requireContext())))
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private var _categoriesAdapter: CategoriesAdapter? = null
    private val categoriesAdapter get() = _categoriesAdapter!!
    private var _categoryProductsAdapter: CategoryProductsAdapter? = null
    private val categoryProductsAdapter get() = _categoryProductsAdapter!!
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
        initSpinner()
    }

    private fun initFavoriteProductsStates() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.insertFavoriteProductToDataBase.buffer().collect { response ->
                when (response) {
                    is ResultState.Error -> showSnackBar(response.errorString)
                    ResultState.Loading -> {}
                    is ResultState.Success -> {}
                    ResultState.EmptyResult -> {}
                }
            }
            categoryViewModel.deleteFavoriteProductToDataBase.buffer().collect { response ->
                when (response) {
                    is ResultState.Error -> showSnackBar(response.errorString)
                    ResultState.Loading -> {}
                    is ResultState.Success -> {}
                    ResultState.EmptyResult -> {}
                }
            }
        }
    }

    private fun onFabClickListener(productType: String) {
        categoryViewModel.getAllProductByType(defaultCategoryId, productType)
        binding.fabMenuButton.collapse()
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
            is ResultState.EmptyResult -> {
                binding.emptyLayout.root.visibility = View.VISIBLE
            }
            is ResultState.Error -> {}
        }
    }

    private fun handleProductsResult(result: ResultState<Products>) {
        when (result) {
            is ResultState.Loading -> {
                startShimmer()
                binding.emptyProductLayout.root.visibility = View.GONE
            }
            is ResultState.Success -> {
                stopShimmer()
                binding.emptyLayout.root.visibility = View.GONE
                binding.productRecycler.visibility = View.VISIBLE
                result.data.products?.let { categoryProductsAdapter.setList(it) }
                binding.emptyProductLayout.root.visibility = View.GONE
            }
            is ResultState.EmptyResult -> {
                stopShimmer()
                binding.emptyProductLayout.root.visibility = View.VISIBLE
                binding.productRecycler.visibility = View.GONE
                binding.emptyProductLayout.txt.text = getString(R.string.empty_products)
            }
            is ResultState.Error -> {}
        }
    }

    private fun initCategoriesRecyclerView() {
        _categoriesAdapter = CategoriesAdapter(onCategoryBtnClick)
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
        if (binding.spinner.selectedItemPosition != 0) {
            firstTime = true
            binding.spinner.setSelection(0, true)
        }
    }

    private fun initRecyclerView() {
        _categoryProductsAdapter = CategoryProductsAdapter(
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
        binding.shimmer1.root.apply {
            visibility = View.VISIBLE
            this.startShimmer()
        }
    }

    private fun stopShimmer() {
        binding.shimmer1.root.apply {
            this.stopShimmer()
            this.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        categoryViewModel.onDestroyView()
        _binding = null
        _categoryProductsAdapter = null
        _categoriesAdapter = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.category_menu, menu)
        searchView = menu.findItem(R.id.cat_menu_search)?.actionView as SearchView
        searchView.queryHint = getString(R.string.searching_products)
        onQueryTextListener()
        onCloseSearch()
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_favorite -> findNavController().navigate(
                CategoryFragmentDirections.actionNavigationCategoryToNavigationFavorites())
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onQueryTextListener() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    categoryViewModel.setSearchQuery(it.trim())
                }
                return true
            }

            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    categoryViewModel.setSearchQuery(query.trim())
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

    private fun initSpinner() {
        binding.spinner.apply {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1,
                resources.getStringArray(R.array.sortProducts))
        }

        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (!firstTime)
                    categoryViewModel.sortProducts(p2)
                else
                    firstTime = false
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }
        }
    }

    override fun onStart() {
        super.onStart()
        binding.spinner.setSelection(0, true)
    }
}
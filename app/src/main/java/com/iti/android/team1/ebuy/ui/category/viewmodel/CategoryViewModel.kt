package com.iti.android.team1.ebuy.ui.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.domain.category.CategoryProductsUseCase
import com.iti.android.team1.ebuy.domain.category.ICategoryProductsUseCase
import com.iti.android.team1.ebuy.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.Categories
import com.iti.android.team1.ebuy.data.pojo.DraftOrder
import com.iti.android.team1.ebuy.data.pojo.Product
import com.iti.android.team1.ebuy.data.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(private var myRepo: IRepository) : ViewModel() {
    private var _allProducts = MutableStateFlow<ResultState<Products>>(ResultState.Loading)
    val allProducts get() = _allProducts.asStateFlow()
    private var _allCategories = MutableStateFlow<ResultState<Categories>>(ResultState.Loading)
    val allCategories get() = _allCategories.asStateFlow()

    private var _insertFavoriteProductToDataBase =
        MutableStateFlow<ResultState<DraftOrder>>(ResultState.Loading)
    val insertFavoriteProductToDataBase
        get() = _insertFavoriteProductToDataBase.asStateFlow()

    private var _deleteFavoriteProductToDataBase =
        MutableStateFlow<ResultState<DraftOrder>>(ResultState.Loading)
    val deleteFavoriteProductToDataBase
        get() = _deleteFavoriteProductToDataBase.asStateFlow()

    private val categoryProductsUseCase: ICategoryProductsUseCase
        get() = CategoryProductsUseCase(myRepo)

    private var cachedProducts: List<Product>? = null

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                categoryProductsUseCase.getAllCategories()
            }
            sendCategoriesResponse(result.await())
        }
    }

    fun getAllProduct(category: Long = 0) {
        _allProducts.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                if (category == 0L) categoryProductsUseCase.getAllProducts()
                else categoryProductsUseCase.getProductsByCollectionID(category)
            }
            sendProductsByTypeResponse(result.await())
        }
    }

    private suspend fun sendCategoriesResponse(result: NetworkResponse<Categories>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _allCategories.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.categoriesList.isEmpty()) {
                    _allCategories.emit(ResultState.EmptyResult)
                } else {
                    _allCategories.emit(ResultState.Success(result.data))
                }
            }
        }
    }

    fun getAllProductByType(categoryId: Long, productType: String) {
        _allProducts.value = ResultState.Loading
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                if (categoryId == 0L)
                    myRepo.getAllProductsByType(productType)
                else
                    categoryProductsUseCase.getAllCategoryProductsByType(categoryId, productType)
            }
            sendProductsByTypeResponse(result.await())
        }
    }

    private suspend fun sendProductsByTypeResponse(result: NetworkResponse<Products>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _allProducts.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.products.isNullOrEmpty()) {
                    _allProducts.emit(ResultState.EmptyResult)
                } else {
                    cachedProducts = result.data.products
                    sortProducts(0)
                }
            }
        }
    }

    fun addProductToFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.addFavorite(product)
            }
            addFavoriteResponse(result.await())
        }
    }

    private fun addFavoriteResponse(response: NetworkResponse<DraftOrder>) {
        when (response) {
            is NetworkResponse.FailureResponse -> _insertFavoriteProductToDataBase.value =
                ResultState.Error(response.errorString)
            is NetworkResponse.SuccessResponse -> {
                _insertFavoriteProductToDataBase.value = ResultState.Success(response.data)
            }
        }
    }

    fun removeFavoriteProduct(productID: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.removeFromFavorite(productID)
            }
            removeFavoriteResponse(result.await())
        }
    }

    private fun removeFavoriteResponse(response: NetworkResponse<DraftOrder>) {
        when (response) {
            is NetworkResponse.FailureResponse -> _deleteFavoriteProductToDataBase.value =
                ResultState.Error(response.errorString)
            is NetworkResponse.SuccessResponse -> {
                _deleteFavoriteProductToDataBase.value = ResultState.Success(response.data)
            }
        }
    }

    fun setSearchQuery(newString: String) {
        if (newString.isNotEmpty()) {
            _allProducts.value = ResultState.Loading
            val searchList: List<Product> = cachedProducts ?: emptyList()
            viewModelScope.launch(Dispatchers.Default) {
                if (searchList.isNotEmpty()) {
                    searchList.filter { product ->
                        filterProduct(product, newString)
                    }.apply {
                        if (this.isNotEmpty())
                            _allProducts.emit(ResultState.Success(Products(this)))
                        else
                            _allProducts.emit(ResultState.EmptyResult)
                    }
                }
            }
        }
    }

    private fun filterProduct(product: Product, newString: String): Boolean {
        return product.productName?.lowercase()?.contains(newString.lowercase()) == true ||
                product.productVendor?.lowercase()?.contains(newString.lowercase()) == true
    }

    fun getProductsAgain() {
        cachedProducts?.let {
            _allProducts.value = ResultState.Success(Products(it))
        }
    }

    private fun sortProductList(sortType: SortType) {
        val sortArray: List<Product> = cachedProducts ?: emptyList()
        _allProducts.value = ResultState.Loading
        when (sortType) {
            SortType.A_to_Z -> sortArray.sortedBy { it.productName }
            SortType.Z_to_A -> sortArray.sortedByDescending { it.productName }
            SortType.Lowest_to_highest_price -> sortArray.sortedBy {
                it.productVariants?.get(0)?.productVariantPrice?.toDouble()
            }
            SortType.Highest_to_lowest_price -> sortArray.sortedByDescending {
                it.productVariants?.get(0)?.productVariantPrice?.toDouble()
            }
        }.apply { _allProducts.value = ResultState.Success(Products(this)) }
    }

    fun sortProducts(position: Int) {
        when (position) {
            0 -> sortProductList(SortType.A_to_Z)
            1 -> sortProductList(SortType.Z_to_A)
            2 -> sortProductList(SortType.Lowest_to_highest_price)
            3 -> sortProductList(SortType.Highest_to_lowest_price)
        }
    }

    fun onDestroyView() {
        cachedProducts = null
    }

}

enum class SortType {
    A_to_Z, Z_to_A, Lowest_to_highest_price, Highest_to_lowest_price
}


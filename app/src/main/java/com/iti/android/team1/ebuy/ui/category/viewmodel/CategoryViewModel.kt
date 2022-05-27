package com.iti.android.team1.ebuy.ui.category.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(var myRepo: IRepository) : ViewModel() {
    private var _allProducts = MutableStateFlow<ResultState<Products>>(ResultState.Loading)
    val allProducts get() = _allProducts.asStateFlow()
    private var _allCategories = MutableStateFlow<ResultState<Categories>>(ResultState.Loading)
    val allCategories get() = _allCategories.asStateFlow()

    fun getAllCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.getAllCategories()
            }
            sendCategoriesResponse(result.await())
        }
    }

    fun getAllProduct(category: Long = 0) {
        viewModelScope.launch(Dispatchers.IO) {
            _allProducts.emit(ResultState.Loading)
            val result = async {
                if (category == 0L) myRepo.getAllProducts()
                else myRepo.getProductsByCollectionID(category)
            }
            sendProductsByTypeResponse(result.await())
        }
    }

    private suspend fun sendCategoriesResponse(result: NetworkResponse<Categories>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _allCategories.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.categoriesList.isNullOrEmpty()) {
                    _allCategories.emit(ResultState.EmptyResult)
                } else{
                    _allCategories.emit(ResultState.Success(result.data))
                }
            }
        }

    }

    //home is default category
    fun getAllProductByType(categoryId: Long=395727569125, productType:String = "SHOES") {
        viewModelScope.launch(Dispatchers.IO) {
            _allProducts.emit(ResultState.Loading)
            val result = async {
                 myRepo.getAllCategoryProducts(categoryId,productType)
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
                } else
                    _allProducts.emit(ResultState.Success(result.data))
            }
        }
    }
}


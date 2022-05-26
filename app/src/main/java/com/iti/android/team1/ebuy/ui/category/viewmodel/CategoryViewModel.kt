package com.iti.android.team1.ebuy.ui.category.viewmodel

import android.util.Log
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
import kotlinx.coroutines.flow.StateFlow
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

    private suspend fun sendCategoriesResponse(result: NetworkResponse<Categories>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _allCategories.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                Log.i("TAG", "sendCategoriesResponse: success ")
                if (result.data.list.isNullOrEmpty()) {
                    _allCategories.emit(ResultState.EmptyResult)
                    Log.i("TAG", "sendCategoriesResponse: result ok")
                } else{
                    _allCategories.emit(ResultState.Success(result.data))
                    Log.i("TAG", "sendCategoriesResponse: result ok")
                }
            }
        }

    }


    fun getAllProduct(category: Long,productType:String) {
        viewModelScope.launch(Dispatchers.IO) {
            _allProducts.emit(ResultState.Loading)
            val result = async {
                 myRepo.getAllCategoryProducts(category,productType)
            }
            sendProductsResponse(result.await())
        }
    }

    private suspend fun sendProductsResponse(result: NetworkResponse<Products>) {
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


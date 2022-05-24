package com.iti.android.team1.ebuy.ui.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(var myRepo: IRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private var _allProducts = MutableLiveData<ResultState<Products>>()
    val allProducts: LiveData<ResultState<Products>> get() = _allProducts

    fun getAllProduct(category: Long = 0) {
        _allProducts.postValue(ResultState.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            val result =
                if (category == 0L) myRepo.getAllProducts() else myRepo.getProductsByCollectionID(
                    category
                )
            when (result) {
                is NetworkResponse.FailureResponse ->
                    _allProducts.postValue(ResultState.Error(result.errorString))
                is NetworkResponse.SuccessResponse -> {
                    if (result.data.products.isNullOrEmpty()) {
                        _allProducts.postValue(ResultState.EmptyResult)
                    } else
                        _allProducts.postValue(ResultState.Success(result.data))
                }

            }
        }
    }
}


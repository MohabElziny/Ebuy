package com.iti.android.team1.ebuy.ui.category.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.launch

class CategoryViewModel(var myRepo: IRepository) : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text
    private var _allProducts = MutableLiveData<ProductResponse>()
    val allProducts: LiveData<ProductResponse> get() = _allProducts

    fun getAllProduct(category: Long = 0) {
        _allProducts.postValue(ProductResponse.Loading)
        viewModelScope.launch {
            val result =
                if (category == 0L) myRepo.getAllProducts() else myRepo.getProductsByCollectionID(
                    category
                )
            when (result) {
                is NetworkResponse.FailureResponse -> _allProducts.postValue(ProductResponse.Error)
                is NetworkResponse.SuccessResponse -> _allProducts.postValue(ProductResponse.Success(result.data?:))
            }
        }
    }
}

sealed class ProductResponse {
    object Loading : ProductResponse()
    object Error : ProductResponse()
    data class Success(val data: Products) : ProductResponse()
}
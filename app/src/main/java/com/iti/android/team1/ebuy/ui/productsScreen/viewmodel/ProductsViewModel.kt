package com.iti.android.team1.ebuy.ui.productsScreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "ProductsViewModel"

class ProductsViewModel(private val repoInterface: IRepository) : ViewModel() {

    private val _productsMutableLivaData: MutableLiveData<ResultState<Products>> = MutableLiveData()

    val productsLiveData: LiveData<ResultState<Products>>
        get() = _productsMutableLivaData

    fun getProductsById(id: Long) {
        viewModelScope.launch {
            val res = async {
                repoInterface.getProductsByCollectionID(id)
            }
            fetchResult(res.await())
        }
    }

    private fun fetchResult(networkResponse: NetworkResponse<Products>) {
        _productsMutableLivaData.value = ResultState.Loading
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (it.products != null && it.products.isNotEmpty()) {
                        _productsMutableLivaData.value = ResultState.Success(it)
                    } else {
                        _productsMutableLivaData.value = ResultState.EmptyResult
                    }
                }
            }
            is NetworkResponse.FailureResponse -> {
                _productsMutableLivaData.value = ResultState.Error(networkResponse.errorString)
            }
        }
    }
}
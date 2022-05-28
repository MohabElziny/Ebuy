package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import com.iti.android.team1.ebuy.model.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsDetailsViewModel(private val myRepo:IRepository) : ViewModel() {
    private var _product :MutableStateFlow<ResultState<Product>> =MutableStateFlow(ResultState.Loading)
    val product get() =_product.asStateFlow()

    fun getProductDetails(productId: Long = 0L) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.emit(ResultState.Loading)
            val result = async {myRepo.getProductDetails(productId)}
            sendResponseBack(result.await())
        }
    }

    private suspend fun sendResponseBack(result: NetworkResponse<Product>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _product.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.images.isNullOrEmpty()) {
                    _product.emit(ResultState.EmptyResult)
                } else
                    _product.emit(ResultState.Success(result.data))
            }
        }
    }
}
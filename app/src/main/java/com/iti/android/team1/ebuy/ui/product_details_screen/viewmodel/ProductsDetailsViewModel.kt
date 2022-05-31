package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsDetailsViewModel(private val myRepo: IRepository) : ViewModel() {

    private var _product: MutableStateFlow<ResultState<Product>> =
        MutableStateFlow(ResultState.Loading)
    val product get() = _product.asStateFlow()

    private var _dbDeleteProgress: MutableStateFlow<DatabaseResult<Int?>> =
        MutableStateFlow(DatabaseResult.Loading)
    val dpDeleteProgress get() = _dbDeleteProgress.asStateFlow()

    private var _dbInsertProgress: MutableStateFlow<DatabaseResult<Long?>> =
        MutableStateFlow(DatabaseResult.Loading)
    val dpInsertProgress get() = _dbInsertProgress.asStateFlow()

    private var _productState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val productState get() = _productState.asStateFlow()

    fun getProductDetails(productId: Long = 0L) {
        viewModelScope.launch(Dispatchers.IO) {
            _product.emit(ResultState.Loading)
            val result = async { myRepo.getProductDetails(productId) }
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

    fun deleteProductFromFavorites(productId: Long) {
        viewModelScope.launch(Dispatchers.IO)  {
            val result = async { myRepo.deleteProductFromFavorite(productId) }
            _deleteProductFromFavorites(result.await())
        }
    }

    private suspend fun _deleteProductFromFavorites(result: DatabaseResponse<Int?>) {
        when (result) {
            is DatabaseResponse.Success -> _dbDeleteProgress.emit(DatabaseResult.Success(result.data))
            is DatabaseResponse.Failure -> _dbDeleteProgress.emit(DatabaseResult.Error(result.errorMsg))
        }
    }

    fun insertProductToFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO)  {
            val result = async { myRepo.addProductToFavorite(product) }
            _insertProductToFavorites(result.await())
        }
    }

    private suspend fun _insertProductToFavorites(result: DatabaseResponse<Long?>) {
        when (result) {
            is DatabaseResponse.Success -> _dbInsertProgress.emit(DatabaseResult.Success(result.data))
            is DatabaseResponse.Failure -> _dbInsertProgress.emit(DatabaseResult.Error(result.errorMsg))
        }
    }

    fun getProductState(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { myRepo.isFavoriteProduct(productId) }
            _productState.emit(result.await())
        }
    }

}
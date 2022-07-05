package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.domain.productdetails.IProductInCartUseCase
import com.iti.android.team1.ebuy.domain.productdetails.ProductInCartUseCase
import com.iti.android.team1.ebuy.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.DraftOrder
import com.iti.android.team1.ebuy.data.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProductsDetailsViewModel(private val myRepo: IRepository) : ViewModel() {

    private val _product: MutableStateFlow<ResultState<Product>> =
        MutableStateFlow(ResultState.Loading)
    val product get() = _product.asStateFlow()

    private val _favoriteProgress: MutableStateFlow<ResultState<Boolean>> =
        MutableStateFlow(ResultState.Loading)
    val favoriteProgress get() = _favoriteProgress.asStateFlow()

    private val _productState: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val productState get() = _productState.asStateFlow()

    private val _productCartState = MutableLiveData<Boolean>()
    val productCartState: LiveData<Boolean> get() = _productCartState

    private val productInCartUseCase: IProductInCartUseCase
        get() = ProductInCartUseCase(myRepo)

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
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { myRepo.removeFromFavorite(productId) }
            deleteProductFromFavoritesResponse(result.await())
        }
    }

    private suspend fun deleteProductFromFavoritesResponse(result: NetworkResponse<DraftOrder>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _favoriteProgress.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _favoriteProgress.emit(ResultState.Success(false))
        }
    }

    fun insertProductToFavorites(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { myRepo.addFavorite(product) }
            insertProductToFavoritesResponse(result.await())
        }
    }

    private suspend fun insertProductToFavoritesResponse(result: NetworkResponse<DraftOrder>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _favoriteProgress.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _favoriteProgress.emit(ResultState.Success(true))
        }
    }

    fun getProductState(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { productInCartUseCase.isFavoriteProduct(productId) }
            _productState.emit(result.await())
        }
    }

    fun getProductInCartState(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { productInCartUseCase.isProductInCart(productId) }
            _productCartState.postValue(result.await())
        }
    }
}
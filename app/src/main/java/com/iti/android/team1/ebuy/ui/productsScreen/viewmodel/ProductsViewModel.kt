package com.iti.android.team1.ebuy.ui.productsScreen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.domain.brandproducts.BrandProductsUseCase
import com.iti.android.team1.ebuy.domain.brandproducts.IBrandProductsUseCase
import com.iti.android.team1.ebuy.data.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.DraftOrder
import com.iti.android.team1.ebuy.data.pojo.Product
import com.iti.android.team1.ebuy.data.pojo.Products
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProductsViewModel(private val repoInterface: IRepository) : ViewModel() {

    private val _productsMutableLivaData: MutableLiveData<ResultState<Products>> = MutableLiveData()

    val productsLiveData: LiveData<ResultState<Products>>
        get() = _productsMutableLivaData

    private val brandProductsUseCase: IBrandProductsUseCase
        get() = BrandProductsUseCase(repoInterface)

    private val _resultOfAddingProductToFavorite: MutableLiveData<ResultState<DraftOrder>> =
        MutableLiveData()
    val resultOfAddingProductToFavorite get() = _resultOfAddingProductToFavorite
    private val _resultOfDeletingProductToFavorite: MutableLiveData<ResultState<DraftOrder>> =
        MutableLiveData()
    val resultOfDeletingProductToFavorite get() = _resultOfDeletingProductToFavorite

    fun getProductsById(id: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                brandProductsUseCase.getProductsByCollectionID(id)
            }
            fetchResult(res.await())
        }
    }

    private fun fetchResult(networkResponse: NetworkResponse<Products>) {
        _productsMutableLivaData.postValue(ResultState.Loading)
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (!it.products.isNullOrEmpty()) {
                        _productsMutableLivaData.postValue(ResultState.Success(it))
                    } else {
                        _productsMutableLivaData.postValue(ResultState.EmptyResult)
                    }
                }
            }
            is NetworkResponse.FailureResponse -> {
                _productsMutableLivaData.postValue(ResultState.Error(networkResponse.errorString))
            }
        }
    }

    fun addProductToFavorite(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                repoInterface.addFavorite(product)
            }
            addFavoriteResponse(result.await())
        }
    }

    private fun addFavoriteResponse(response: NetworkResponse<DraftOrder>) {
        when (response) {
            is NetworkResponse.FailureResponse ->
                _resultOfAddingProductToFavorite.postValue(ResultState.Error(response.errorString))
            is NetworkResponse.SuccessResponse ->
                _resultOfAddingProductToFavorite.postValue(ResultState.Success(response.data))
        }
    }

    fun removeProductFromFavorite(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                repoInterface.removeFromFavorite(productId)
            }
            removeFavoriteResponse(result.await())
        }
    }

    private fun removeFavoriteResponse(response: NetworkResponse<DraftOrder>) {
        when (response) {
            is NetworkResponse.FailureResponse ->
                _resultOfDeletingProductToFavorite.postValue(ResultState.Error(response.errorString))
            is NetworkResponse.SuccessResponse ->
                _resultOfDeletingProductToFavorite.postValue(ResultState.Success(response.data))
        }
    }
}
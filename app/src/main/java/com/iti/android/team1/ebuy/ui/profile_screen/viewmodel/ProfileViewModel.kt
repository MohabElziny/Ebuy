package com.iti.android.team1.ebuy.ui.profile_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class ProfileViewModel(val myRepo: IRepository) : ViewModel() {
    private var _customer: MutableStateFlow<ResultState<Customer>> =
        MutableStateFlow(ResultState.Loading)
    val customer = _customer.asStateFlow()

    private var _orderList: MutableStateFlow<ResultState<List<Order>>> =
        MutableStateFlow(ResultState.Loading)
    val orderList = _orderList.asStateFlow()

    private var _favouriteProducts: MutableStateFlow<ResultState<List<FavoriteProduct>>> =
        MutableStateFlow(ResultState.Loading)
    val favoriteProducts = _favouriteProducts.asStateFlow()

    private val _deleteState: MutableStateFlow<ResultState<Int?>> =
        MutableStateFlow(ResultState.Loading)
    val deleteState get() = _deleteState.asStateFlow()

    fun deleteFavoriteProduct(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { myRepo.deleteProductFromFavorite(productId) }
            setDeleteState(result.await())
        }
    }

    private suspend fun setDeleteState(result: DatabaseResponse<Int?>) {
        when (result) {
            is DatabaseResponse.Failure ->
                _deleteState.emit(ResultState.Error(result.errorMsg))
            is DatabaseResponse.Success -> {
                _deleteState.emit(ResultState.Success(1))
            }
        }
    }

    fun getFavouriteProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getFlowFavoriteProducts()
            }
            sendResponseBackFavourites(res.await())
        }
    }

    private suspend fun sendResponseBackFavourites(favLists: Flow<List<FavoriteProduct>>) {
        _favouriteProducts.emit(ResultState.Loading)

        favLists.buffer().collect { data ->
            if (data.isEmpty())
                _favouriteProducts.emit(ResultState.EmptyResult)
            else
                _favouriteProducts.emit(ResultState.Success(getLastNElements(data, 4)))
        }
    }

    private fun getLastNElements(favLists: List<FavoriteProduct>, n: Int): List<FavoriteProduct> {
        return favLists.takeLast(n)
    }


    fun getCustomerOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getCustomerOrders()
            }
            sendResponseBack(res.await())
        }
    }

    private suspend fun sendResponseBack(networkResponse: NetworkResponse<OrderAPI>) {
        _orderList.emit(ResultState.Loading)
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (networkResponse.data.orders.isNotEmpty()) {
                        _orderList.emit(ResultState.Success(getFirstNElements(it.orders)))
                    } else {
                        _customer.emit(ResultState.EmptyResult)
                    }
                }
            }
            is NetworkResponse.FailureResponse -> {
                _customer.emit(ResultState.Error(networkResponse.errorString))
            }
        }
    }

    private fun getFirstNElements(orders: ArrayList<Order>): List<Order> {
        return orders.take(2)
    }

    fun getCustomerInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getCustomerByID()
            }
            fetchResult(res.await())
        }
    }

    private suspend fun fetchResult(networkResponse: NetworkResponse<Customer>) {
        _customer.emit(ResultState.Loading)
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (!networkResponse.data.email.isNullOrEmpty()) {
                        _customer.emit(ResultState.Success(it))
                    } else {
                        _customer.emit(ResultState.EmptyResult)
                    }
                }
            }
            is NetworkResponse.FailureResponse -> {
                _customer.emit(ResultState.Error(networkResponse.errorString))
            }
        }
    }
}
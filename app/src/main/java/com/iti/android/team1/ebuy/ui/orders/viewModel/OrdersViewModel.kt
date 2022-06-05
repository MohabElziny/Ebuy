package com.iti.android.team1.ebuy.ui.orders.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Order
import com.iti.android.team1.ebuy.model.pojo.OrderAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OrdersViewModel(private val myRepo: IRepository) : ViewModel() {
    private val _customerOrders: MutableStateFlow<ResultState<List<Order>>> =
        MutableStateFlow(ResultState.Loading)
    val customerOrders = _customerOrders.asStateFlow()

    fun getCustomerOrders() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getCustomerOrders(myRepo.getUserIdFromPrefs())
            }
            sendResponseBack(res.await())
        }
    }

    private suspend fun sendResponseBack(networkResponse: NetworkResponse<OrderAPI>) {
        _customerOrders.emit(ResultState.Loading)
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> {
                networkResponse.data.let {
                    if (networkResponse.data.orders.isNotEmpty()) {
                        _customerOrders.emit(ResultState.Success(it.orders))
                    } else {
                        _customerOrders.emit(ResultState.EmptyResult)
                    }
                }
            }
            is NetworkResponse.FailureResponse -> {
                _customerOrders.emit(ResultState.Error(networkResponse.errorString))
            }
        }
    }

}
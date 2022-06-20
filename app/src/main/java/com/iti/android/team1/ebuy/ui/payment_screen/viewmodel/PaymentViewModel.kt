package com.iti.android.team1.ebuy.ui.payment_screen.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.data.repository.IRepository
import com.iti.android.team1.ebuy.model.factories.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Order
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val TAG = "PaymentViewModel"
class PaymentViewModel(private val myRepo: IRepository) : ViewModel() {
    private val _requestSucceed = MutableLiveData<Boolean>()
    val requestSucceed = _requestSucceed as LiveData<Boolean>
    fun postOrder(order: Order) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.postOrder(order)
            }
            handlePostResponse(result.await())
        }
    }

    private fun handlePostResponse(response: NetworkResponse<Order>) {
        when (response) {
            is NetworkResponse.FailureResponse -> {
                Log.i(TAG, "handlePostResponse: ${response.errorString}")
                _requestSucceed.postValue(false)
            }
            is NetworkResponse.SuccessResponse -> {
                response.data.customer?.let {
                    _requestSucceed.postValue(true)
                } ?: run {
                    Log.i(TAG, "handlePostResponse: ${response.data}")
                    _requestSucceed.postValue(false)

                }
            }
        }
    }
}
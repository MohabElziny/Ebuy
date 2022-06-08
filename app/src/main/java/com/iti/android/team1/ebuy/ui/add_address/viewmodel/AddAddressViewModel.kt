package com.iti.android.team1.ebuy.ui.add_address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.model.pojo.AddressApi
import com.iti.android.team1.ebuy.model.pojo.AddressDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAddressViewModel(private val repo: IRepository) : ViewModel() {
    private val _addressState: MutableStateFlow<ResultState<Address>> =
        MutableStateFlow(ResultState.Loading)
    val addressState get() = _addressState.asStateFlow()

    fun addAddress(address: AddressDto) {
        viewModelScope.launch(Dispatchers.IO) {
            _addressState.emit(ResultState.Loading)
            val result =
                async { repo.addAddress(customerId = repo.getUserIdFromPrefs(), address = address) }
            setAddAddressState(result.await())
        }
    }

    private suspend fun setAddAddressState(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _addressState.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _addressState.emit(ResultState.EmptyResult)
        }
    }
}
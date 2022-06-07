package com.iti.android.team1.ebuy.ui.all_addresses.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.model.pojo.Addresses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddressesViewModel(private val repo: IRepository) : ViewModel() {
    private val _addAddressState: MutableStateFlow<ResultState<List<Address>>> =
        MutableStateFlow(ResultState.Loading)
    val allAddressesState get() = _addAddressState.asStateFlow()

    private val _deleteAddressState: MutableStateFlow<ResultState<Address>> =
        MutableStateFlow(ResultState.Loading)
    val deleteAddressState get() = _deleteAddressState.asStateFlow()

    fun deleteAddress(customerId: Long = repo.getUserIdFromPrefs(), addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.deleteAddress(customerId, addressId) }
            setDeleteState(result.await())
        }
    }

    private suspend fun setDeleteState(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _deleteAddressState.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _deleteAddressState.emit(ResultState.EmptyResult)
        }
    }

    fun getAllAddresses(customerId: Long = repo.getUserIdFromPrefs()) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repo.getAllAddresses(customerId) }
            setAddAddress(result.await())
        }
    }

    private suspend fun setAddAddress(result: NetworkResponse<Addresses>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _addAddressState.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.addresses.isEmpty())
                    _addAddressState.emit(ResultState.EmptyResult)
                else
                    _addAddressState.emit(ResultState.Success(data = result.data.addresses))
            }
        }
    }
}
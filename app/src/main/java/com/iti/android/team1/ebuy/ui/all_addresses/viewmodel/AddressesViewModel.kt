package com.iti.android.team1.ebuy.ui.all_addresses.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.factories.ResultState
import com.iti.android.team1.ebuy.data.pojo.Address
import com.iti.android.team1.ebuy.data.pojo.Addresses
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class AddressesViewModel(private val repo: IRepository) : ViewModel() {
    private val _addAddressState: MutableLiveData<ResultState<List<Address>>> =
        MutableLiveData()
    val allAddressesState get() = _addAddressState as LiveData<ResultState<List<Address>>>

    private val _deleteAddressState: MutableLiveData<ResultState<Address>> =
        MutableLiveData()
    val deleteAddressState get() = _deleteAddressState as LiveData<*>

    private val _addressDefState: MutableLiveData<ResultState<Address>> = MutableLiveData()
    val addressDefState get() = _addressDefState as LiveData<ResultState<Address>>

    fun deleteAddress(addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteAddressState.postValue(ResultState.Loading)
            val result = async { repo.deleteAddress(repo.getUserIdFromPrefs(), addressId) }
            setDeleteState(result.await())
        }
    }

    private fun setDeleteState(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _deleteAddressState.postValue(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _deleteAddressState.postValue(ResultState.EmptyResult)
        }
    }

    fun getAllAddresses() {
        viewModelScope.launch(Dispatchers.IO) {
            _deleteAddressState.postValue(ResultState.Loading)
            val result = async { repo.getAllAddresses(repo.getUserIdFromPrefs()) }
            setAddAddress(result.await())
        }
    }

    private fun setAddAddress(result: NetworkResponse<Addresses>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _addAddressState.postValue(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.addresses.isEmpty())
                    _addAddressState.postValue(ResultState.EmptyResult)
                else
                    _addAddressState.postValue(ResultState.Success(data = result.data.addresses))
            }
        }
    }

    fun setAddressAsDef(addressId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            _addressDefState.postValue(ResultState.Loading)
            val result = async { repo.setDefaultAddress(repo.getUserIdFromPrefs(), addressId) }
            setAddressDef(result.await())
        }
    }

    private fun setAddressDef(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _addressDefState.postValue(ResultState.Error(
                result.errorString))
            is NetworkResponse.SuccessResponse -> _addressDefState.postValue(ResultState.EmptyResult)
        }
    }
}
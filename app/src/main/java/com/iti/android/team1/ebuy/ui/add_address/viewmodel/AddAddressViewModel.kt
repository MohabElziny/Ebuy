package com.iti.android.team1.ebuy.ui.add_address.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.data.repository.IRepository
import com.iti.android.team1.ebuy.model.factories.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Address
import com.iti.android.team1.ebuy.model.pojo.AddressDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddAddressViewModel(private val repo: IRepository) : ViewModel() {
    private val _addressState: MutableStateFlow<AddressResult> =
        MutableStateFlow(AddressResult.Loading)
    val addressState get() = _addressState.asStateFlow()

    private val _editAddressState: MutableStateFlow<AddressResult> =
        MutableStateFlow(AddressResult.Loading)
    val editAddressState get() = _editAddressState.asStateFlow()

    fun addAddress(address: AddressDto) {
        _addressState.value = AddressResult.Loading
        if (chekValidation(address, false))
            viewModelScope.launch(Dispatchers.IO) {
                val result =
                    async {
                        repo.addAddress(customerId = repo.getUserIdFromPrefs(),
                            address = address)
                    }
                setAddAddressState(result.await())
            }
    }

    private suspend fun setAddAddressState(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _addressState.emit(AddressResult.AddAddressError(
                result.errorString))
            is NetworkResponse.SuccessResponse -> _addressState.emit(AddressResult.AddAddressSuccessful)
        }
    }

    fun editAddress(addressId: Long, newAddress: AddressDto) {
        _editAddressState.value = AddressResult.Loading
        if (chekValidation(newAddress, true))
            viewModelScope.launch(Dispatchers.IO) {
                val result =
                    async { repo.updateAddress(repo.getUserIdFromPrefs(), addressId, newAddress) }
                setEditAddressState(result.await())
            }
    }

    private suspend fun setEditAddressState(result: NetworkResponse<Address>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _editAddressState.emit(AddressResult.AddAddressError(
                result.errorString))
            is NetworkResponse.SuccessResponse -> _editAddressState.emit(AddressResult.AddAddressSuccessful)
        }
    }

    private fun chekValidation(address: AddressDto, isEditAddress: Boolean): Boolean {
        return when {
            address.addressApi?.address1.isNullOrEmpty() -> {
                emitInputError(DataErrorType.ADDRESS_ERROR, isEditAddress)
                false
            }
            address.addressApi?.city.isNullOrEmpty() -> {
                emitInputError(DataErrorType.CITY_ERROR, isEditAddress)
                false
            }
            address.addressApi?.province.isNullOrEmpty() -> {
                emitInputError(DataErrorType.PROVINCE_ERROR, isEditAddress)
                false
            }
            isPhoneNumberInvalid(address.addressApi?.phone) -> {
                emitInputError(DataErrorType.PHONE_ERROR, isEditAddress)
                false
            }
            else -> true
        }
    }

    private fun emitInputError(error: DataErrorType, isEditAddress: Boolean) {
        if (isEditAddress) _editAddressState.value = AddressResult.InvalidData(error)
        else _addressState.value = AddressResult.InvalidData(error)
    }


    private fun isPhoneNumberInvalid(phone: String?): Boolean {
        return when {
            phone.isNullOrEmpty() -> true
            !phone.matches(Regex("^01[0125][0-9]{8}\$")) -> true
            else -> false
        }
    }
}

sealed class AddressResult {
    object Loading : AddressResult()
    object AddAddressSuccessful : AddressResult()
    data class AddAddressError(val errorString: String) : AddressResult()
    data class InvalidData(val error: DataErrorType) : AddressResult()
}

enum class DataErrorType {
    ADDRESS_ERROR,
    CITY_ERROR,
    PROVINCE_ERROR,
    PHONE_ERROR,
}
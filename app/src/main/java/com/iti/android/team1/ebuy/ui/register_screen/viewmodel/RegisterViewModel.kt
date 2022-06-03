package com.iti.android.team1.ebuy.ui.register_screen.viewmodel


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Customer
import com.iti.android.team1.ebuy.model.pojo.CustomerRegister
import com.iti.android.team1.ebuy.ui.register_screen.ErrorType
import com.iti.android.team1.ebuy.ui.register_screen.RegisterResult
import com.iti.android.team1.ebuy.util.AuthRegex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterViewModel(private val repoInterface: IRepository) : ViewModel() {

    private val _registerMutableLiveData: MutableLiveData<RegisterResult?> = MutableLiveData()
    val registerLiveData = _registerMutableLiveData as LiveData<*>

    fun registerCustomer(customerRegister: CustomerRegister) {

        _registerMutableLiveData.value = RegisterResult.Loading

        when {
            isUserNameInvalid(customerRegister.firstName) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.FirstNameError)
            }
            isUserNameInvalid(customerRegister.lastName) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.LastNameError)
            }
            !AuthRegex.isEmailValid(customerRegister.email) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.EmailError)
            }
            !AuthRegex.isPasswordValid(customerRegister.password) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.PasswordError)
            }
            else -> register(customerRegister)
        }

    }

    private fun register(customerRegister: CustomerRegister) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repoInterface.registerCustomer(customerRegister) }
            setRegisterResult(result.await())
        }
    }

    private fun setRegisterResult(networkResponse: NetworkResponse<Customer>) {
        when (networkResponse) {
            is NetworkResponse.SuccessResponse -> _registerMutableLiveData.postValue(
                RegisterResult.RegisterSuccess(networkResponse.data))

            is NetworkResponse.FailureResponse -> _registerMutableLiveData.postValue(
                RegisterResult.RegisterFail(networkResponse.errorString))
        }
    }


    private fun isUserNameInvalid(userName: String): Boolean {
        return when {
            userName.isEmpty() -> true
            else -> false
        }
    }

}
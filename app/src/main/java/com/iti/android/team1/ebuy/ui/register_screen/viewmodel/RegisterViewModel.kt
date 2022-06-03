package com.iti.android.team1.ebuy.ui.register_screen.viewmodel

import android.util.Patterns
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterViewModel(private val repoInterface:IRepository) : ViewModel() {

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
            isEmailInvalid(customerRegister.email) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.EmailError)
            }
            isPasswordInvalid(customerRegister.password) -> {
                _registerMutableLiveData.value =
                    RegisterResult.InvalidData(ErrorType.PasswordError)
            }
            else -> register(customerRegister)
        }

    }

    private fun register(customerRegister: CustomerRegister) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repoInterface.createCustomer(customerRegister) }
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

    private fun checkUpperCase(password: String): Boolean {
        for (i in password)
            if (i.isUpperCase())
                return true
        return false
    }

    private fun isEmailInvalid(email: String): Boolean {
        return when {
            email.isEmpty() -> true
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> true
            else -> false
        }
    }

    private fun isPasswordInvalid(password: String): Boolean {
        return when {
            password.isEmpty() -> true
            password.length < 8 -> true
            !checkUpperCase(password) -> true
            else -> false
        }
    }

    private fun isUserNameInvalid(userName: String): Boolean {
        return when {
            userName.isEmpty() -> true
            else -> false
        }
    }

}
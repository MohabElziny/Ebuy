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
import com.iti.android.team1.ebuy.ui.register_screen.AuthResult
import com.iti.android.team1.ebuy.util.AuthRegex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class RegisterViewModel(private val repoInterface: IRepository) : ViewModel() {

    private val _registerMutableLiveData: MutableLiveData<AuthResult> = MutableLiveData()
    val registerLiveData = _registerMutableLiveData as LiveData<*>

    fun registerCustomer(customerRegister: CustomerRegister) {

        _registerMutableLiveData.value = AuthResult.Loading

        when {
            isUserNameInvalid(customerRegister.firstName) -> {
                _registerMutableLiveData.value =
                    AuthResult.InvalidData(ErrorType.FirstNameError)
            }
            isUserNameInvalid(customerRegister.lastName) -> {
                _registerMutableLiveData.value =
                    AuthResult.InvalidData(ErrorType.LastNameError)
            }
            !AuthRegex.isEmailValid(customerRegister.email) -> {
                _registerMutableLiveData.value =
                    AuthResult.InvalidData(ErrorType.EmailError)
            }
            !AuthRegex.isPasswordValid(customerRegister.password) -> {
                _registerMutableLiveData.value =
                    AuthResult.InvalidData(ErrorType.PasswordError)
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
            is NetworkResponse.SuccessResponse -> {
                if (networkResponse.data.id != null) {
                    repoInterface.setAuthStateToPrefs(true)
                    setIdsToPrefs(networkResponse.data.id!!,
                        networkResponse.data.favoriteID,
                        networkResponse.data.cartID)
                    _registerMutableLiveData.postValue(
                        AuthResult.RegisterSuccess(networkResponse.data))
                } else
                    _registerMutableLiveData.postValue(AuthResult.RegisterFail("Invalid data"))
            }
            is NetworkResponse.FailureResponse -> _registerMutableLiveData.postValue(
                AuthResult.RegisterFail(networkResponse.errorString))
        }
    }

    private fun setIdsToPrefs(userId: Long, favoriteID: String, cartID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repoInterface.setUserIdToPrefs(userId)

            if (favoriteID.isNotEmpty())
                repoInterface.setFavoritesIdToPrefs(favoriteID)

            if (cartID.isNotEmpty())
                repoInterface.setCartIdToPrefs(cartID)
        }
    }

    private fun isUserNameInvalid(userName: String): Boolean {
        return when {
            userName.isEmpty() -> true
            else -> false
        }
    }

}
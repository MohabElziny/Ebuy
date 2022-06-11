package com.iti.android.team1.ebuy.ui.login_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Customer
import com.iti.android.team1.ebuy.model.pojo.CustomerLogin
import com.iti.android.team1.ebuy.ui.register_screen.AuthResult
import com.iti.android.team1.ebuy.ui.register_screen.ErrorType
import com.iti.android.team1.ebuy.util.AuthRegex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class LoginScreenViewModel(private val repository: IRepository) : ViewModel() {

    private val _loginState: MutableLiveData<AuthResult> =
        MutableLiveData()
    val loginState get() = _loginState as LiveData<AuthResult>


    fun setUserIdToPrefs(userId: Long) = repository.setUserIdToPrefs(userId)

    fun setAuthStateToPrefs(state: Boolean) = repository.setAuthStateToPrefs(state)

    fun getAuthStateFromPrefs() = repository.getAuthStateFromPrefs()

    fun makeLoginRequest(customerLogin: CustomerLogin) {

        when {
            !AuthRegex.isEmailValid(customerLogin.email) -> {
                _loginState.value =
                    AuthResult.InvalidData(ErrorType.EmailError)
            }
            !AuthRegex.isPasswordValid(customerLogin.password) -> {
                _loginState.value =
                    AuthResult.InvalidData(ErrorType.PasswordError)
            }

            else -> {
                _loginState.value = AuthResult.Loading
                viewModelScope.launch(Dispatchers.IO) {
                    val result = async { repository.loginCustomer(customerLogin) }
                    setLoginState(result.await())
                }
            }
        }

    }

    private fun setLoginState(result: NetworkResponse<Customer>) {
        when (result) {
            is NetworkResponse.FailureResponse -> {
                _loginState.postValue(AuthResult.RegisterFail(result.errorString))
            }
            is NetworkResponse.SuccessResponse -> {
                if (result.data.id != null) {
                    setIdsToPrefs(result.data.favoriteID, result.data.cartID)
                    _loginState.postValue(AuthResult.RegisterSuccess(result.data))
                } else
                    _loginState.postValue(AuthResult.RegisterFail("Invalid data"))
            }
        }
    }

    private fun setIdsToPrefs(favoriteID: String, cartID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (favoriteID.isNotEmpty())
                repository.setFavoritesIdToPrefs(favoriteID)

            if (cartID.isNotEmpty())
                repository.setCartIdToPrefs(cartID)
        }
    }
}
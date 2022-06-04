package com.iti.android.team1.ebuy.ui.login_screen.viewmodel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginScreenViewModel(private val repository: IRepository) : ViewModel() {

    private val _loginState: MutableStateFlow<AuthResult> =
        MutableStateFlow(AuthResult.Loading)
    val loginState get() = _loginState.asStateFlow()

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

    fun setUserIdToPrefs(userId: Long) = repository.setUserIdToPrefs(userId)

    fun setAuthStateToPrefs(state: Boolean) = repository.setAuthStateToPrefs(state)

    fun getAuthStateFromPrefs() = repository.getAuthStateFromPrefs()

    private suspend fun setLoginState(result: NetworkResponse<Customer>) {
        when (result) {
            is NetworkResponse.FailureResponse -> {
                _loginState.emit(AuthResult.RegisterFail(result.errorString))
            }
            is NetworkResponse.SuccessResponse -> {
                if (result.data.id != null)
                    _loginState.emit(AuthResult.RegisterSuccess(result.data))
                else
                    _loginState.emit(AuthResult.RegisterFail("Invalid data"))
            }

        }
    }
}
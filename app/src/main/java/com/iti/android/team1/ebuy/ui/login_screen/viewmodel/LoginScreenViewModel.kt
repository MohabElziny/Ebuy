package com.iti.android.team1.ebuy.ui.login_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Customer
import com.iti.android.team1.ebuy.model.pojo.CustomerLogin
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class LoginScreenViewModel(private val repository: IRepository) : ViewModel() {

    private val _loginState: MutableStateFlow<ResultState<Customer>> =
        MutableStateFlow(ResultState.Loading)
    val loginState get() = _loginState.asStateFlow()

    fun makeLoginRequest(email: String, password: String) {

        val customerLogin = CustomerLogin(
            email = email,
            password = repository.encodePassword(password)
        )

        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repository.loginCustomer(customerLogin) }
            setLoginState(result.await(), password)
        }
    }

    fun setUserIdToPrefs(userId: Long) = repository.setUserIdToPrefs(userId)

    fun setAuthStateToPrefs(state: Boolean) = repository.setAuthStateToPrefs(state)

    fun getAuthStateFromPrefs() = repository.getAuthStateFromPrefs()

    private suspend fun setLoginState(result: NetworkResponse<Customer>, password: String) {
        when (result) {
            is NetworkResponse.FailureResponse -> {
                _loginState.emit(ResultState.Error(result.errorString))
            }
            is NetworkResponse.SuccessResponse -> {
                if (password == repository.decodePassword(result.data.password
                        ?: "")
                )
                    _loginState.emit(ResultState.Success(result.data))
                else
                    _loginState.emit(ResultState.Error("Invalid data"))

            }
        }
    }
}
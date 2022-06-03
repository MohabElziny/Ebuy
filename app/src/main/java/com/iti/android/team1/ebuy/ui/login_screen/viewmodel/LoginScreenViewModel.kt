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

    fun makeLoginRequest(customerLogin: CustomerLogin) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = async { repository.loginCustomer(customerLogin) }
            setLoginState(result.await())
        }
    }

    private fun setLoginState(result: NetworkResponse<Customer>) {
        when (result) {
            is NetworkResponse.FailureResponse -> ResultState.Error(result.errorString)
            is NetworkResponse.SuccessResponse -> ResultState.Success(result.data)
        }
    }
}
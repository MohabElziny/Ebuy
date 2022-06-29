package com.iti.android.team1.ebuy.ui.login_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.domain.cart.IProductsInCartUseCase
import com.iti.android.team1.ebuy.domain.cart.ProductsInCartUseCase
import com.iti.android.team1.ebuy.domain.savedItems.ISavedItemsUseCase
import com.iti.android.team1.ebuy.domain.savedItems.SavedItemsUseCase
import com.iti.android.team1.ebuy.model.data.repository.IRepository
import com.iti.android.team1.ebuy.model.factories.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.CartItem
import com.iti.android.team1.ebuy.model.pojo.Customer
import com.iti.android.team1.ebuy.model.pojo.CustomerLogin
import com.iti.android.team1.ebuy.model.pojo.Product
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

    private val savedItemsUseCase: ISavedItemsUseCase
        get() = SavedItemsUseCase(repository)

    private val productsInCartUseCase: IProductsInCartUseCase
        get() = ProductsInCartUseCase(repository)

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

    private suspend fun setLoginState(result: NetworkResponse<Customer>) {
        when (result) {
            is NetworkResponse.FailureResponse ->
                _loginState.postValue(AuthResult.RegisterFail(result.errorString))
            is NetworkResponse.SuccessResponse -> {
                if (result.data.id != null)
                    setIdsToPrefs(result.data)
                else
                    _loginState.postValue(AuthResult.RegisterFail("Invalid data"))
            }
        }
    }

    private suspend fun setIdsToPrefs(customer: Customer) {
        viewModelScope.launch(Dispatchers.IO) {
            if (customer.cartID.isNotEmpty()) {
                repository.setCartIdToPrefs(customer.cartID)
                val result = async { productsInCartUseCase.getAllCartProducts() }
                setCartSizeToPrefs(result.await())
            }

            if (customer.favoriteID.isNotEmpty()) {
                repository.setFavoritesIdToPrefs(customer.favoriteID)
                val result = async { savedItemsUseCase.getFavoriteItems() }
                setFavoriteSizeToPrefs(result.await())
            }
            _loginState.postValue(AuthResult.RegisterSuccess(customer))
        }
    }

    private suspend fun setCartSizeToPrefs(result: NetworkResponse<List<CartItem>>) {
        when (result) {
            is NetworkResponse.FailureResponse -> Unit
            is NetworkResponse.SuccessResponse -> repository.setCartNo(result.data.size)
        }
    }

    private suspend fun setFavoriteSizeToPrefs(result: NetworkResponse<List<Product>>) {
        when (result) {
            is NetworkResponse.FailureResponse -> Unit
            is NetworkResponse.SuccessResponse -> repository.setFavoritesNo(result.data.size)
        }
    }
}
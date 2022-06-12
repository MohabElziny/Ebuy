package com.iti.android.team1.ebuy.ui.cart_screen.viewmodel

import androidx.lifecycle.*
import com.iti.android.team1.ebuy.domain.cart.IProductsInCartUseCase
import com.iti.android.team1.ebuy.domain.cart.ProductsInCartUseCase
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.CartItem
import com.iti.android.team1.ebuy.model.pojo.DraftOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CartViewModel(private val myRepo: IRepository) : ViewModel() {
    private var cartItemList = mutableListOf<CartItem>()
    private val _allCartItems = MutableLiveData<ResultState<List<CartItem>>>(ResultState.Loading)
    val allCartItems: LiveData<ResultState<List<CartItem>>> get() = _allCartItems
    private val _isOverFlow = MutableLiveData(false)
    val isOverFlow: LiveData<Boolean> get() = _isOverFlow
    private val _deleteState =
        MutableStateFlow<ResultState<Boolean>>(ResultState.Loading)
    val deleteState: LiveData<ResultState<Boolean>> get() = _deleteState.asLiveData()

    private val productsInCartUseCase: IProductsInCartUseCase
        get() = ProductsInCartUseCase(myRepo)

    fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                productsInCartUseCase.getAllCartProducts()
            }
            sendResponseBackFavourites(res.await())
        }
    }

    private fun sendResponseBackFavourites(response: NetworkResponse<List<CartItem>>) {
        _allCartItems.postValue(ResultState.Loading)
        when (response) {
            is NetworkResponse.FailureResponse -> {
                _allCartItems.postValue(ResultState.Error(response.errorString))
            }
            is NetworkResponse.SuccessResponse -> {
                cartItemList = response.data.toMutableList()
                if (response.data.isNotEmpty()) {
                    _allCartItems.postValue(ResultState.Success(cartItemList))
                } else {
                    _allCartItems.postValue(ResultState.EmptyResult)
                }
            }
        }
    }

    fun updateToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            myRepo.updateCart(cartItemList.toList())
        }
    }

    fun manipulateCartItem(cart_index: Int, operation: CartItemOperation) {
        when (operation) {
            CartItemOperation.INCREASE -> increaseItemQuantity(cart_index)
            CartItemOperation.DECREASE -> decreaseItemQuantity(cart_index)
            CartItemOperation.DELETE -> deleteItem(cart_index)
        }
        updateItemList()
    }

    private fun updateItemList() {
        viewModelScope.launch(Dispatchers.Main) {
            if (cartItemList.isNotEmpty())
                _allCartItems.postValue(ResultState.Success(cartItemList))
            else
                _allCartItems.postValue(ResultState.EmptyResult)
        }
    }

    private fun increaseItemQuantity(cart_index: Int) {
        _isOverFlow.postValue(false)
        val item = cartItemList[cart_index]
        if (item.customerProductQuantity < item.variantInventoryQuantity)
            cartItemList[cart_index].customerProductQuantity++
        else {
            _isOverFlow.postValue(true)
        }

    }

    private fun decreaseItemQuantity(cart_index: Int) {
        val item = cartItemList[cart_index]
        if (item.customerProductQuantity > 1)
            cartItemList[cart_index].customerProductQuantity--
    }

    private suspend fun setDeleteState(result: NetworkResponse<DraftOrder>) {
        when (result) {
            is NetworkResponse.FailureResponse -> _deleteState.emit(ResultState.Error(result.errorString))
            is NetworkResponse.SuccessResponse -> _deleteState.emit(ResultState.Success(true))
        }
    }

    private fun deleteItem(cart_index: Int) {
        _deleteState.value = ResultState.Loading
        val item = cartItemList[cart_index]
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.removeFromCart(item.productID)
            }
            setDeleteState(result.await())
        }
        cartItemList.removeAt(cart_index)
    }


    enum class CartItemOperation {
        INCREASE,
        DECREASE,
        DELETE
    }
}



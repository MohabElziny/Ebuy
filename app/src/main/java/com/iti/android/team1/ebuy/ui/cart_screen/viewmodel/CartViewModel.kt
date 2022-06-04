package com.iti.android.team1.ebuy.ui.cart_screen.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.DatabaseResult
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CartViewModel(private val myRepo: IRepository) : ViewModel() {
    private var cartItemList = mutableListOf<CartItem>()
    private val _allCartItems = MutableLiveData<ResultState<List<CartItem>>>(ResultState.Loading)
    val allCartItems: LiveData<ResultState<List<CartItem>>> get() = _allCartItems
    private val _isOverFlow = MutableLiveData(false)
    val isOverFlow: LiveData<Boolean> get() = _isOverFlow
    private val _deleteState =
        MutableLiveData<DatabaseResult<Int?>>(DatabaseResult.Loading)
    val deleteState: LiveData<DatabaseResult<Int?>> get() = _deleteState

    fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getAllCartProducts()
            }
            sendResponseBackFavourites(res.await())
        }
    }

    private fun sendResponseBackFavourites(items: List<CartItem>) {
        cartItemList = items.toMutableList()
        _allCartItems.postValue(ResultState.Loading)
        if (items.isNotEmpty()) {
            _allCartItems.postValue(ResultState.Success(cartItemList))
        } else {
            _allCartItems.postValue(ResultState.EmptyResult)
        }
    }

    fun updateToDB() {
        viewModelScope.launch(Dispatchers.IO) {
            cartItemList.forEach {
                myRepo.updateCartItem(it)
            }
        }
        ////
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


    private fun setDeleteState(result: DatabaseResponse<Int?>) {
        when (result) {
            is DatabaseResponse.Failure -> _deleteState.postValue(
                DatabaseResult.Error(result.errorMsg))
            is DatabaseResponse.Success -> {
                _deleteState.postValue(DatabaseResult.Empty)
            }
        }
    }

    private fun deleteItem(cart_index: Int) {
        val item = cartItemList[cart_index]
        viewModelScope.launch(Dispatchers.IO) {
            val result = async {
                myRepo.removeProductFromCart(item.productVariantID)
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



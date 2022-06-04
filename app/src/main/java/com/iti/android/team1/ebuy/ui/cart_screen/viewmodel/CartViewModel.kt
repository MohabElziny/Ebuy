package com.iti.android.team1.ebuy.ui.cart_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.CartItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.launch

class CartViewModel(val myRepo: IRepository) : ViewModel() {
    private val _allCartItems = MutableStateFlow<ResultState<List<CartItem>>>(ResultState.Loading)
    var allCartItems = _allCartItems.asStateFlow()

    fun getAllCartItems() {
        viewModelScope.launch(Dispatchers.IO) {
            val res = async {
                myRepo.getAllCartProducts()
            }
            sendResponseBackFavourites(res.await())
        }
    }

    private suspend fun sendResponseBackFavourites(flowItems: Flow<List<CartItem>>) {
        _allCartItems.emit(ResultState.Loading)
        flowItems.buffer().collect { items ->
            if (items.isNotEmpty()) {
                _allCartItems.emit(ResultState.Success(items))
            } else {
                _allCartItems.emit(ResultState.EmptyResult)
            }
        }
    }

}
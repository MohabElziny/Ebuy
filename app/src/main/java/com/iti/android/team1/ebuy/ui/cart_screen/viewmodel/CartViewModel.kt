package com.iti.android.team1.ebuy.ui.cart_screen.viewmodel

import androidx.lifecycle.*
import com.iti.android.team1.ebuy.domain.cart.IProductsInCartUseCase
import com.iti.android.team1.ebuy.domain.cart.ProductsInCartUseCase
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val DELIVER = 50L

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

    private val _subTotal = MutableStateFlow(0L)
    val subTotal = _subTotal.asStateFlow()

    private val _total = MutableStateFlow(0L)
    val total = _total.asStateFlow()

    private val _oder = MutableLiveData<Order>()
    val order = _oder as LiveData<Order>
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
                   viewModelScope.launch(Dispatchers.IO) {
                       updateCosts()
                   }
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
            if (cartItemList.isNotEmpty()) {
              updateCosts()
            } else
                _allCartItems.postValue(ResultState.EmptyResult)
        }
    }
    private suspend fun updateCosts(){
        val sum = cartItemList.sumOf {
            it.productVariantPrice * it.customerProductQuantity
        }.toLong()
        _allCartItems.postValue(ResultState.Success(cartItemList))
        _subTotal.emit(sum)
        _total.emit(sum + DELIVER)
    }

     fun makeOrder() {
        viewModelScope.launch(Dispatchers.Default) {
            total.collect {
                makeOrderRequest(cartItemList, it)
            }
        }
    }

    private  fun makeOrderRequest(
        cartItemList: MutableList<CartItem>,
        currentTotalPrice: Long,
    ) {
        val lineItems = mutableListOf<LineItems>()
        cartItemList.forEach {
            lineItems.add(DraftsLineItemConverter.convertToLineItem(it))
        }
        val orderMade = Order(lineItems = lineItems as ArrayList<LineItems>,
            currentTotalPrice = "$currentTotalPrice")

        _oder.postValue(orderMade)

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



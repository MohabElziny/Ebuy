package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.networkresponse.ResultState
import com.iti.android.team1.ebuy.model.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddToCartViewModel(private val repository: IRepository) : ViewModel() {

    private val _plusButtonState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val plusButtonsState = _plusButtonState.asStateFlow()

    private val _minusButtonState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val minusButtonState = _minusButtonState.asStateFlow()

    private val _quantityText: MutableStateFlow<Int> = MutableStateFlow(1)
    val quantityText = _quantityText.asStateFlow()

    private val _addProductClicked: MutableStateFlow<ResultState<Boolean>> =
        MutableStateFlow(ResultState.Loading)
    val addProductClicked = _addProductClicked.asStateFlow()

    private var quantity = 1
    private var productQuantity = 0

    fun setProductQuantity(productQuantity: Int) {
        this.productQuantity = productQuantity
    }

    fun insertProductToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val response = repository.addCart(product, quantity)) {
                is NetworkResponse.FailureResponse -> _addProductClicked.emit(ResultState.Error(
                    response.errorString))
                is NetworkResponse.SuccessResponse -> _addProductClicked.emit(ResultState.Success(
                    true))
            }
        }
    }

    fun onPressPlusButton() {
        if (quantity < productQuantity) {
            ++quantity
            emitQuantityState()
            _minusButtonState.value = true
        } else {
            _plusButtonState.value = false
        }
    }

    fun onPressMinusButton() {
        if (quantity > 1) {
            --quantity
            emitQuantityState()
            _plusButtonState.value = true
        } else {
            _minusButtonState.value = false
        }
    }

    private fun emitQuantityState() {
        _quantityText.value = quantity
    }
}
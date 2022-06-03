package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddToCartViewModel(private val repository: IRepository) : ViewModel() {

    private var _plusButtonState: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val plusButtonsState = _plusButtonState.asStateFlow()

    private var _minusButtonState: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val minusButtonState = _minusButtonState.asStateFlow()

    private var _quantityText: MutableStateFlow<Int> = MutableStateFlow(1)
    val quantityText = _quantityText.asStateFlow()

    private var quantity = 1
    private var productQuantity = 0

    fun setProductQuantity(productQuantity: Int) {
        this.productQuantity = productQuantity
    }

    fun insertProductToCart(product: Product) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProductToCart(product, quantity)
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
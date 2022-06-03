package com.iti.android.team1.ebuy.ui.product_details_screen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.pojo.Product
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddToCartViewModel(private val repository: IRepository) : ViewModel() {

    fun insertProductToCart(product: Product, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addProductToCart(product, quantity)
        }
    }
}
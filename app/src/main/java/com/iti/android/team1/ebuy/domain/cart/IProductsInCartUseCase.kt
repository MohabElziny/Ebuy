package com.iti.android.team1.ebuy.domain.cart

import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.pojo.CartItem

interface IProductsInCartUseCase {
    suspend fun getAllCartProducts(): NetworkResponse<List<CartItem>>
}
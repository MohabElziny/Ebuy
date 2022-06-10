package com.iti.android.team1.ebuy.domain.cart

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.CartItem

interface IProductsInCartUseCase {
    suspend fun getAllCartProducts():NetworkResponse<List<CartItem>>
}
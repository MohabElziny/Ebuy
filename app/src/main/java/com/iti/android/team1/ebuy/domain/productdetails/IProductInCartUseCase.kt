package com.iti.android.team1.ebuy.domain.productdetails

interface IProductInCartUseCase {
    suspend fun isProductInCart(productID: Long): Boolean
}
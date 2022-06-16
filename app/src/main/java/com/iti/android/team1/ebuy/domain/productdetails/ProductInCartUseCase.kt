package com.iti.android.team1.ebuy.domain.productdetails

import com.iti.android.team1.ebuy.model.data.repository.IRepository
import com.iti.android.team1.ebuy.model.factories.NetworkResponse

class ProductInCartUseCase(private val repository: IRepository) : IProductInCartUseCase {
    override suspend fun isProductInCart(productID: Long): Boolean {
        return when (val cartResponse = repository.getCartItems()) {
            is NetworkResponse.FailureResponse -> false
            is NetworkResponse.SuccessResponse -> {
                val lineItems = cartResponse.data.draftOrder.lineItems
                if (lineItems.isNotEmpty()) {
                    val cartMap = lineItems.associateBy {
                        it.productId
                    }
                    cartMap.containsKey(productID)
                } else {
                    false
                }
            }
        }
    }

    override suspend fun isFavoriteProduct(productID: Long): Boolean {
        return when (val favoriteProduct = repository.getFavoriteItems()) {
            is NetworkResponse.FailureResponse -> false
            is NetworkResponse.SuccessResponse -> {
                val lineItems = favoriteProduct.data.draftOrder.lineItems
                if (lineItems.isNotEmpty()) {
                    val cartMap = lineItems.associateBy {
                        it.productId
                    }
                    cartMap.containsKey(productID)
                } else {
                    false
                }
            }
        }
    }
}
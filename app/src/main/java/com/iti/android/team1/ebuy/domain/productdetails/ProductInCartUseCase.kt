package com.iti.android.team1.ebuy.domain.productdetails

import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse

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
}
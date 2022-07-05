package com.iti.android.team1.ebuy.domain.cart

import com.iti.android.team1.ebuy.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.pojo.CartItem
import com.iti.android.team1.ebuy.data.pojo.CartItemConverter
import com.iti.android.team1.ebuy.data.pojo.DraftsLineItems
import com.iti.android.team1.ebuy.data.pojo.Product

class ProductsInCartUseCase(private val repository: IRepository) : IProductsInCartUseCase {
    private val cartItems: MutableList<CartItem> = mutableListOf()

    override suspend fun getAllCartProducts(): NetworkResponse<List<CartItem>> {
        return when (val response = repository.getCartItems()) {
            is NetworkResponse.FailureResponse -> response
            is NetworkResponse.SuccessResponse -> getCartItems(response.data.draftOrder.lineItems)
        }
    }

    private suspend fun getCartItems(
        lineItems: ArrayList<DraftsLineItems>,
    ): NetworkResponse<List<CartItem>> {
        if (lineItems.count() < 0) return NetworkResponse.SuccessResponse(emptyList())
        lineItems.forEach { item ->
            when (val response = repository.getProductDetails(item.productId)) {
                is NetworkResponse.SuccessResponse -> convertProductToCartItem(
                    response.data,
                    item.quantity,
                )
                is NetworkResponse.FailureResponse -> return NetworkResponse.SuccessResponse(
                    emptyList())
            }
        }
        return NetworkResponse.SuccessResponse(cartItems.toList())
    }

    private fun convertProductToCartItem(product: Product, quantity: Int) {
        cartItems.add(CartItemConverter.convertProductToCartItemEntity(product, quantity))
    }
}
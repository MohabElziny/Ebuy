package com.iti.android.team1.ebuy.domain.savedItems

import com.iti.android.team1.ebuy.data.data.repository.IRepository
import com.iti.android.team1.ebuy.data.factories.NetworkResponse
import com.iti.android.team1.ebuy.data.pojo.DraftsLineItems
import com.iti.android.team1.ebuy.data.pojo.Product

class SavedItemsUseCase(private val repository: IRepository) : ISavedItemsUseCase {

    private val productList: MutableList<Product> = mutableListOf()

    override suspend fun getFavoriteItems(): NetworkResponse<List<Product>> {
        return when (val response = repository.getFavoriteItems()) {
            is NetworkResponse.FailureResponse -> response
            is NetworkResponse.SuccessResponse -> getProducts(response.data.draftOrder.lineItems)
        }
    }

    private suspend fun getProducts(lineItems: ArrayList<DraftsLineItems>): NetworkResponse<List<Product>> {
        if (lineItems.count() < 0) return NetworkResponse.SuccessResponse(emptyList())
        lineItems.forEach { item ->
            when (val response = repository.getProductDetails(item.productId)) {
                is NetworkResponse.SuccessResponse -> productList.add(response.data)
                is NetworkResponse.FailureResponse -> {}
            }
        }
        return NetworkResponse.SuccessResponse(productList.toList())
    }
}
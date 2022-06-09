package com.iti.android.team1.ebuy.domain.brandproducts

import com.iti.android.team1.ebuy.domain.productsResponse
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.DraftsLineItems
import com.iti.android.team1.ebuy.model.pojo.Products

class BrandProductsUseCase(private val repository: IRepository) : IBrandProductsUseCase {
    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val productsResponse = repository.getProductsByCollectionID(collectionID)
        return checkFavorites(productsResponse)
    }

    private suspend fun checkFavorites(productsResponse: NetworkResponse<Products>): NetworkResponse<Products> {
        val allDraftsLineItems = getDraftLineItems()
        return if (allDraftsLineItems.isNullOrEmpty()) {
            productsResponse
        } else {
            productsResponse(productsResponse, allDraftsLineItems)
        }
    }

    private suspend fun getDraftLineItems(): List<DraftsLineItems>? {
        return when (val response = repository.getCustomerByID()) {
            is NetworkResponse.FailureResponse -> null
            is NetworkResponse.SuccessResponse -> {
                if (response.data.favoriteID.isEmpty()) {
                    null
                } else {
                    getDrafts(response.data.favoriteID.toLong())
                }
            }
        }
    }

    private suspend fun getDrafts(favoriteID: Long): List<DraftsLineItems>? {
        return when (val response = repository.getDraftFromApi(favoriteID)) {
            is NetworkResponse.FailureResponse -> null
            is NetworkResponse.SuccessResponse -> response.data.draftOrder.lineItems
        }
    }

}
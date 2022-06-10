package com.iti.android.team1.ebuy.domain.category

import com.iti.android.team1.ebuy.domain.productsResponse
import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.DraftsLineItems
import com.iti.android.team1.ebuy.model.pojo.Products

class CategoryProductsUseCase(
    private val repository: IRepository,
) : ICategoryProductsUseCase {

    override suspend fun getAllProducts(): NetworkResponse<Products> {
        val allProducts = repository.getAllProducts()
        return checkFavorites(allProducts)
    }

    override suspend fun getAllCategories(): NetworkResponse<Categories> {
        return repository.getAllCategories()
    }

    override suspend fun getAllCategoryProductsByType(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products> {
        val networkResponse = repository.getAllCategoryProducts(collectionID, productType)
        return checkFavorites(networkResponse)
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val networkResponse = repository.getProductsByCollectionID(collectionID)
        return checkFavorites(networkResponse)
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
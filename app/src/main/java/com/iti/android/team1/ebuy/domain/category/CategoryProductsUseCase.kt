package com.iti.android.team1.ebuy.domain.category

import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Categories
import com.iti.android.team1.ebuy.model.pojo.Products

class CategoryProductsUseCase(
    private val repository: IRepository,
) : ICategoryProductsUseCase {

    override suspend fun getAllProducts(): NetworkResponse<Products> {
        return productsResponse(repository.getAllProducts())
    }

    override suspend fun getAllCategories(): NetworkResponse<Categories> {
        return repository.getAllCategories()
    }

    override suspend fun getAllCategoryProductsByType(
        collectionID: Long,
        productType: String,
    ): NetworkResponse<Products> {
        val networkResponse = repository.getAllCategoryProducts(collectionID, productType)
        return productsResponse(networkResponse)
    }

    private suspend fun productsResponse(networkResponse: NetworkResponse<Products>): NetworkResponse<Products> {
        when (networkResponse) {
            is NetworkResponse.FailureResponse -> return networkResponse
            is NetworkResponse.SuccessResponse -> {
                val productMap = networkResponse.data.products?.associateBy {
                    it.productID
                }

                repository.getAllFavoritesProducts().forEach {
                    productMap?.get(it.productID)?.apply {
                        isFavorite = true
                    }
                }

                return NetworkResponse.SuccessResponse(Products(productMap?.values?.toList()
                    ?: emptyList()))
            }
        }
    }

    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        val networkResponse = repository.getProductsByCollectionID(collectionID)
        return productsResponse(networkResponse)
    }

}
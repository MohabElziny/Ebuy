package com.iti.android.team1.ebuy.domain.brandproducts

import com.iti.android.team1.ebuy.model.datasource.repository.IRepository
import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.Products

class BrandProductsUseCase(private val repository: IRepository) : IBrandProductsUseCase {
    override suspend fun getProductsByCollectionID(collectionID: Long): NetworkResponse<Products> {
        return productsResponse(repository.getProductsByCollectionID(collectionID))
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

}
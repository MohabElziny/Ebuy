package com.iti.android.team1.ebuy.domain

import com.iti.android.team1.ebuy.model.networkresponse.NetworkResponse
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import com.iti.android.team1.ebuy.model.pojo.Products

fun productsResponse(
    networkResponse: NetworkResponse<Products>,
    allFavoritesProducts: List<FavoriteProduct>,
): NetworkResponse<Products> {
    when (networkResponse) {
        is NetworkResponse.FailureResponse -> return networkResponse
        is NetworkResponse.SuccessResponse -> {
            val productMap = networkResponse.data.products?.associateBy {
                it.productID
            }

            allFavoritesProducts.forEach {favoriteProduct ->
                productMap?.get(favoriteProduct.productID)?.apply {
                    isFavorite = true
                }
            }

            return NetworkResponse.SuccessResponse(Products(productMap?.values?.toList()
                ?: emptyList()))
        }
    }
}
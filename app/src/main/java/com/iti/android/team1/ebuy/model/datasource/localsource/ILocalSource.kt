package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

interface ILocalSource {
    suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct): Long
    suspend fun removeProductFromFavorites(productID: Long): Int
    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>
    suspend fun removeAllFavoriteProducts()
    suspend fun isFavoriteProduct(productID: Long): Boolean
    suspend fun updateFavoriteProduct(favoriteProduct: FavoriteProduct) : Int
}
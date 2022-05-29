package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

interface ILocalSource {
    suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct)
    suspend fun removeProductFromFavorites(favoriteProduct: FavoriteProduct)
    suspend fun getAllFavoriteProducts(): List<FavoriteProduct>
    suspend fun removeAllFavoriteProducts()
    suspend fun isFavoriteProduct(productID: Long): Boolean
}
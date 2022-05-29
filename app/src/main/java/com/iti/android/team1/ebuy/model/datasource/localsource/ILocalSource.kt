package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.DatabaseResponse
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

interface ILocalSource {
    suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct): Long
    suspend fun removeProductFromFavorites(productID: Long): Int
    suspend fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>>
    suspend fun removeAllFavoriteProducts()
}
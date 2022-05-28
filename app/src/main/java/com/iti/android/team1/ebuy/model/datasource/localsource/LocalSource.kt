package com.iti.android.team1.ebuy.model.datasource.localsource

import android.content.Context
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

class LocalSource(
    private val context: Context,
    private val favoritesDao: FavoritesDao = CommerceDatabase.getDataBase(context)
        .favoritesDao(),
) : ILocalSource {

    override suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct) {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductFromFavorites(favoriteProduct: FavoriteProduct) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>> {
        return favoritesDao.getAllFavoriteProducts()
    }

    override suspend fun removeAllFavoriteProducts() {
        favoritesDao.removeAllFavoriteProducts()
    }

    override suspend fun isFavoriteProduct(productID: Long): Boolean {
        TODO("Not yet implemented")
    }
}
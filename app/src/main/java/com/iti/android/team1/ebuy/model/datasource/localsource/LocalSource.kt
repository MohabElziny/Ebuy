package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

class LocalSource(private val favoritesDao: FavoritesDao) : ILocalSource {
    override suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct) {
        favoritesDao.insertProductToFavorite(favoriteProduct)
    }

    override suspend fun removeProductFromFavorites(favoriteProduct: FavoriteProduct) {
        favoritesDao.deleteProductFromFavorite(favoriteProduct)
    }

    override suspend fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>> {
        TODO("Not yet implemented")
    }

    override suspend fun removeAllFavoriteProducts() {
        TODO("Not yet implemented")
    }

    override suspend fun isFavoriteProduct(productID: Long): Boolean {
        TODO("Not yet implemented")
    }
}
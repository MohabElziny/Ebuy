package com.iti.android.team1.ebuy.model.datasource.localsource

import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

class LocalSource : ILocalSource {
    override suspend fun addProductToFavorites(favoriteProduct: FavoriteProduct) {
        TODO("Not yet implemented")
    }

    override suspend fun removeProductFromFavorites(favoriteProduct: FavoriteProduct) {
        TODO("Not yet implemented")
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
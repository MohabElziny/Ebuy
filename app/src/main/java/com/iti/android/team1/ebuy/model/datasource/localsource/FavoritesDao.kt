package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Dao
import androidx.room.Query
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>>

    @Query("DELETE FROM favorite_products")
    fun removeAllFavoriteProducts()

    @Insert()
    suspend fun insertProductToFavorite(favoriteProduct: FavoriteProduct)

    @Delete
    suspend fun deleteProductFromFavorite(favoriteProduct:FavoriteProduct)

}
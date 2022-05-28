package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Dao
import androidx.room.Query
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>>

    @Query("DELETE FROM favorite_products")
    fun removeAllFavoriteProducts()
}
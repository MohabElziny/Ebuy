package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

@Dao
interface FavoritesDao {

    @Insert()
    suspend fun insertProductToFavorite(favoriteProduct: FavoriteProduct)

    @Delete
    suspend fun deleteProductFromFavorite(favoriteProduct:FavoriteProduct)

}
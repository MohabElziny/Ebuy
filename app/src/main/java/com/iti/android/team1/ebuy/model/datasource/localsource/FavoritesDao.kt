package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import androidx.room.Update
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): List<FavoriteProduct>

    @Query("DELETE FROM favorite_products")
    fun removeAllFavoriteProducts()

    @Insert(onConflict = REPLACE)
    suspend fun insertProductToFavorite(favoriteProduct: FavoriteProduct): Long

    @Update
    suspend fun updateFavoriteProduct(favoriteProduct: FavoriteProduct) : Int

    @Query("DELETE FROM favorite_products where productID = :productId")
    suspend fun deleteProductFromFavorite(productId: Long): Int

    @Query("SELECT EXISTS(SELECT productID FROM favorite_products WHERE productID= :productID)")
    suspend fun isFavouriteProduct(productID: Long): Boolean

}
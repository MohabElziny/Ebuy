package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.*
import androidx.room.OnConflictStrategy.IGNORE
import com.iti.android.team1.ebuy.model.pojo.FavoriteProduct
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoritesDao {
    @Query("SELECT * FROM favorite_products")
    fun getAllFavoriteProducts(): Flow<List<FavoriteProduct>>

    @Query("DELETE FROM favorite_products")
    fun removeAllFavoriteProducts()

    @Insert(onConflict = IGNORE)
    suspend fun insertProductToFavorite(favoriteProduct: FavoriteProduct)

    @Delete
    suspend fun deleteProductFromFavorite(favoriteProduct:FavoriteProduct)

    @Query("SELECT EXISTS(SELECT productID FROM favorite_products WHERE productID= :productID)")
    suspend fun isFavouriteProduct(productID:Long) :Boolean

}
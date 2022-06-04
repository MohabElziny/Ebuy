package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.iti.android.team1.ebuy.model.pojo.CartItem
import kotlinx.coroutines.flow.Flow

@Dao
interface CartDao {
    @Query("SELECT * FROM cartItems")
     fun getAllItemsInCart(): Flow<List<CartItem>>

    @Query("DELETE FROM cartItems")
    suspend fun removeAllItemsFromCart(): Int

    @Insert
    suspend fun insertItemToCart(cartItem: CartItem): Long

    @Query("DELETE FROM cartItems WHERE productVariantID = :productVariantID")
    suspend fun removeItemFromCart(productVariantID: Long): Int

    @Update
    suspend fun updateItemInTheCart(cartItem: CartItem)
}
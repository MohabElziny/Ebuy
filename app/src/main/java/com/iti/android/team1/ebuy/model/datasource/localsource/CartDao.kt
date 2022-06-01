package com.iti.android.team1.ebuy.model.datasource.localsource

import androidx.room.*
import com.iti.android.team1.ebuy.model.pojo.CartItem

@Dao
interface CartDao {
    @Query("SELECT * FROM cartItems")
    suspend fun getAllItemsInCart(): List<CartItem>

    @Query("DELETE FROM cartItems")
    suspend fun removeAllItemsFromCart(): Int

    @Insert
    suspend fun insertItemToCart(cartItem: CartItem): Long

    @Query("DELETE FROM cartItems WHERE productVariantID = :productVariantID")
    suspend fun removeItemFromCart(productVariantID: Long): Int

    @Update
    suspend fun updateItemInTheCart(cartItem: CartItem)
}